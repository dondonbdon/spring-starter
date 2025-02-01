package dev.bti.starters.user;

import dev.bti.starters.models.main.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    @Query("{'lockedMaxAttempts': true, 'lockedFailedAttemptsTimestampEnd': {$lte: ?0}}")
    List<User> findUsersToUnlock(Instant currentTime);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
}
