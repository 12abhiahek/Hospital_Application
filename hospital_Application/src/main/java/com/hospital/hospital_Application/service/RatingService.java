package com.hospital.hospital_Application.service;


import com.hospital.hospital_Application.dto.RatingRequestDTO;
import com.hospital.hospital_Application.entity.Appointment;
import com.hospital.hospital_Application.entity.DoctorList;
import com.hospital.hospital_Application.entity.Rating;
import com.hospital.hospital_Application.entity.User;
import com.hospital.hospital_Application.repository.AppointmentRepository;
import com.hospital.hospital_Application.repository.RatingRepository;
import com.hospital.hospital_Application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

    public Rating submitRating(RatingRequestDTO request) {
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));


        DoctorList doctor = appointment.getDoctor();
        if (doctor == null) {
            throw new RuntimeException("Appointment has no doctor assigned");
        }

        User patient = appointment.getPatient();
        if (patient == null) {
            throw new RuntimeException("Appointment has no patient assigned");
        }

        Rating rating = new Rating();
        rating.setAppointment(appointment);
        rating.setDoctor(doctor);
        rating.setPatient(patient);
        rating.setRating(request.getRating());
        rating.setReview(request.getReview());

        return ratingRepository.save(rating);
    }

    public List<Rating> getRatingsByDoctor(Long doctorId) {
        return ratingRepository.findByDoctorId(doctorId);
    }

//    public List<RatingResponseDTO> getRatingsForDoctor(Long doctorId) {
//        return ratingRepository.findByDoctorId(doctorId)
//                .stream()
//                .map(r -> new RatingResponseDTO(
//                        r.getId(),
//                        r.getRating(),
//                        r.getReview(),
//                        r.getPatient().getName()
//                ))
//                .collect(Collectors.toList());
//    }
//
//    public Double getAverageRatingForDoctor(Long doctorId) {
//        return ratingRepository.findAverageRatingByDoctorId(doctorId).orElse(0.0);
//    }

}
