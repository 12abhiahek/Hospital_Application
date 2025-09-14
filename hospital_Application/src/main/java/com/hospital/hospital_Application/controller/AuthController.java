package com.hospital.hospital_Application.controller;


import com.hospital.hospital_Application.entity.User;
import com.hospital.hospital_Application.repository.UserRepository;
import com.hospital.hospital_Application.service.AuthService;
import com.hospital.hospital_Application.service.EmailService;
import com.hospital.hospital_Application.service.OtpService;
import com.hospital.hospital_Application.service.SmsService;
import com.hospital.hospital_Application.utility.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private OtpService otpService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private SmsService smsService;

//    @Autowired
//    private SmsService smsService;

//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody User user) {
//        String result = authService.registerUser(user);
//        return ResponseEntity.ok(result);
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody Map<String, String> credentials) {
//        String email = credentials.get("email");
//        String password = credentials.get("password");
//
//        Optional<User> user = authService.authenticate(email, password);
//        return user.isPresent()
//                ? ResponseEntity.ok("Login successful for " + user.get().getName())
//                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
//    }
//    @PostMapping("/send-otp")
//    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
//        String email = request.get("email");
//        Optional<User> userOpt = userRepository.findByEmail(email);
//        if (userOpt.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//        }
//
//        String otp = otpService.generateOtp(email);
//        emailService.sendOtpEmail(email, otp);
//        return ResponseEntity.ok("OTP sent to email");
//    }
//
//    @PostMapping("/verify-otp")
//    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
//        String email = request.get("email");
//        String otp = request.get("otp");
//
//        if (otpService.verifyOtp(email, otp)) {
//            return ResponseEntity.ok("OTP verified");
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP");
//        }
//    }
//
//    @PostMapping("/reset-password")
//    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
//        String email = request.get("email");
//        String newPassword = request.get("newPassword");
//
//        Optional<User> userOpt = userRepository.findByEmail(email);
//        if (userOpt.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//        }
//
//        User user = userOpt.get();
//        user.setPassword(passwordEncoder.encode(newPassword));
//        userRepository.save(user);
//
//        otpService.clearOtp(email);
//
//        return ResponseEntity.ok("Password reset successful");
//    }



    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        // Encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Ensure role is set, default to PATIENT
        if (user.getRole() == null) {
            user.setRole(com.hospital.hospital_Application.entity.Role.PATIENT);
        }

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully with role: " + user.getRole());
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        Optional<User> userOpt = authService.authenticate(email, password);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "role", user.getRole().name(),
                    "name", user.getName(),
                    "email",user.getEmail(),
                    "id",user.getId()
            ));

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    // SEND OTP
    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        String otp = otpService.generateOtp(email);
        emailService.sendOtpEmail(email, otp);
        return ResponseEntity.ok("OTP sent to email");
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        if (otpService.verifyOtp(email, otp)) {
            return ResponseEntity.ok("OTP verified");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        otpService.clearOtp(email);

        return ResponseEntity.ok("Password reset successful");
    }


    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestParam String mobile, @RequestParam String otp) {
        boolean isValid = otpService.verifyMobileOtp(mobile, otp);

        if (isValid) {
            return ResponseEntity.ok("OTP verified successfully!");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired OTP");
        }
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generateOtp(@RequestParam String mobile) {
        if (!mobile.matches("^[0-9]{10}$")) {
            return ResponseEntity.badRequest().body("Invalid mobile number. Must be 10 digits.");
        }

        String otp = otpService.generateMobileOtp(mobile);

        smsService.sendOtp(mobile, otp); // real SMS sent

        return ResponseEntity.ok("OTP sent successfully to your mobile!");
    }

}
