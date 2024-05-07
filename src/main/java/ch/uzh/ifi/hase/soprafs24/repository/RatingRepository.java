package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Rating;
import ch.uzh.ifi.hase.soprafs24.entity.Task;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("ratingRepository")
public interface RatingRepository extends JpaRepository<Rating, Long> {
    
    Rating findRatingById(Long id);

    @Query("SELECT r FROM Rating r WHERE r.reviewed.id = :reviewedId")
    List<Rating> findRatingsByReviewedId(long reviewedId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Rating r WHERE r.id = :reviewId")
    void deleteRatingById(@Param("reviewId") long reviewId);

    boolean existsByTaskAndReviewerId(Task task, Long reviewerId);
}
