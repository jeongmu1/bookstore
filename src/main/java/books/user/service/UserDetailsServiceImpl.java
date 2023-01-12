package books.user.service;

import books.user.common.UserDetailsImpl;
import books.user.repository.UserAuthorityRepository;
import books.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserAuthorityRepository userAuthorityRepo;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, UserAuthorityRepository userAuthorityRepo) {
        this.userRepository = userRepository;
        this.userAuthorityRepo = userAuthorityRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .flatMap(user -> userAuthorityRepo
                        .findByUser(user)
                        .map(authorities -> new UserDetailsImpl(user, authorities)))
                .orElse(null);

    }
}
