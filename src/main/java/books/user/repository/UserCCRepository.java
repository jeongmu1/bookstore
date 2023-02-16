package books.user.repository;

import books.user.domain.User;
import books.user.domain.UserCreditCard;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface UserCCRepository extends CrudRepository<UserCreditCard, Long> {
    Optional<Set<UserCreditCard>> findAllByUser(User user);

    void deleteByUser(User user);
}
