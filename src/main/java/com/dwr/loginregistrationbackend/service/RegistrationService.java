package com.dwr.loginregistrationbackend.service;

import com.dwr.loginregistrationbackend.domain.AppUser;
import com.dwr.loginregistrationbackend.domain.AppUserRole;
import com.dwr.loginregistrationbackend.registration.EmailValidator;
import com.dwr.loginregistrationbackend.registration.RegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class RegistrationService {

    private final EmailValidator emailValidator;
    private final AppUserService appUserService;

    public String register(RegistrationRequest request){
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if(!isValidEmail){
            throw new IllegalStateException("email not valid");
        }
        //ctrl + p
        return appUserService.signUpUser(new AppUser(
                request.getFirstname(),
                request.getLastname(),
                request.getEmail(),
                request.getPassword(),
                AppUserRole.USER
        ));
    }
}
