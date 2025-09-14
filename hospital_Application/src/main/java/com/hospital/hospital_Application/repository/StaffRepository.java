package com.hospital.hospital_Application.repository;

import com.hospital.hospital_Application.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff,Long> {
    Optional<Staff> findByUserId(Long userId);

}
