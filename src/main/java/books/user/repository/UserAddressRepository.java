package books.user.repository;

import books.user.domain.User;
import books.user.domain.UserAddress;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserAddressRepository extends CrudRepository<UserAddress, Long> {
    public Optional<Set<UserAddress>> findAllByUser(User user);

    public Optional<List<UserAddress>> findAllByUserOrderByCreateTimeDesc(User user, Pageable pageable);
}
