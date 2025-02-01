package dev.bti.starters.services.user;

import dev.bti.starters.common.Constants;
import dev.bti.starters.common.Pair;
import dev.bti.starters.exceptions.auth.AuthException;
import dev.bti.starters.models.main.User;
import dev.bti.starters.models.req.Credential;
import dev.bti.starters.models.req.UserRequest;
import dev.bti.starters.models.res.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserCacheService userCacheService;

    @FunctionalInterface
    private interface UserFinder<T> {
        Optional<T> findUser(String identifier);
    }

    private <T> T findUserByIdentifier(String identifier, UserFinder<User> userFinder, Class<T> resultClass) throws AuthException {
        User user = userFinder.findUser(identifier).orElseThrow(() -> Constants.Throws.UserNotFound);
        return mapUserToResult(user, resultClass);
    }

    private <T> T mapUserToResult(User user, Class<T> resultClass) {
        if (resultClass == User.class) {
            return resultClass.cast(user);
        } else if (resultClass == UserProfile.class) {
            return resultClass.cast(user.toProfile());
        } else {
            return resultClass.cast(user.toMap());
        }
    }

    private <T> T getUserById(String id, Class<T> resultClass) throws AuthException {
        return findUserByIdentifier(id, userRepository::findById, resultClass);
    }

    private <T> T getUserByEmail(String email, Class<T> resultClass) throws AuthException {
        return findUserByIdentifier(email, userRepository::findByEmail, resultClass);
    }

    private <T> T getUserByPhone(String phone, Class<T> resultClass) throws AuthException {
        return findUserByIdentifier(phone, userRepository::findByPhone, resultClass);
    }

    // --- Internally Used ---

    public Pair<String, String> registerUser(UserRequest userRequest) throws AuthException {
        if (userCacheService.credentialExistsInCache(Credential.of(userRequest.getPhone()))) {
            throw Constants.Throws.PhoneNumberAlreadyExists;
        }

        User user = new User(userRequest);
        userRepository.save(user);

        userCacheService.cacheUserProfile(user.getId(), getUserProfile(user.getId()));
        userCacheService.addCredentialToCache(Credential.of(user.getEmail()));
        userCacheService.addCredentialToCache(Credential.of(user.getPhone()));

        return Pair.of(user.getTokens().getFirst(), user.getId());
    }

    public <T> T getUser(Credential credential, Class<T> resultClass) throws AuthException {
        return switch (credential.getCredentialType()) {
            case EMAIL -> getUserByEmail(credential.getCredential(), resultClass);
            case PHONE -> getUserByPhone(credential.getCredential(), resultClass);
            default -> getUserById(credential.getCredential(), resultClass);
        };
    }

    public boolean isTokenValid(String id, String jwt) throws AuthException {
        User user = getUserById(id, User.class);
        return user.hasToken(jwt);
    }

    // --- Externally Used ---

    public UserProfile getUserProfile(String id) throws AuthException {
        UserProfile profile = userCacheService.getCachedUserProfile(id);
        return profile != null ? profile : getUserById(id, User.class).toProfile();
    }

    public void updateUser(String id, UserRequest userRequest) throws AuthException {
        User user = userRepository.findById(id).orElseThrow(() -> Constants.Throws.UserNotFound);
        user.update(userRequest);
        userRepository.save(user);
    }

    public void invalidateUserToken(String id, String token) throws AuthException {
        User user = getUserById(id, User.class);
        user.removeToken(token);
        userRepository.save(user);
    }

    public boolean userExists(Credential credential) throws AuthException {
        return userCacheService.credentialExistsInCache(credential);
    }
}
