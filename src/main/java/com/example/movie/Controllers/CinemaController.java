package com.example.movie.Controllers;

import com.example.movie.Service.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cinema")
public class CinemaController {
    @Autowired
    private CinemaService cinemaService;

    @GetMapping
    public String cinema(Model model){
        model.addAttribute("cinemas", cinemaService.getAllCinema());
        return "cinema/index";
    }
}
