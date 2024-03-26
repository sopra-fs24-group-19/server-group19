package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("ratingRepository")
public interface RatingRepository extends JpaRepository<Rating, Long> {
  Rating findByName(String name);

  Rating findByUsername(String username);
}
