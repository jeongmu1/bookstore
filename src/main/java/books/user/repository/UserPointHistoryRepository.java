package books.user.repository;

import books.user.domain.User;
import books.user.domain.UserPointHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserPointHistoryRepository extends CrudRepository<UserPointHistory, Long> {
    List<UserPointHistory> findAllByUser(User user);
    Optional<List<UserPointHistory>> findAllByUserOrderByCreateTimeDesc(User user);
    Optional<List<UserPointHistory>> findAllByUserOrderByCreateTimeDesc(User user, Pageable pageable);
}
