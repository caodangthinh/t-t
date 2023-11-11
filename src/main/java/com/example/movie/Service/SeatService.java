package com.example.movie.Service;

import com.example.movie.Entity.Comment;
import com.example.movie.Entity.Seat;
import com.example.movie.Entity.Shows;
import com.example.movie.Repository.SeatRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {
    @Autowired
    private SeatRepo seatRepo;
    public Seat getSeatBySeatId (Long id) {
        return seatRepo.findById(id).orElse(null);
    }
    public List<Seat> getAllSeat() {
        return seatRepo.findAll();
    }
    public List<Seat> getAllSeat(Integer pageNo,
                                        Integer pageSize,
                                        String sortBy){
        return seatRepo.getAllSeat(pageNo, pageSize, sortBy);
    }
    public Seat getSeatById(Long id) {
        return seatRepo.findById(id).orElse(null);
    }
    public Seat addSeat(Seat seat){
        return seatRepo.save(seat);
    }
    public void deleteSeat(Long id) {
        //goi repository ==> deleteById
        seatRepo.deleteById(id);
    }
}
