package com.example.movie.Repository;

import com.example.movie.Entity.Comment;
import com.example.movie.Entity.Seat;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepo extends JpaRepository<Seat, Long> {
    default List<Seat> getAllSeat(Integer pageNo,
                                         Integer pageSize,
                                         String sortBy){
        return findAll(PageRequest.of(pageNo,
                pageSize,
                Sort.by(sortBy)))
                .getContent();
    }
}
