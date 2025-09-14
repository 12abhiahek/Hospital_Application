package com.hospital.hospital_Application.controller;


import com.hospital.hospital_Application.entity.Appointment;
import com.hospital.hospital_Application.dto.AppointmentRequestDTO;
import com.hospital.hospital_Application.service.AppointmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {
//    @Autowired
    private AppointmentService appointmentService;

    private static final Logger log = LoggerFactory.getLogger(AppointmentController.class);

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }


    @PostMapping("/book")
    public ResponseEntity<Appointment> bookAppointment(@RequestBody AppointmentRequestDTO request) {
        Appointment appointment = appointmentService.bookAppointment(request);
        log.info(" API Response: {}", appointment);
        return ResponseEntity.ok(appointment);
    }

//    @PostMapping("/book")
//    public ResponseEntity<?> bookAppointment(@RequestBody AppointmentRequestDTO request) {
//        try {
//            AppointmentResponseDTO response = (AppointmentResponseDTO) appointmentService.bookAppointment(request);
//            return ResponseEntity.ok(response);
//        } catch (SlotAlreadyBookedException e) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }


    @GetMapping("/{id}")
    public Appointment getAppointment(@PathVariable Long id) {
        return appointmentService.getAppointmentById(id);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<Appointment> getDoctorAppointments(@PathVariable Long doctorId) {
        return appointmentService.getAppointmentsForDoctor(doctorId);
    }

    @GetMapping("/patient/{patientId}")
    public List<Appointment> getPatientAppointments(@PathVariable Long patientId) {
        return appointmentService.getAppointmentsForPatient(patientId);
    }

    @GetMapping("/admin")
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }
}
