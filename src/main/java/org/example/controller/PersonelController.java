package org.example.controller;

import org.example.utilities.CapitaliseUtil;
import org.example.models.entities.Personel;
import org.example.services.PersonelService;
import org.example.views.PersonelView;

import java.time.LocalDate;
import java.util.function.Supplier;

public class PersonelController {

    public static <T extends Personel> T CreatePersonel(Supplier<T> constructor) {
            PersonelView view = new PersonelView();
            PersonelService service = new PersonelService();

            String firstName;
            do {
                firstName = view.getFirstName();
                if (!service.isNonNullableNameValid(firstName)){
                    System.out.println("Invalid name: Please enter letters only.");
                }
            } while(!service.isNonNullableNameValid(firstName));

            String middleName;
            do {
                middleName = view.getMiddleName();
                if (!service.isNullableNameValid(middleName)){
                    System.out.println("Invalid name: Please enter letters only.");
                }
            } while(!service.isNullableNameValid(middleName));

            String lastName;
            do {
                lastName = view.getLastName();
                if (!service.isNonNullableNameValid(lastName)){
                    System.out.println("Invalid name: Please enter letters only.");
                }
            } while(!service.isNonNullableNameValid(lastName));


            LocalDate dateOfBirth;
            do {
                dateOfBirth = view.getDate();
                if(!service.isDateOfBirthValid(dateOfBirth)){
                    System.out.println("Invalid Date of Birth: Please enter a valide date of birth.");
                }
            } while(!service.isDateOfBirthValid(dateOfBirth));

            String contactNumber;
            do {
                contactNumber = view.getContactNumber();
                if (!service.iscontactNumberValid(contactNumber)){
                    System.out.println("Invalid Telephone Number: Please enter a valid number.");
                }
            } while (!service.iscontactNumberValid(contactNumber));

            String email;
            do {
                email = view.getEmail();
                if (!service.isEmailValid(email)){
                    System.out.println("Invalid Email address: Please enter a valid email Address.");
                }
            } while (!service.isEmailValid(email));

            //   CapitaliseNames
            firstName = CapitaliseUtil.Capitalise(firstName);
            middleName = CapitaliseUtil.Capitalise(middleName);
            lastName = CapitaliseUtil.Capitalise(lastName);

            T person = constructor.get();
            person.setFirstName(firstName);
            person.setMiddleName(middleName);
            person.setLastName(lastName);
            person.setDateOfBirth(dateOfBirth);
            person.setContactNumber(contactNumber);
            person.setEmail(email);

            return  person;




    }



}
