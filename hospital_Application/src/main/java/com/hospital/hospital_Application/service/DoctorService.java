package com.hospital.hospital_Application.service;


import com.hospital.hospital_Application.entity.DoctorList;
import com.hospital.hospital_Application.repository.DoctorListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    @Autowired
    private DoctorListRepository doctorRepository;


    public List<DoctorList> getAllDoctors() {
        return doctorRepository.findAll();
    }


    public Optional<DoctorList> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }


    public DoctorList addDoctor(DoctorList doctor) {
        return doctorRepository.save(doctor);
    }


    public DoctorList updateDoctor(Long id, DoctorList updatedDoctor) {
        return doctorRepository.findById(id)
                .map(existing -> {
                    existing.setName(updatedDoctor.getName());
                    existing.setSpecialization(updatedDoctor.getSpecialization());
                    return doctorRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }


    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

}
