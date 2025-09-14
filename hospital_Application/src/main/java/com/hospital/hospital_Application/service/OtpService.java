package com.hospital.hospital_Application.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();
    private final Random random = new SecureRandom();

    private final Map<String, OtpEntry> mobileOtpStorage = new ConcurrentHashMap<>();
    private static final long OTP_VALIDITY_DURATION = 2 * 60 * 1000;


    public String generateOtp(String email) {
        String otp = String.valueOf(100000 + random.nextInt(900000));
        otpStorage.put(email, otp);
        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        return otp.equals(otpStorage.get(email));
    }

    public void clearOtp(String email) {
        otpStorage.remove(email);
    }

    public String generateMobileOtp(String mobile) {
        String otp = String.valueOf(100000 + random.nextInt(900000));
        mobileOtpStorage.put(mobile, new OtpEntry(otp, System.currentTimeMillis()));
        return otp;
    }

    public boolean verifyMobileOtp(String mobile, String otp) {
        OtpEntry entry = mobileOtpStorage.get(mobile);

        if (entry == null) {
            return false; // no OTP generated
        }

        // Check expiry
        long now = System.currentTimeMillis();
        if (now - entry.timestamp > OTP_VALIDITY_DURATION) {
            mobileOtpStorage.remove(mobile);
            return false;
        }

        // Check OTP match
        if (entry.otp.equals(otp)) {
            mobileOtpStorage.remove(mobile);
            return true;
        }

        return false;
    }

    public void clearMobileOtp(String mobile) {
        mobileOtpStorage.remove(mobile);
    }

    // Helper class for storing OTP + timestamp
    private static class OtpEntry {
        String otp;
        long timestamp;

        OtpEntry(String otp, long timestamp) {
            this.otp = otp;
            this.timestamp = timestamp;
        }
    }

}
