package org.example.services;

public class UserService {
    public boolean isPasswordValid(String password) {
        if (password == null) {
            return false;
        }
        // At least 8 characters, 1 uppercase, 1 lowercase, 1 digit, 1 special char
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        return password.matches(pattern);
    }

}
