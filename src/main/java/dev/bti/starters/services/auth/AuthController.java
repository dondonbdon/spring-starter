package dev.bti.starters.services.auth;

import dev.bti.starters.exceptions.auth.AuthException;
import dev.bti.starters.models.req.Credential;
import dev.bti.starters.models.req.LoginRequest;
import dev.bti.starters.models.req.UserRequest;
import dev.bti.starters.models.res.ErrorResponse;
import dev.bti.starters.models.res.Response;
import dev.bti.starters.models.res.SuccessResponse;
import dev.bti.starters.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {
        try {
            Response response = SuccessResponse.builder()
                    .success(true)
                    .message("User created successfully")
                    .data(authService.createUser(userRequest))
                    .build();
            return ResponseEntity.ok().body(response);
        } catch (AuthException e) {
            return ResponseEntity.badRequest().body(Response.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            return ResponseEntity.ok().body(SuccessResponse.builder()
                    .success(true)
                    .message("User logged in successfully")
                    .data(authService.loginUser(loginRequest))
                    .build());
        } catch (AuthException e) {
            return ResponseEntity.status(e.getStatus()).body(ErrorResponse.builder()
                    .success(false)
                    .message("User login failed")
                    .error(e)
                    .build());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Credential credential, @RequestParam String newPassword) {
        try {
            authService.resetPassword(credential, newPassword);
            return ResponseEntity.ok().body(SuccessResponse.builder()
                    .success(true)
                    .message("Password reset successfully")
                    .build());
        } catch (AuthException e) {
            return ResponseEntity.status(e.getStatus()).body(Response.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    @PostMapping("/find-user")
    public ResponseEntity<?> findUser(@RequestBody Credential credential) {
        try {
            if (userService.userExists(credential))
                return ResponseEntity.ok().body(SuccessResponse.builder()
                        .success(true)
                        .message("User exists")
                        .build());
            else return ResponseEntity.ok().body(SuccessResponse.builder()
                    .success(true)
                    .message("User not found")
                    .build());
        } catch (AuthException e) {
            return ResponseEntity.status(e.getStatus()).body(Response.builder()
                    .success(true)
                    .message(e.getMessage())
                    .build());
        }
    }
}
