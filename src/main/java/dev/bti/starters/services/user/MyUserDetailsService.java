package dev.bti.starters.services.user;


import dev.bti.starters.exceptions.auth.AuthException;
import dev.bti.starters.models.main.User;
import dev.bti.starters.models.req.Credential;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;

        try {
            user = userService.getUser(Credential.of(username), User.class);
        } catch (AuthException e) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        if (user.getId().equals(username)) {
            return org.springframework.security.core.userdetails.User.withUsername(username)
                    .password(user.getPassword())
                    .roles(user.getRoles().toArray(new String[0]))
                    .build();
        }
        throw new UsernameNotFoundException("User not found: " + username);
    }
}
