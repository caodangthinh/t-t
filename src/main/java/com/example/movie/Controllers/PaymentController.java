package com.example.movie.Controllers;

import com.example.movie.Entity.Reservation;
import com.example.movie.Entity.Seat;
import com.example.movie.Entity.Shows;
import com.example.movie.Entity.User;
import com.example.movie.Repository.ShowsRepo;
import com.example.movie.Repository.UserRepo;
import com.example.movie.Service.*;
import com.example.movie.config.PaypalPaymentIntent;
import com.example.movie.config.PaypalPaymentMethod;
import com.example.movie.utils.Utils;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@SpringBootApplication
@RequestMapping("/payment")

public class PaymentController {


    public static final String URL_PAYPAL_SUCCESS = "pay/success";
    public static final String URL_PAYPAL_CANCEL = "pay/cancel";
    private Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private PaypalService paypalService;

    @Autowired
    private SeatService seatService;

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ShowService showService;

    @Autowired
    private ShowsRepo showsRepo;
    @Autowired
    private UserRepo UserRepo;
    @Autowired
    private SendMailService sendMailService;
//    @Autowired
//    private UserSer UserRepo;

    @GetMapping
    public String index() {
        return "payment/index";
    }

    @PostMapping("/pay")
    public String pay(HttpServletRequest request,@RequestParam("showId") String showId, @RequestParam("price") String price,@RequestParam("seat") String seat) {
        String cancelUrl = Utils.getBaseURL(request) + "/" + "payment" + "/" + URL_PAYPAL_CANCEL;
        String successUrl = Utils.getBaseURL(request) + "/" + "payment" + "/" + URL_PAYPAL_SUCCESS;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        User user = UserRepo.findByUsername(name);

        Reservation reservation;
        String input = seat;
        String[] array = input.trim().split("\\s+");


        for(int i = 0; i < array.length; i++){
            reservation = new Reservation();
            reservation.setUser(user);
            Seat s = seatService.getSeatBySeatId(Long.parseLong(array[i]));
            reservation.setSeat(s);
            Shows sh = showService.getShowById(Long.parseLong(showId));
            reservation.setShows(sh);
            reservationService.addReservation(reservation);
        }
        double priceB = Double.parseDouble(price);

        //send mail

        Shows show = showService.getShowById(Long.parseLong(showId));
        String cinema = showsRepo.findCinemaNameByShowId(Long.parseLong(showId));
        String time = showsRepo.findTimeByShowId(Long.parseLong(showId));
        String day = showsRepo.findDayByShowId(Long.parseLong(showId));
        String movie = showsRepo.findMovieByShowId(Long.parseLong(showId));

        String quantity = String.valueOf(array.length);
        String total = String.valueOf(array.length * 9);
        sendMailService.sendEmail(user, cinema, time, day, seat, quantity, total, movie);

      //  reservation.

        try {
            Payment payment = paypalService.createPayment(
                    priceB,
                    "USD",
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    "payment description",
                    cancelUrl,
                    successUrl);
            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    return "redirect:" + links.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping(URL_PAYPAL_CANCEL)
    public String cancelPay() {
        return "payment/cancel";
    }

    @GetMapping(URL_PAYPAL_SUCCESS)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return "payment/success";
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }
}
