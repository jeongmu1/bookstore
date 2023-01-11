package books.user.repository;

import books.user.domain.User;
import books.user.domain.UserAddress;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.Set;

public interface UserAddressRepository extends CrudRepository<UserAddress, Long> {
    public Optional<Set<UserAddress>> findUserAddressesByUser(User user);
    public Optional<UserAddress> findUserAddressByUserAndDefaultFlag(User user, boolean defaultFlag);
}
