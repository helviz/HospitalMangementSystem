package org.example.services.Impl;

import jakarta.enterprise.context.ApplicationScoped;
import org.example.services.ServiceException;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

@ApplicationScoped
public class BaseService implements Serializable {


    public boolean isNonNullableNameValid(String name) {
        if (name == null || !name.matches("[A-Za-z]+")) {
            throw new ServiceException("Name must contain only letters and cannot be null");
        }
        return true;
    }


    public boolean isNullableNameValid(String name) {
        if (name != null && !name.trim().isEmpty() && !name.matches("[A-Za-z]+")) {
            throw new ServiceException("Name must contain only letters if provided");
        }
        return true;
    }


    public boolean isDateOfBirthValid(LocalDate dob) {
        if (dob == null) {
            throw new ServiceException("Date of birth cannot be null");
        }
        LocalDate today = LocalDate.now();
        if (dob.isAfter(today)) {
            throw new ServiceException("Date of birth must be in the past");
        }
        int age = Period.between(dob, today).getYears();
        if (age < 0 || age > 120) {
            throw new ServiceException("Age must be between 0 and 120 years");
        }
        return true;
    }


    public boolean isContactNumberValid(String contact) {
        if (contact == null || !contact.matches("^7\\d{8}$")) {
            throw new ServiceException("Contact number must be exactly 9 digits starting with 7");
        }
        return true;
    }

    public boolean isEmailValid(String email) {
        if (email == null || !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new ServiceException("Invalid email format");
        }
        return true;
    }

    public boolean isIdValid(Long id) {
        if (id == null || id <= 0) {
            throw new ServiceException("ID must be a positive number");
        }
        return true;
    }
}