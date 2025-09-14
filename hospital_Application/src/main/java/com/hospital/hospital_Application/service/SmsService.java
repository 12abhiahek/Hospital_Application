package com.hospital.hospital_Application.service;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SmsService {

    private static final String AUTH_KEY = "467833AEfT7JYsf7u68bc00a4P1";
    private static final String TEMPLATE_ID = "68bc0acadb7b6518da492d26"; // DLT template
    private static final String SENDER_ID = "ABH184"; // 6-char sender ID
    private static final String ROUTE = "4"; // transactional route


    private final RestTemplate restTemplate = new RestTemplate();

    public void sendOtp(String mobile, String otp) {
        String url = String.format(
                "https://control.msg91.com/api/v5/otp?template_id=%s&mobile=91%s&authkey=%s&otp=%s",
                TEMPLATE_ID, mobile, AUTH_KEY, otp
        );

        restTemplate.getForObject(url, String.class);
    }

}
