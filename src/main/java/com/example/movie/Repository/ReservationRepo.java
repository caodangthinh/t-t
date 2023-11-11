package com.example.movie.Repository;


import com.example.movie.Entity.Movie;
import com.example.movie.Entity.Reservation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Long>{
    @Query(value = "SELECT seat_id FROM `reservation` WHERE show_id = ?1", nativeQuery = true)
    List<Integer> findSeatByShowId(Long show_id);
    @Query(value = "SELECT c.cinema_name from shows s, cinema c where c.cinema_id = s.cinema_id and show_id = ?1", nativeQuery = true)
    String[] getCinemaByShowId(Long show_id);

    @Query(value = "SELECT c.time from shows s, show_time c where c.show_time_id = s.show_time_id and show_id = ?1", nativeQuery = true)
    String[] getTimeByShowId(Long show_id);

    @Query(value = "SELECT c.day from shows s, show_day c where c.show_day_id = s.show_day_id and show_id = ?1", nativeQuery = true)
    String[] getDayByShowId(Long show_id);

    default List<Reservation> getAllReservations(Integer pageNo,
                                                 Integer pageSize,
                                                 String sortBy){
        return findAll(PageRequest.of(pageNo,
                pageSize,
                Sort.by(sortBy)))
                .getContent();
    }

    @Query("""
            SELECT r
            FROM Reservation r
            WHERE r.shows.movie.movieName LIKE %?1%
                OR r.user.username LIKE %?1%
                OR r.shows.cinema.cinemaName LIKE %?1%
            """)
    List<Reservation> searchReservation(String keyword);

    @Query(value = "select sum(c.price) from reservation a, shows b, seat c where a.show_id = b.show_id and a.seat_id = c.seat_id", nativeQuery = true)
    String revenue();
}
