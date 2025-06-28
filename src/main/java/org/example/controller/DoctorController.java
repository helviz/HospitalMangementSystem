package org.example.controller;

import org.example.controller.base.UpdateCredentials;
import org.example.dao.AppointmentDAO;
import org.example.dao.DoctorDAO;
import org.example.dao.MedicalRecordDAO;
import org.example.enums.AppointmentStatus;
import org.example.enums.Role;
import org.example.enums.Speciality;
import org.example.models.entities.Appointment;
import org.example.models.entities.Doctor;

import org.example.models.entities.MedicalRecord;
import org.example.models.entities.User;
import org.example.services.DoctorService;

import org.example.services.PersonelService;
import org.example.services.UserService;
import org.example.views.DoctorView;
import org.example.views.UserView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class DoctorController implements UpdateCredentials {
    DoctorView view= new DoctorView();
    DoctorService service = new DoctorService();
    UserView userView = new UserView();
    UserService userService = new UserService();

    public void CreateDoctor(){
        Doctor doctor = PersonelController.CreatePersonel(Doctor::new);
        Speciality speciality = view.getSpeciality();
        doctor.setSpeciality(speciality);

//        setting the user_account
        String password;
        do {
            password = userView.getUserPassword();
            if(!userService.isPasswordValid(password)){
                System.out.println("Invalid password: Please a valid password At least 8 characters, 1 uppercase, 1 lowercase, 1 digit, 1 special char.");
            }
        } while (!userService.isPasswordValid(password));
        String email = doctor.getEmail();
        User user = new User(email, Role.DOCTOR, password);

        //assignig the useraccount to the doctor
        doctor.setUser(user);
        Doctor savedDoctor = service.saveDoctor(doctor);
        System.out.println(savedDoctor);

    }


    public void updateDoctorUser(){
        Scanner userInput = new Scanner(System.in);

        System.out.println("Select the field of the Doctor to update:" +
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
            System.out.println("Error in update Doctor field" + e.getMessage());
            userInput.nextLine();

        }

    }

    public void displayDoctor(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Display Options:" +
                "\n1. Display all Doctors" +
                "\n2. Find a doctor ");

        try {
            int input = scanner.nextInt();
            switch(input){
                case 1:
                    List<Doctor> doctorlist = new DoctorService().getAllDoctors();
                    for(Doctor doctor: doctorlist) {
                        System.out.println(doctor);
                    }
                    break;
                case 2:
                    getDoctor();
                    break;
                default:
                    System.out.println("Enter numbers btn 1 and 2.");
            }

        } catch (InputMismatchException e ){
            System.out.println("InputError: Enter a number:");
            scanner.nextLine();
        }

    }

    public void viewDoctorAppointment(Long doctorid){
        List<Appointment> doctorAppointments = new AppointmentDAO().getAppointmentsByDoctor(doctorid);
        for(Appointment appointment: doctorAppointments){
            int i = 1;
            System.out.println(i + ". " +  appointment);
            i += 1;
        }
    }

    public void viewPatientMedicalRecords(){
        DoctorView view = new DoctorView();
        PersonelService service = new PersonelService();
        Long id;
        boolean valid;

        do {
            id = view.getPatientId();
            valid = service.isIdValid(id);

            if(!valid) System.out.println("Enter a valid id");
        } while(!valid);

        List<MedicalRecord> records = new MedicalRecordDAO().getMedicalRecordsByPatient(id);

        for (MedicalRecord record: records){
            int i = 0;
            System.out.println(i + ". " + record );
        }


    }

    public void writeMedicalDiagnosis(Long doctorid){
        Long patientId;
        String diagnosis;
        LocalDate recordDate;
        boolean valid;

        do {
            patientId = view.getPatientId();
            valid = service.isIdValid(patientId);

            if(!valid) System.out.println("Enter a valid id");
        } while(!valid);

        diagnosis = new DoctorView().writeDiagnosis();

        recordDate = LocalDate.now();

        new MedicalRecordDAO().createMedicalRecord(patientId, doctorid, diagnosis, recordDate);




    }

    private void getDoctor(){
        Scanner scanner = new Scanner(System.in);
        try {
            Long input = scanner.nextLong();
            Doctor doctor = new DoctorService().findDoctorById(input);
            System.out.println(doctor);

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

        new DoctorDAO().updateFirstNameField(id, firstName);


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



        new DoctorDAO().updateMiddleNameField(id, middleName);
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

        new DoctorDAO().updateLastNameField(id, lastName);
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
        new DoctorDAO().updateContactNumberField(id, contactNumber);

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
        new DoctorDAO().updateDateOfBirthField(id, dateOfBirth);
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
