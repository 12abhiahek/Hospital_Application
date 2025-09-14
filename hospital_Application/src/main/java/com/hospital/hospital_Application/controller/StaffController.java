package com.hospital.hospital_Application.controller;


import com.hospital.hospital_Application.entity.Staff;
import com.hospital.hospital_Application.exception.StaffNotFoundException;
import com.hospital.hospital_Application.dto.StaffRequestDto;
import com.hospital.hospital_Application.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/staff")
@CrossOrigin(origins = "*")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }
    private static final Logger log = LoggerFactory.getLogger(StaffController.class);



    @PostMapping("/add")
    public ResponseEntity<?> addStaff(@RequestBody StaffRequestDto dto) {
        try {
            log.info("Adding new staff: {}", dto.getName());
            Staff staff = staffService.addStaff(dto);
            return ResponseEntity.ok(staff);
        } catch (Exception e) {
            log.error("Error adding staff: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to add staff", "details", e.getMessage()));
        }
    }



    @GetMapping("/all")
    public ResponseEntity<?> getAllStaff() {
        try {
            log.info("Fetching all staff records");
            return ResponseEntity.ok(staffService.getAllStaff());
        } catch (Exception e) {
            log.error("Error fetching staff list: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch staff", "details", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStaffById(@PathVariable Long id) {
        try {
            log.info("Fetching staff by id: {}", id);
            Staff staff = staffService.getStaffById(id)
                    .orElseThrow(() -> new StaffNotFoundException("Staff with ID " + id + " not found"));
            return ResponseEntity.ok(staff);
        } catch (StaffNotFoundException e) {
            log.warn("Staff not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error fetching staff by id: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch staff", "details", e.getMessage()));
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateStaff(@PathVariable Long id, @RequestBody StaffRequestDto dto) {
        try {
            log.info("Updating staff with id: {}", id);
            Staff updatedStaff = staffService.updateStaff(id, dto);
            return ResponseEntity.ok(updatedStaff);
        } catch (StaffNotFoundException e) {
            log.warn("Staff not found for update: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error updating staff: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update staff", "details", e.getMessage()));
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStaff(@PathVariable Long id) {
        try {
            log.info("Deleting staff with id: {}", id);
            staffService.deleteStaff(id);
            return ResponseEntity.ok(Map.of("message", "Staff deleted successfully with ID: " + id));
        } catch (StaffNotFoundException e) {
            log.warn("Staff not found for delete: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error deleting staff: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete staff", "details", e.getMessage()));
        }
    }
}
