package dev.bti.starters.services.user;

import dev.bti.starters.models.req.Credential;
import dev.bti.starters.models.res.UserProfile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserCacheService {

    private final RedisTemplate<String, UserProfile> redisTemplate;
    private final RedisTemplate<String, Credential> credentialsTemplate;

    public UserCacheService(RedisTemplate<String, UserProfile> redisTemplate, RedisTemplate<String, Credential> credentialsTemplate) {
        this.redisTemplate = redisTemplate;
        this.credentialsTemplate = credentialsTemplate;
    }

    public void cacheUserProfile(String userId, UserProfile userProfile) {
        redisTemplate.opsForValue().set("profile:" + userId, userProfile);
    }

    public UserProfile getCachedUserProfile(String userId) {
        return redisTemplate.opsForValue().get("profile:" + userId);
    }

    public void removeCachedUserProfile(String userId) {
        redisTemplate.delete("profile:" + userId);
    }

    public void updateUserProfile(String userId, UserProfile updatedUserProfile) {
        redisTemplate.opsForValue().set("profile:" + userId, updatedUserProfile);
    }

    public void addCredentialToCache(Credential credential) {
        credentialsTemplate.opsForSet().add("userCredentials", credential);

    }

    public boolean credentialExistsInCache(Credential credential) {
        return Boolean.TRUE.equals(credentialsTemplate.opsForSet().isMember("userCredentials", credential));
    }
}
