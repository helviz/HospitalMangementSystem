package org.example.services;

import java.time.LocalDate;
import java.time.Period;

public class PersonelService {

    public boolean isNonNullableNameValid(String name){
        return name != null && name.matches("[A-Za-z]+");
    }

    public boolean isNullableNameValid(String name){
        return name == null || name.trim().isEmpty() || name.matches("[A-Za-z]+");
    }

    public boolean isDateOfBirthValid(LocalDate dob){
        LocalDate today = LocalDate.now();

        // Must be in the past
        if (dob.isAfter(today)) {
            return false;
        }

        // Optionally, check minimum age
        int age = Period.between(dob, today).getYears();
        if (age < 0 || age > 120) {  // Arbitrary upper limit
            return false;
        }

        return true;

    }
    public boolean iscontactNumberValid(String contact){
        return contact.matches("^7\\d{8}$");

    }

    public boolean isEmailValid(String email){
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }
    
    public boolean isIdValid(Long id){
        return id != null && id > 0;
    }
    




}
