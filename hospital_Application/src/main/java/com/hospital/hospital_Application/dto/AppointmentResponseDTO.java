package com.hospital.hospital_Application.dto;




public class AppointmentResponseDTO {

    private Long id;
    private String doctorName;
    private String doctorSpecialty;
    private String doctorExperience;
    private String doctorImageUrl;
    private String date;
    private String time;
    private String patientName;
    private String patientEmail;
    private String patientPhone;
    private String status;

    private String reason;


    public AppointmentResponseDTO(Long id, String doctorName, String specialty, String experience,
                                  String date, String time,
                                  String patientName, String patientEmail, String patientPhone,
                                  String status,String reason) {
        this.id = id;
        this.doctorName = doctorName;
        this.doctorSpecialty = specialty;
        this.doctorExperience = experience;
        this.date = date;
        this.time = time;
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.patientPhone = patientPhone;
        this.status = status;
        this.reason=reason;
    }


}
