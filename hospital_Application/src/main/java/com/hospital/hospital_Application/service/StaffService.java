package com.hospital.hospital_Application.service;


import com.hospital.hospital_Application.dto.StaffRequestDto;
import com.hospital.hospital_Application.entity.Role;
import com.hospital.hospital_Application.entity.Staff;
import com.hospital.hospital_Application.entity.User;
import com.hospital.hospital_Application.exception.StaffNotFoundException;
import com.hospital.hospital_Application.repository.StaffRepository;
import com.hospital.hospital_Application.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffService {
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;

    private static final Logger log = LoggerFactory.getLogger(StaffService.class);

    public StaffService(UserRepository userRepository, StaffRepository staffRepository) {
        this.userRepository = userRepository;
        this.staffRepository = staffRepository;
    }

    public Staff addStaff(StaffRequestDto dto) {
        log.info("Attempting to create staff with name: {}, phone: {}", dto.getName(), dto.getPhone());

        // 1. Save User entry
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setPassword(dto.getPassword()); // Hash this in real projects
        user.setRole(Role.STAFF);

        userRepository.save(user);
        log.info("User created for staff: {}, email: {}", dto.getName(), dto.getEmail());

        // 2. Save Staff entry linked to user

        Staff staff = new Staff();
        staff.setUser(user);
        staff.setId(user.getId());
        staff.setName(dto.getName());
        staff.setPhone(dto.getPhone());
        staff.setAge(dto.getAge());
        staff.setAddress(dto.getAddress());
        staff.setAadhaarNo(maskAadhaar(dto.getAadhaarNo())); // mask for log
        staff.setDesignation(dto.getDesignation());
//        staff.setDepartment(dto.getDepartment());
        staff.setShiftTime(dto.getShiftTime());
//        staff.setSalary(dto.getSalary());
        staff.setActive(dto.isActive());

        log.info("Staff created successfully: ID={}, Name={}, Designation={}",
                staff.getId(),
                staff.getName(),
//                staff.getDepartment(),
                staff.getDesignation());

        return staffRepository.save(staff);
    }

    private String maskAadhaar(String aadhaarNo) {
        if (aadhaarNo != null && aadhaarNo.length() >= 4) {
            return "XXXX-XXXX-" + aadhaarNo.substring(aadhaarNo.length() - 4);
        }
        return aadhaarNo;
    }

    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    public Optional<Staff> getStaffById(Long id) {
        log.debug("Looking for staff with id: {}", id);
        return staffRepository.findById(id);
    }

    public Staff updateStaff(Long id, StaffRequestDto dto) {
        log.debug("Updating staff with id: {}", id);
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new StaffNotFoundException("Staff with ID " + id + " not found"));

        staff.setName(dto.getName());
      //  staff.setRole(dto.getRole());
       // staff.setEmail(dto.getEmail());
        staff.setPhone(dto.getPhone());

        return staffRepository.save(staff);
    }

    public void deleteStaff(Long id) {
        log.debug("Deleting staff with id: {}", id);
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new StaffNotFoundException("Staff with ID " + id + " not found"));
        staffRepository.delete(staff);
    }

}
