package org.example.controller;

import org.example.controller.base.UpdateCredentials;
import org.example.dao.StaffDAO;

import org.example.enums.Role;
import org.example.models.entities.Staff;

import org.example.models.entities.User;
import org.example.services.StaffService;

import org.example.services.UserService;
import org.example.views.RoleView;
import org.example.views.StaffView;
import org.example.views.UserView;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class StaffController implements UpdateCredentials {
    StaffView view= new StaffView();
    StaffService service = new StaffService();
    UserView userView = new UserView();
    UserService userService = new UserService();
    RoleView roleView = new RoleView();

    public void Create(){

        Staff staff = PersonelController.CreatePersonel(Staff::new);


        //        setting the user_account
        String password;
        do {
            password = userView.getUserPassword();
            if(!userService.isPasswordValid(password)){
                System.out.println("Invalid password: Please a valid password At least 8 characters, 1 uppercase, 1 lowercase, 1 digit, 1 special char.");
            }
        } while (!userService.isPasswordValid(password));
        String email = staff.getEmail();
        Role role = roleView.getRole();
        User user = new User(email, role, password);

        staff.setUser(user);
        Staff savedStaff = service.saveStaff(staff);
        System.out.println(savedStaff);
    }

    public void updateStaffUser(){
        Scanner userInput1 = new Scanner(System.in);

        System.out.println("Select the field of the Staff to update:" +
                "\n1. First Name" +
                "\n2. Middle Name" +
                "\n3. Last Name" +
                "\n4. Email " +
                "\n5. Contact" +
                "\n6. Date of Birth"
        );

        try{
            int index = userInput1.nextInt();

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
            userInput1.nextLine();

        }

    }

    public void displayStaff(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Display Options:" +
                "\n1. Display all Staffs" +
                "\n2. Find a staff ");

        try {
            int input = scanner.nextInt();
            switch(input){
                case 1:
                    List<Staff> stafflist = new StaffService().getAllStaffs();
                    for(Staff staff: stafflist) {
                        System.out.println(staff);
                    }
                    break;
                case 2:
                    getStaff();
                    break;
                default:
                    System.out.println("Enter numbers btn 1 and 2.");
            }

        } catch (InputMismatchException e ){
            System.out.println("InputError: Enter a number:");
            scanner.nextLine();
        }

    }
    private void getStaff(){
        Scanner scanner = new Scanner(System.in);
        try {
            Long input = scanner.nextLong();
            Staff staff = new StaffService().findStaffById(input);
            System.out.println(staff);

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

        new StaffDAO().updateFirstNameField(id, firstName);


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



        new StaffDAO().updateMiddleNameField(id, middleName);
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

        new StaffDAO().updateLastNameField(id, lastName);
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
        new StaffDAO().updateContactNumberField(id, contactNumber);

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
        new StaffDAO().updateDateOfBirthField(id, dateOfBirth);
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
