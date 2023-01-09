package books.user.repository;

import books.user.domain.Authority;
import books.user.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserAuthorityRepository extends CrudRepository<Authority, Long> {
    Optional<Authority> findByUser(User user);
}
