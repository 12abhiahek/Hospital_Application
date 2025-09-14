package com.hospital.hospital_Application.repository;

import com.hospital.hospital_Application.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerification,Long> {

    Optional<OtpVerification> findByMobileAndOtpAndUsedFalse(String mobile, String otp);
}
