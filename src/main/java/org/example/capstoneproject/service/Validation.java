package org.example.capstoneproject.service;

import org.springframework.stereotype.Component;

@Component
public class Validation {

    public boolean validatePhoneNumber(String phone){
//        if(phone == null || phone.isBlank()) return false;

        if(phone.length() != 13) return false;

        if (!phone.startsWith("+") || !phone.substring(1).matches("\\d+")) return false;

        return true;
    }

    public boolean validateEmail(String email){
//        if(email == null || email.isBlank() ) return false;

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        return email.matches(emailRegex);
    }


}
