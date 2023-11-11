package com.example.movie.Service;

import com.example.movie.Entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import javax.mail.internet.InternetAddress;
import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class SendMailService {
    public void sendEmail(User user, String cinema, String time, String day, String seat, String quantity, String total, String movie) {
        // Cấu hình thông tin email
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        // Thông tin đăng nhập email
        String senderEmail = "sendcodetw@gmail.com";
        String senderPassword = "cfhzuhybjjrdbwzq";

        // Tạo một phiên làm việc (session)
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Tạo đối tượng MimeMessage
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject("New Order");
            String htmlContent = "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<title>Thông tin đơn hàng mới từ khách hàng: " + user.getFullName() +"</title>"
                    + "<meta charset=\"utf-8\" />"
                    + "</head>"
                    + "<body>"
                    + "New order information from customers: " + user.getFullName() +"<br />"
                    + "Email: " + user.getEmail() + "<br />"
                    + "MovieName: "  + movie + "<br />"
                    + "Cinema: "  + cinema + "<br />"
                    + "Day: "  + day + ", Time: " + time + "<br />"
                    + "Quantity: "  + quantity + "  Seats: "  + seat + "<br />"
                    + "Total: "  + total +  "<br />"
                    + "<i style=\"font-weight: bold\">\"I hope you have a great time at the movie theater!\"</i>"
                    + "</body>"
                    + "</html>";
            message.setContent(htmlContent, "text/html");
            // Gửi email
            Transport.send(message);

            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void resetPassword(User user, String newPassword){
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        // Thông tin đăng nhập email
        String senderEmail = "sendcodetw@gmail.com";
        String senderPassword = "cfhzuhybjjrdbwzq";

        // Tạo một phiên làm việc (session)
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Tạo đối tượng MimeMessage
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject("New Order");
            String htmlContent = "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<title>Reset password</title>"
                    + "<meta charset=\"utf-8\" />"
                    + "</head>"
                    + "<body>"
                    + "Customers: " + user.getFullName() + "<br />"
                    + "Name: " + user.getUsername() + "<br />"
                    + "New password: " + newPassword + "<br />"
                    + "<i style=\"font-weight: bold\">\"Don't share your account with anyone!\"</i>"
                    + "</body>"
                    + "</html>";
            message.setContent(htmlContent, "text/html");
            // Gửi email
            Transport.send(message);

            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

