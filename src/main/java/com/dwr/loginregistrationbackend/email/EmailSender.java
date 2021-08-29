package com.dwr.loginregistrationbackend.email;

public interface EmailSender {
    void send(String to, String email);
}

