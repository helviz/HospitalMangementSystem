package org.example.controller;


import org.example.controller.base.UpdateCredentials;
import org.example.dao.PatientDAO;
import org.example.models.entities.Patient;
import org.example.services.PatientService;
import org.example.views.PatientView;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class PatientController  implements UpdateCredentials  {
    PatientView view= new PatientView();
    PatientService service = new PatientService();

    public void CreatePatient() {
        Patient patient = PersonelController.CreatePersonel(Patient::new);
        Patient savedPatient = service.savePatient(patient);
        System.out.println(savedPatient);
    }

    public void updatePatient(){
        Scanner userInput = new Scanner(System.in);

        System.out.println("Select the field of the Patient to update:" +
                "\n1. First Name" +
                "\n2. Middle Name" +
                "\n3. Last Name" +
                "\n4. Email " +
                "\n5. Contact" +
                "\n6. Date of Birth"
        );

        try{
            int index = userInput.nextInt();

            switch (index) {
                case 1:
                    updateFirstName();
                    break;
                case 2:
                    updateMiddleName();
                    break;
                case 3:
                    updateLastName();
                    break;
                case 4:
                    updateEmail();
                    break;
                case 5:
                    updateContactNumber();
                    break;
                case 6:
                    updateDateOfBirth();
                    break;
                default:
                    System.out.println("Enter a number between 1 and 6:");

            }

        } catch (InputMismatchException e) {
            System.out.println("Error in update Patient field" + e.getMessage());
            userInput.nextLine();

        }

    }



    public void displayPatient(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Display Options:" +
                "\n1. Display all Patients" +
                "\n2. Find a patient ");

        try {
            int input = scanner.nextInt();
            switch(input){
                case 1:
                    List<Patient> patientlist = new PatientService().getAllPatients();
                    for(Patient patient: patientlist) {
                        System.out.println(patient);
                    }
                    break;
                case 2:
                    getPatient();
                    break;
                default:
                    System.out.println("Enter numbers btn 1 and 2.");
            }

        } catch (InputMismatchException e ){
            System.out.println("InputError: Enter a number:");
            scanner.nextLine();
        }

    }
    private void getPatient(){
        Scanner scanner = new Scanner(System.in);
        try {
            Long input = scanner.nextLong();
            Optional<Patient> patient = new PatientService().findPatientById(input);
            System.out.println(patient);

        } catch (InputMismatchException e){
            System.out.println("Error: Enter an integer");
            scanner.nextLine();
        }
    }

    private void updateFirstName(){
        Long id = IdInputHelper();

        String firstName;
        do {
            firstName = view.getFirstName();
            if (!service.isNonNullableNameValid(firstName)){
                System.out.println("Invalid name: Please enter letters only.");
            }
        } while(!service.isNonNullableNameValid(firstName));

        new PatientDAO().updateFirstNameField(id, firstName);


    }


    private void updateMiddleName(){
        Long id = IdInputHelper();

        String middleName;
        do {
            middleName = view.getMiddleName();
            if (!service.isNullableNameValid(middleName)){
                System.out.println("Invalid name: Please enter letters only.");
            }
        } while(!service.isNullableNameValid(middleName));



        new PatientDAO().updateMiddleNameField(id, middleName);
    }



    private void updateLastName(){
        Long id = IdInputHelper();

        String lastName;
        do {
            lastName = view.getLastName();
            if (!service.isNonNullableNameValid(lastName)){
                System.out.println("Invalid name: Please enter letters only.");
            }
        } while(!service.isNonNullableNameValid(lastName));

        new PatientDAO().updateLastNameField(id, lastName);
    }
    private void updateEmail(){
        Long id = IdInputHelper();

        String email;
        do {
            email = view.getEmail();
            if (!service.isEmailValid(email)){
                System.out.println("Invalid Email address: Please enter a valid email Address.");
            }
        } while (!service.isEmailValid(email));

    }
    private void updateContactNumber(){
        Long id = IdInputHelper();
        String contactNumber;
        do {
            contactNumber = view.getContactNumber();
            if (!service.iscontactNumberValid(contactNumber)){
                System.out.println("Invalid Telephone Number: Please enter a valid number.");
            }
        } while (!service.iscontactNumberValid(contactNumber));
        new PatientDAO().updateContactNumberField(id, contactNumber);

    }
    private void updateDateOfBirth(){
        Long id = IdInputHelper();
        LocalDate dateOfBirth;
        do {
            dateOfBirth = view.getDate();
            if(!service.isDateOfBirthValid(dateOfBirth)){
                System.out.println("Invalid Date of Birth: Please enter a valide date of birth.");
            }
        } while(!service.isDateOfBirthValid(dateOfBirth));
        new PatientDAO().updateDateOfBirthField(id, dateOfBirth);
    }

    private Long IdInputHelper() {
        Long id;

        do {
            id = view.getID();
            if (!service.isIdValid(id)){
                System.out.println("Invalid id: Please enter a valid id. ");

            }
        } while(!service.isIdValid(id));

        return id;
    }







}
