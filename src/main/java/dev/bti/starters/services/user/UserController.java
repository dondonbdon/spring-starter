package dev.bti.starters.services.user;

import dev.bti.starters.exceptions.Exception;
import dev.bti.starters.exceptions.auth.AuthException;
import dev.bti.starters.models.req.UserRequest;
import dev.bti.starters.models.res.Response;
import dev.bti.starters.models.res.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private UserService service;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable String userId) {
        try {
            return ResponseEntity.ok().body(SuccessResponse.builder()
                    .success(true)
                    .message("User retrieved successfully")
                    .data(service.getUserProfile(userId))
                    .build());
        } catch (AuthException e) {
            return ResponseEntity.status(e.getStatus()).body(Response.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody UserRequest userRequest) {
        try {
            service.updateUser(userId, userRequest);
            return ResponseEntity.ok().body(SuccessResponse.builder()
                    .success(true)
                    .message("User updated successfully")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(e.getStatus()).body(Response.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    @PostMapping("/{userId}/invalidate")
    public ResponseEntity<?> invalidateUserToken(@PathVariable String userId,
                                                 @RequestHeader("Authorization") String token) {

        try {
            service.invalidateUserToken(userId, token);
            return ResponseEntity.ok().body(Response.builder()
                    .success(true)
                    .message("Invalidated token appropriately")
                    .build());
        } catch (AuthException e) {
            return ResponseEntity.status(e.getStatus()).body(Response.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }
}
