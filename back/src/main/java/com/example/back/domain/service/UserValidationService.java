package com.example.back.domain.service;

public class UserValidationService {

    public void validateEmail(String email) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }

    private boolean isValidEmail(String email) {
        if (email == null)
            return false;
        String v = email.trim();
        if (v.isEmpty())
            return false;

        return v.contains("@") && v.indexOf('@') > 0 && v.indexOf('@') < v.length() - 1;
    }
}
