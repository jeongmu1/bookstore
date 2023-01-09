package books.user.repository;

import books.user.domain.Authority;
import books.user.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserAuthorityRepository extends CrudRepository<Authority, Long> {
    Optional<List<Authority>> findByUser(User user);
}
