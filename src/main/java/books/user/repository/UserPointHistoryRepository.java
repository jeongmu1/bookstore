package books.user.repository;

import books.user.domain.User;
import books.user.domain.UserPointHistory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserPointHistoryRepository extends CrudRepository<UserPointHistory, Long> {
    List<UserPointHistory> findAllByUser(User user);

    void deleteByUser(User user);
}
