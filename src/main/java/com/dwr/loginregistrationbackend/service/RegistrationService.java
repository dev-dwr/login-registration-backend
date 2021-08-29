package com.dwr.loginregistrationbackend.service;

import com.dwr.loginregistrationbackend.domain.AppUser;
import com.dwr.loginregistrationbackend.domain.AppUserRole;
import com.dwr.loginregistrationbackend.email.EmailSender;
import com.dwr.loginregistrationbackend.registration.EmailValidator;
import com.dwr.loginregistrationbackend.registration.RegistrationRequest;
import com.dwr.loginregistrationbackend.registration.token.ConfirmationToken;
import com.dwr.loginregistrationbackend.util.EmailBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class RegistrationService {

    private final EmailValidator emailValidator;
    private final AppUserService appUserService;
    private final ConfirmationTokenService tokenService;
    private final EmailSender emailSender;
    private final EmailBuilder emailBuilder;
    private static final String REGISTRATION_LINK = "http://localhost:8080/api/v1/registration/confirm?token=";

    public String register(RegistrationRequest request){

        boolean isValidEmail = emailValidator.test(request.getEmail());

        if(!isValidEmail){
            throw new IllegalStateException("email not valid");
        }

        String token =  appUserService.signUpUser(new AppUser(
                request.getFirstname(),
                request.getLastname(),
                request.getEmail(),
                request.getPassword(),
                AppUserRole.USER
        ));

        String link = REGISTRATION_LINK + token;

        emailSender.send(
                request.getEmail(),
                emailBuilder.buildEmail(request.getFirstname(), link)//HTML email
        );

        return token;
    }

    @Transactional //two update statements, so instead two queries we would send one
    public String confirmToken(String token){
        ConfirmationToken confirmationToken = tokenService.getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if(confirmationToken.getConfirmedAt() != null){
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        tokenService.setConfirmedAt(token);
        appUserService.enableAppUser(
                confirmationToken.getAppUser().getEmail());

        return "confirmed";
    }



}
