package com.campus.helpdesk.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public void sendActivationEmail(String toEmail, String name, String tempPassword) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setFrom(senderEmail, "SLIIT Helpdesk System");
            helper.setTo(toEmail);
            helper.setSubject("SLIIT Helpdesk - Account Activation");

            String plainText = "Hello " + name + ",\n\n" +
                    "Your account has been created successfully.\n" +
                    "Please log in using the credentials below and CHANGE your password immediately.\n\n" +
                    "Email: " + toEmail + "\n" +
                    "Temporary Password: " + tempPassword + "\n\n" +
                    "Login URL: https://sliit-helpdesk.ddnsfree.com/\n\n" +
                    "Thank you,\nSLIIT Helpdesk System";

            helper.setText(plainText);

            mailSender.send(message);

        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void sendNewTicketNotificationToAdmin(int ticketId, String title, String studentId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(senderEmail, "SLIIT Helpdesk Alert");
            helper.setTo("youremail");
            helper.setSubject("New Support Ticket Alert - #" + ticketId);

            String plainText = "New Ticket Alert: #" + ticketId + " from Student: " + studentId;

            String htmlMsg = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; border: 1px solid #e0e0e0; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 10px rgba(0,0,0,0.05);'>" +
                    "<div style='background-color: #dc3545; color: white; padding: 25px; text-align: center;'>" +
                    "<h2 style='margin: 0; font-size: 24px;'>New Support Ticket Alert</h2>" +
                    "</div>" +
                    "<div style='padding: 30px; background-color: #ffffff;'>" +
                    "<h3 style='color: #333; margin-top: 0;'>Hello Admin,</h3>" +
                    "<p style='color: #555; font-size: 16px; line-height: 1.5;'>A new support ticket has been submitted by a student and requires your attention.</p>" +

                    "<table style='width: 100%; margin: 25px 0; border-collapse: collapse; background-color: #f8f9fa; border-radius: 8px; overflow: hidden;'>" +
                    "<tr><td style='padding: 12px 15px; border-bottom: 1px solid #dee2e6; color: #555; width: 30%;'><strong>Ticket ID:</strong></td><td style='padding: 12px 15px; border-bottom: 1px solid #dee2e6; color: #dc3545; font-weight: bold;'>#" + ticketId + "</td></tr>" +
                    "<tr><td style='padding: 12px 15px; border-bottom: 1px solid #dee2e6; color: #555;'><strong>Student ID:</strong></td><td style='padding: 12px 15px; border-bottom: 1px solid #dee2e6; color: #333;'> " + studentId + "</td></tr>" +
                    "<tr><td style='padding: 12px 15px; color: #555;'><strong>Issue Title:</strong></td><td style='padding: 12px 15px; color: #333;'>" + title + "</td></tr>" +
                    "</table>" +

                    "<div style='text-align: center; margin-top: 35px;'>" +
                    "<a href='https://sliit-helpdesk.ddnsfree.com/' style='background-color: #198754; color: white; padding: 12px 30px; text-decoration: none; border-radius: 50px; font-weight: bold; font-size: 16px; display: inline-block;'>Review Ticket</a>" +
                    "</div>" +
                    "</div>" +
                    "<div style='background-color: #f1f1f1; color: #888; padding: 20px; text-align: center; font-size: 12px;'>" +
                    "<p style='margin: 0;'>System Generated Notification</p>" +
                    "</div>" +
                    "</div>";

            helper.setText(plainText, htmlMsg);
            mailSender.send(message);

        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
