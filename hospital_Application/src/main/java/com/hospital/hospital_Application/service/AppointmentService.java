package com.hospital.hospital_Application.service;


import com.hospital.hospital_Application.dto.AppointmentRequestDTO;
import com.hospital.hospital_Application.entity.Appointment;
import com.hospital.hospital_Application.entity.DoctorList;
import com.hospital.hospital_Application.entity.User;
import com.hospital.hospital_Application.exception.SlotAlreadyBookedException;
import com.hospital.hospital_Application.repository.AppointmentRepository;
import com.hospital.hospital_Application.repository.DoctorListRepository;
import com.hospital.hospital_Application.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private DoctorListRepository doctorListRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    private static final Logger log= LoggerFactory.getLogger(AppointmentService.class);

//    public String bookAppointment(Long doctorId, Long patientId, LocalDateTime appointmentTime) {
//        Optional<DoctorList> doctorOpt = doctorListRepository.findById(doctorId);
//        Optional<User> patientOpt = userRepository.findById(patientId);
//
//        if (doctorOpt.isEmpty() || patientOpt.isEmpty()) {
//            return "Doctor or Patient not found.";
//        }
//
//        // Check if slot already booked
//        boolean alreadyBooked = AppointmentRepository.existsByDoctorIdAndAppointmentTime(doctorId, appointmentTime);
//        if (alreadyBooked) {
//            return "This time slot is already booked.";
//        }
//
//        Appointment appointment = new Appointment();
//        appointment.setDoctor(doctorOpt.get());
//        appointment.setPatient(patientOpt.get());
//        appointment.setAppointmentTime(appointmentTime);
//        appointment.setDate(appointmentTime.toLocalDate());
//        appointment.setStatus("BOOKED");
//
//        appointmentRepo.save(appointment);
//        return "Appointment successfully booked!";
//    }

//    public Appointment bookAppointment(AppointmentRequestDTO request) {
//        DoctorList doctor = doctorListRepository.findById(request.getDoctorId())
//                .orElseThrow(() -> new RuntimeException("Doctor not found"));
//
//        // Save patient (if user login not implemented)
//        User patient = new User();
//        patient.setName(request.getName());
//        patient.setEmail(request.getEmail());
//        userRepository.save(patient);
//
//        // Parse date and time into LocalDateTime
//        LocalDate date = LocalDate.parse(request.getDate()); // "2025-08-04"
//
//        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
//        String timeString = request.getTime().trim(); // e.g., "09:00 AM"
//        LocalTime time = LocalTime.parse(timeString, timeFormatter);
//
//        LocalDateTime appointmentDateTime = LocalDateTime.of(date, time);
//
//        Appointment appointment = new Appointment();
//        appointment.setDate(date);
//        appointment.setAppointmentTime(appointmentDateTime);
//        appointment.setStatus("CONFIRMED");
//        appointment.setDoctor(doctor);
//        appointment.setPatient(patient);
//
//        return appointmentRepo.save(appointment);
//    }
//
//    public Appointment bookAppointment(AppointmentRequestDTO request) {
//        DoctorList doctor = doctorListRepository.findById(request.getDoctorId())
//                .orElseThrow(() -> new RuntimeException("Doctor not found"));
//
//        // Save patient (if user login not implemented)
//        User patient = new User();
//        patient.setName(request.getName());
//        patient.setEmail(request.getEmail());
//        userRepository.save(patient);
//
//        // Parse date
//        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate date = LocalDate.parse(request.getDate(), dateFormatter);
//
//        // Parse time safely
//        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
//        String timeString = request.getTime().trim(); // e.g., "09:00 AM"
//        LocalTime time;
//        try {
//            time = LocalTime.parse(timeString, timeFormatter);
//        } catch (Exception e) {
//            throw new RuntimeException("Invalid time format. Please use 'hh:mm AM/PM'");
//        }
//
//        LocalDateTime appointmentDateTime = LocalDateTime.of(date, time);
//
//        //  Check if slot already booked
//        boolean slotExists = appointmentRepo.existsByDoctorAndAppointmentTime(doctor, appointmentDateTime);
//        if (slotExists) {
////            throw new RuntimeException("This time slot is already booked for the selected doctor.");
//            throw new SlotAlreadyBookedException("This slot is already booked for the selected doctor.");
//
//        }
//
//        // Create new appointment
//        Appointment appointment = new Appointment();
//        appointment.setDate(date);
//        appointment.setAppointmentTime(appointmentDateTime);
//        appointment.setStatus("CONFIRMED");
//        appointment.setDoctor(doctor);
//        appointment.setPatient(patient);
//
//        Appointment savedAppointment = appointmentRepo.save(appointment);
//
//        //  Send confirmation email
//        emailService.sendAppointmentConfirmationEmail(
//                patient.getEmail(),
//                patient.getName(),
//                doctor.getName(),
//                date.format(dateFormatter),
//                time.format(timeFormatter)
//        );
//        return savedAppointment;
//
//    }


    public Appointment bookAppointment(AppointmentRequestDTO request) {
        // Check doctor
        DoctorList doctor = doctorListRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Check patient
        User patient = userRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Parse date
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(request.getDate(), dateFormatter);

        // Parse time (e.g. "09:00 AM")
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
        LocalTime time = LocalTime.parse(request.getTime().trim(), timeFormatter);

        // Build appointmentTime
        LocalDateTime appointmentDateTime = LocalDateTime.of(date, time);

        // Check duplicate slot
        boolean slotExists = appointmentRepo.existsByDoctorAndAppointmentTime(doctor, appointmentDateTime);
        if (slotExists) {
            log.error("Attempt to book an already occupied slot for selected doctor: {}", appointmentDateTime);
            throw new SlotAlreadyBookedException("This slot is already booked for the selected doctor.");

        }

        // Save appointment
        Appointment appointment = new Appointment();
        appointment.setId(appointment.getId());
        appointment.setDate(date);
        appointment.setAppointmentTime(appointmentDateTime);
        appointment.setStatus("CONFIRMED");
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setReason(request.getReason());

        return appointmentRepo.save(appointment);
    }


    public Appointment getAppointmentById(Long id) {
        return appointmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    public List<Appointment> getAppointmentsForDoctor(Long doctorId) {
        return appointmentRepo.findByDoctorId(doctorId);
    }

    public List<Appointment> getAppointmentsForPatient(Long patientId) {
        return appointmentRepo.findByPatientId(patientId);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepo.findAll();
    }

}
