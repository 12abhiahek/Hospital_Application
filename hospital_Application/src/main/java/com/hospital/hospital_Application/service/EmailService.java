package com.hospital.hospital_Application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {


        @Autowired
        private JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(String to, String otp) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Your OTP for Password Reset");
            message.setText("Your OTP is: " + otp);
            mailSender.send(message);
        }

    // === New Appointment Confirmation Email Logic ===
    public void sendAppointmentConfirmationEmail(
            String to,
            String patientName,
            String doctorName,
            String date,
            String time
    ) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Appointment Confirmation");
        message.setText(
                "Hello " + patientName + ",\n\n" +
                        "Your appointment with " + doctorName + " is confirmed.\n" +
                        "📅 Date: " + date + "\n" +
                        "⏰ Time: " + time + "\n\n" +
                        "Thank you for choosing our hospital.\n" +
                        "If you have any questions, please contact us."
        );
        mailSender.send(message);
    }


}
