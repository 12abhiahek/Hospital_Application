package com.hospital.hospital_Application.service;

import com.hospital.hospital_Application.entity.OtpVerification;
import com.hospital.hospital_Application.entity.User;
import com.hospital.hospital_Application.repository.OtpVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class otpverService {
    @Autowired
    private OtpVerificationRepository otpRepo;

    @Autowired
    private EmailService emailService;

    public void generateAndSendOtp(User user) {
        String otp = String.format("%06d", new Random().nextInt(999999));

        OtpVerification entity = new OtpVerification();
        entity.setMobile(user.getPhone());
        entity.setEmail(user.getEmail());
        entity.setOtp(otp);
        entity.setUsed(false);
        entity.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        otpRepo.save(entity);

        emailService.sendOtp(user.getEmail(), otp);
    }

    public boolean verifyOtp(String mobile, String otp) {
        Optional<OtpVerification> recordOpt =
                otpRepo.findByMobileAndOtpAndUsedFalse(mobile, otp);

        if (recordOpt.isEmpty()) return false;

        OtpVerification record = recordOpt.get();

        // Expired OTP check
        if (record.getExpiryTime().isBefore(LocalDateTime.now())) {
            return false;
        }

        // Mark as used
        record.setUsed(true);
        otpRepo.save(record);

        return true;
    }
}
