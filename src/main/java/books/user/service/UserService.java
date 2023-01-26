package books.user.service;

import books.user.common.PointHistoryDto;
import books.user.common.RegistrationForm;
import books.user.common.UserDto;
import books.user.domain.*;

import java.security.Principal;
import java.util.List;

public interface UserService {
    UserDto findUserInfo(Principal principal);

    void processRegistration(RegistrationForm form);

    User loadUserByUsername(String username);

    List<PointHistoryDto> findUserPointHistory(Principal principal);
}