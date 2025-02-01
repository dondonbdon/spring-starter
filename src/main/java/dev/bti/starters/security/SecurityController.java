package dev.bti.starters.security;

import dev.bti.starters.exceptions.auth.AuthException;
import dev.bti.starters.models.req.Credential;
import dev.bti.starters.models.res.ErrorResponse;
import dev.bti.starters.models.res.Response;
import dev.bti.starters.models.res.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/security")
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityService service;

    @PostMapping("/request-reset-code")
    public ResponseEntity<?> requestVerificationCodeFor(@RequestBody Credential credential) {
        try {
            return ResponseEntity.ok().body(SuccessResponse.builder()
                    .success(true)
                    .data(service.requestVerificationCodeFor(credential))
                    .message("Reset code sent successfully")
                    .build());
        } catch (AuthException e) {
            return ResponseEntity.status(e.getStatus()).body(Response.builder()
                    .success(false)
                    .message("Failed to send reset code")

                    .build());
        }
    }

    @PostMapping("/validate-reset-code")
    public ResponseEntity<?> validateVerificationCodeFor(@RequestBody Credential credential, @RequestParam String resetCode) {
        try {
            service.validateVerificationCodeFor(credential, resetCode);
            return ResponseEntity.ok().body(SuccessResponse.builder()
                    .success(true)
                    .message("Reset code validated successfully")
                    .build());
        } catch (AuthException e) {
            return ResponseEntity.status(e.getStatus()).body(ErrorResponse.builder()
                    .success(false)
                    .message("Failed to validate reset code")
                    .error(e)
                    .build());
        }
    }
}
