package com.dwr.loginregistrationbackend.service;

import com.dwr.loginregistrationbackend.registration.RegistrationRequest;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    public String register(RegistrationRequest request){
        return "worked";
    }
}
