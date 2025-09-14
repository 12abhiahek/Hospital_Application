package com.hospital.hospital_Application.controller;

import com.hospital.hospital_Application.entity.Rating;
import com.hospital.hospital_Application.dto.RatingRequestDTO;
import com.hospital.hospital_Application.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ratings")
@CrossOrigin(origins = "*")
public class RatingController {
    @Autowired
    private RatingService ratingService;

    @PostMapping("/submit")
    public ResponseEntity<?> submitRating(@RequestBody RatingRequestDTO request) {
        Rating rating = ratingService.submitRating(request);
        return ResponseEntity.ok(rating);
    }


    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Rating>> getRatingsForDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(ratingService.getRatingsByDoctor(doctorId));
    }
}
