package books.user.repository;

import books.user.domain.User;
import books.user.domain.UserPointHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserPointHistoryRepository extends CrudRepository<UserPointHistory, Long> {
    public Optional<Set<UserPointHistory>> findAllByUser(User user);
    public Optional<List<UserPointHistory>> findAllByUserOrderByCreateTimeDesc(User user);
    public Optional<List<UserPointHistory>> findAllByUserOrderByCreateTimeDesc(User user, Pageable pageable);
}
