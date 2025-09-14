package com.hospital.hospital_Application.controller;


import com.hospital.hospital_Application.entity.DoctorList;
import com.hospital.hospital_Application.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/doctors")
public class DoctorListController {
    @Autowired
    private DoctorService doctorService;


    @GetMapping("/list")
    public List<DoctorList> getAllDoctors() {
        return doctorService.getAllDoctors();
    }


    @GetMapping("/{id}")
    public Optional<DoctorList> getDoctorById(@PathVariable Long id) {
        return doctorService.getDoctorById(id);
    }


    @PostMapping("/add")
    public DoctorList addDoctor(@RequestBody DoctorList doctor) {
        return doctorService.addDoctor(doctor);
    }


    @PutMapping("/{id}")
    public DoctorList updateDoctor(@PathVariable Long id, @RequestBody DoctorList doctor) {
        return doctorService.updateDoctor(id, doctor);
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }

}
