package com.example.movie.Service;

import com.example.movie.Entity.Rating;
import com.example.movie.Repository.RatingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RatingService {

    @Autowired
    private RatingRepo ratingRepo;

    public List<Rating> getAllRatings() {
        return ratingRepo.findAll();
    }

    public double calculateAverageRating(List<Rating> ratings) {
        if (ratings.isEmpty()) {
            return 0.0; // Tránh chia cho 0 nếu không có đánh giá nào.
        }

        int totalRating = ratings.stream().mapToInt(Rating::getValue).sum();
        return (double) totalRating / ratings.size();
    }
    public int getTotalReviewCount(List<Rating> ratings) {

        return ratings.size();
    }


}
