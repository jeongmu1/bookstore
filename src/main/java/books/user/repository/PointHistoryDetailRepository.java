package books.user.repository;

import books.user.domain.PointHistoryDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryDetailRepository extends JpaRepository<PointHistoryDetail, Integer> {
    PointHistoryDetail findPointHistoryDetailById(Integer id);
}
