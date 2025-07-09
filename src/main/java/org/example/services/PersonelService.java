package org.example.services;

import java.time.LocalDate;

public interface PersonelService {
    boolean isNonNullableNameValid(String name);
    boolean isNullableNameValid(String name);
    boolean isDateOfBirthValid(LocalDate dob);
    boolean isContactNumberValid(String contact);
    boolean isEmailValid(String email);
    boolean isIdValid(Long id);


}