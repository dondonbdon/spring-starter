package dev.bti.starters.models.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRequest {
    String firstName, lastName, email, phone, password,
            state, gender;
    Instant dob;
    Boolean isVerified, isAdmin;
    List<String> collectiveIds;
}
