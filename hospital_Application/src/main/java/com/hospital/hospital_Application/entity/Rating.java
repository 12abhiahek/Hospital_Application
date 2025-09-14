package com.hospital.hospital_Application.entity;

import jakarta.persistence.*;

@Entity
public class Rating {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private Integer rating;   // e.g., 1–5
        private String review;

        @ManyToOne
        @JoinColumn(name = "appointment_id", nullable = false)
        private Appointment appointment;

        @ManyToOne
        @JoinColumn(name = "doctor_id", nullable = false)
        private DoctorList doctor;

        @ManyToOne
        @JoinColumn(name = "patient_id", nullable = false)
        private User patient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }


    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public DoctorList getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorList doctor) {
        this.doctor = doctor;
    }

}
