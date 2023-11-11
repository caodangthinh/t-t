package com.example.movie.Repository;

import com.example.movie.Entity.Comment;
import com.example.movie.Entity.Rating;
import com.example.movie.Entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingRepo extends JpaRepository<Rating, Long> {

    @Query(value = "SELECT * from rating where movie_id = ?1", nativeQuery = true)
    List<Rating> getRatingByMovieId(Long movie_id);

    default List<Rating> getRatingByMovieId(Integer pageNo,
                                            Integer pageSize,
                                            String sortBy){
        return findAll(PageRequest.of(pageNo,
                pageSize,
                Sort.by(sortBy)))
                .getContent();
    }
    @Query("""
            SELECT c
            FROM Rating c
            WHERE c.value LIKE %?1%
                OR c.user.username LIKE %?1%
                OR c.movie.movieName LIKE %?1%
            """)
    List<Rating> searchRating(String keyword);

    @Query("SELECT r FROM Rating r WHERE r.user = :user AND r.movie.id = :movieId")
    Rating findByUserAndMovieId(@Param("user") User user, @Param("movieId") Long movieId);
}
