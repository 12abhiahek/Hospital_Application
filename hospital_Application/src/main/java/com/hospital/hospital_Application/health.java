package com.hospital.hospital_Application;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/check")
public class health {
    @GetMapping
    public String checkhealth(){
        return "Ok";
    }

}
