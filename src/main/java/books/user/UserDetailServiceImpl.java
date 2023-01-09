package books.user;

import books.user.domain.Authority;
import books.user.domain.User;
import books.user.domain.UserDetailsImpl;
import books.user.repository.UserAuthorityRepository;
import books.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserAuthorityRepository userAuthorityRepo;

    @Autowired
    public UserDetailServiceImpl(UserRepository userRepository, UserAuthorityRepository userAuthorityRepo) {
        this.userRepository = userRepository;
        this.userAuthorityRepo = userAuthorityRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<List<Authority>> optionalAuthorities = userAuthorityRepo.findByUser(user);
            return optionalAuthorities.map(authorities -> new UserDetailsImpl(user, authorities)).orElse(null);

        }

        return null;
    }
}
