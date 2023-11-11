package com.example.movie.Controllers;

import com.example.movie.Entity.Comment;
import com.example.movie.Entity.Movie;
import com.example.movie.Entity.Rating;
import com.example.movie.Entity.User;
import com.example.movie.Repository.MovieRepo;
import com.example.movie.Repository.RatingRepo;
import com.example.movie.Repository.UserRepo;
import com.example.movie.Service.MovieService;
import com.example.movie.Service.RatingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller
public class RatingController {
    @Autowired
    private RatingRepo ratingRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepo movieRepo;

    @Autowired
    private RatingService ratingService;

    @GetMapping("/ratings")
    public String getAllRatings(Model model) {
        List<Rating> ratings = ratingRepo.findAll();
        double averageRating = ratingService.calculateAverageRating(ratings);
        int totalReviewCount = ratingService.getTotalReviewCount(ratings);
        model.addAttribute("ratings", ratings);
        model.addAttribute("totalReviewCount", totalReviewCount);
        model.addAttribute("averageRating", averageRating);

        return "/movies/ratings";
    }

    @PostMapping("/ratings")
    public String addRating(HttpServletRequest request, @RequestParam("value") int value) {
        String previousPageUrl = request.getHeader("Referer");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findByUsername(authentication.getName());
        System.out.println("Received rating: " + value);
        try {
            URI uri = new URI(previousPageUrl);
            String path = uri.getPath();
            String[] pathSegments = path.split("/");
            String movieIdStr = pathSegments[pathSegments.length - 1];
            long movieId = Long.parseLong(movieIdStr);

            Rating existingRating = ratingRepo.findByUserAndMovieId(user, movieId);
            if (existingRating == null) {
                Movie movie = movieService.getMovieById(movieId);
                Rating rating = new Rating();
                rating.setUser(user);
                rating.setValue(value);
                rating.setMovie(movie);
                ratingRepo.save(rating);
                System.out.println("Rating saved: " + rating);
                return "redirect:" + previousPageUrl;
            } else {
                // Người dùng đã đánh giá bộ phim này trước đó, có thể xử lý thông báo hoặc thực hiện hành động khác tùy ý.
                // Ví dụ: return "redirect:/ratings?error=You have already rated this movie.";
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return "redirect:" + previousPageUrl;
    }

}
