package com.hospital.hospital_Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class HospitalApplication {
	private static final Logger logger = LoggerFactory.getLogger(HospitalApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(HospitalApplication.class, args);

		System.out.println("MedCare Application is starting...");
		String logFilePath = new File("logs/hospital-app.log").getAbsolutePath();

		System.out.println("Log file created at: " + logFilePath);
		logger.info(" Hospital Booking Application started successfully!");

//		try {
//			File logFile = new File(logFilePath);
//			if (logFile.exists()) {
//				new ProcessBuilder("notepad.exe", logFile.getAbsolutePath()).start();
//			} else {
//				System.out.println("Log file not found: " + logFilePath);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}

}
