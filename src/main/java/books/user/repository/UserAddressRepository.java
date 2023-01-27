package books.user.repository;

import books.user.domain.User;
import books.user.domain.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    Optional<UserAddress> findUserAddressByUserAndDefaultFlag(User user, boolean isDefault);
}
