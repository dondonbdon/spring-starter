package dev.bti.starters.models.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserProfile {

    private String id;
    private String title;
    private String firstName;
    private String lastName;
    private String profileUrl;
}
