package books.user.service;

import books.user.common.RegistrationForm;
import books.user.domain.User;
import books.user.domain.UserAddress;

import java.util.Set;

public interface UserService {

    public User getUserInfo(User user);
    public Set<UserAddress> getUserAddresses(User user);

    public void processRegistration(RegistrationForm form);
}