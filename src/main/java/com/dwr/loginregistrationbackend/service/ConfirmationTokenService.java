package com.dwr.loginregistrationbackend.service;


import com.dwr.loginregistrationbackend.registration.token.ConfirmationToken;
import com.dwr.loginregistrationbackend.repository.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository repository;

    public void saveConfirmationToken(ConfirmationToken token){
        repository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token){
        return repository.findByToken(token);
    }

    public int setConfirmedAt(String token){
        return repository.updateConfirmedAt(token, LocalDateTime.now());
    }

}
