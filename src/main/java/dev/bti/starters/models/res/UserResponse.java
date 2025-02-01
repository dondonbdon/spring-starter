package dev.bti.starters.models.res;

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
public class UserResponse {

    String id;
    String title, firstName, lastName;
    String email;
    String phone;
    Boolean isVerified;
    List<Instant> logins;
    List<Instant> updates;
}
