package com.hospital.hospital_Application.repository;

import com.hospital.hospital_Application.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Long> {
    List<Rating> findByDoctorId(Long doctorId);

}
