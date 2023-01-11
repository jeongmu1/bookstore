package books.user.service;

import books.order.domain.ProductOrder;
import books.order.repository.CartRepository;
import books.order.repository.OrderRepository;
import books.user.common.RegistrationForm;
import books.user.domain.Authority;
import books.user.domain.User;
import books.user.domain.UserAddress;
import books.user.repository.UserAddressRepository;
import books.user.repository.UserAuthorityRepository;
import books.user.repository.UserCCRepository;
import books.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final UserAuthorityRepository authorityRepo;
    private final UserAddressRepository userAddressRepo;
    private final UserCCRepository userCCRepo;
    private final CartRepository cartRepo;
    private final OrderRepository orderRepo;
    private final PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepo
            , UserAuthorityRepository authorityRepo
            , UserAddressRepository userAddressRepo
            , UserCCRepository userCCRepo
            , CartRepository cartRepo
            , OrderRepository orderRepo
            , PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.authorityRepo = authorityRepo;
        this.userAddressRepo = userAddressRepo;
        this.userCCRepo = userCCRepo;
        this.cartRepo = cartRepo;
        this.orderRepo = orderRepo;
        this.encoder = encoder;
    }

    public User getUserInfo(User user) {
        return null;
    }

    @Override
    public Set<UserAddress> getUserAddresses(User user) {
        return userAddressRepo.findUserAddressesByUser(user).orElse(null);
    }

    @Override
    public void processRegistration(RegistrationForm form) {
        userRepo.save(form.toUser(encoder));
        userRepo.findByUsername(form.getUsername()).ifPresent(this::addUserAuthority);
    }

    private void addUserAuthority(User user) {
        Authority auth = new Authority();
        auth.setUser(user);
        auth.setAuthority("ROLE_USER");
        authorityRepo.save(auth);
    }

}