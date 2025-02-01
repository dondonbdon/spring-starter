package dev.bti.starters.models.main;

import dev.bti.starters.common.Constants;
import dev.bti.starters.models.req.UserRequest;
import dev.bti.starters.models.res.UserProfile;
import dev.bti.starters.models.res.UserResponse;
import dev.bti.starters.util.GeneratorUtils;
import dev.bti.starters.util.JwtUtils;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.util.*;

@Document(collection = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String title;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String phone;
    private String gender;
    private String state;
    private Instant dob;
    private List<Instant> logins = Collections.emptyList();
    private List<Instant> updates = Collections.emptyList();
    private Boolean isVerified = false;
    private Boolean lockedMaxAttempts = false;
    private Integer failedAttempts = 0;
    private Instant lockedFailedAttemptsTimestampEnd;
    private List<String> tokens, collectiveIds = Collections.emptyList();
    private String profileId;
    private List<String> validOTPs = new ArrayList<>();
    private Map<String, Instant> validOTPsTimestampEnd = new HashMap<>();
    private boolean acceptPasswordResets = false;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles = new HashSet<>();

    /**
     * Constructor to initialize a User object from a UserReq object.
     */
    public User(UserRequest userRequest) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        this.id = GeneratorUtils.generateRandomId();
        this.firstName = userRequest.getFirstName();
        this.lastName = userRequest.getLastName();
        this.email = userRequest.getEmail();
        this.phone = userRequest.getPhone();
        this.password = encoder.encode(userRequest.getPassword());
        this.isVerified = userRequest.getIsVerified();
        this.logins = Collections.singletonList(Instant.now());
        this.updates = Collections.singletonList(Instant.now());
        this.failedAttempts = Constants.Auth.MAX_ATTEMPTS;
        this.lockedMaxAttempts = false;

        boolean isAdmin = userRequest.getIsAdmin();
        this.tokens = Collections.singletonList(JwtUtils.createToken(this.id, isAdmin ? "ROLE_ADMIN" : "ROLE_USER"));
        this.collectiveIds = userRequest.getCollectiveIds();
        this.roles = Collections.singleton(isAdmin ? "ADMIN" : "USER");
    }

    /**
     * Updates the current User object with data from a UserRequest object.
     * @param userRequest a
     */
    public void update(UserRequest userRequest) {
        this.firstName = userRequest.getFirstName();
        this.lastName = userRequest.getLastName();
        this.phone = userRequest.getPhone();
        this.email = userRequest.getEmail();
        this.isVerified = userRequest.getIsVerified();
        this.updates.add(Instant.now());
    }

    /**
     * Maps the User object to a UserRes object for response purposes.
     */
    public UserResponse toMap() {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(this.id);
        userResponse.setPhone(this.phone);
        userResponse.setEmail(this.email);
        userResponse.setLogins(this.logins);
        userResponse.setUpdates(this.updates);
        userResponse.setIsVerified(this.isVerified);
        userResponse.setFirstName(this.firstName);
        userResponse.setLastName(this.lastName);
        userResponse.setTitle(this.title);
        return userResponse;
    }

    public UserProfile toProfile() {
        UserProfile userProfile = new UserProfile();

        userProfile.setId(getId());
        userProfile.setFirstName(getFirstName());
        userProfile.setLastName(getLastName());
        userProfile.setTitle(getTitle());
        userProfile.setProfileUrl(String.format(Locale.getDefault(), "%s/media/get/%s", Constants.Server.URL, getProfileId()));

        return userProfile;
    }

    public boolean hasToken(String jwt) {
        return tokens.contains(jwt);
    }

    public void removeToken(String token) {
        tokens.remove(token);
    }
}
