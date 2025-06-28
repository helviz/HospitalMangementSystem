package org.example.activity;

import org.example.controller.DoctorController;
import org.example.controller.StaffController;
import org.example.models.entities.User;

import java.util.InputMismatchException;
import java.util.Scanner;

public class PerformAdminActivity {
    private User user;

    public PerformAdminActivity(User user){
        this.user = user;
    }

    public boolean run(boolean systemState) {
        Scanner userinput = new Scanner(System.in);

        System.out.println("Select the operation to perform." +
                "\n1. Create" +
                "\n2. Display" +
                "\n3. Update" +
                "\n4. Delete" +
                "\n5. Exit"
        );
        try {
            int input = userinput.nextInt();
            switch (input) {
                case 1:
                    CreateUsers();
                    break;
                case 2:
                    UpdateUsers();
                    break;
                case 3:
                    DisplayUsers();
                    break;
                case 4:
                    DeleteUsers();
                    break;
                case 5:
                    systemState = false;
                    break;
                default:
                    System.out.println("You Entered an invalid number validNumbers(1,2,3,4,5).");

            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input: Please enter an integer:");
            userinput.nextLine();

        }

        return systemState;
    }

    private void CreateUsers() {
        Scanner createInput = new Scanner(System.in);
        System.out.println("Select what to Create:" +
                "\n1. Staff " +
                "\n2. Doctor "

        );

        try {
            int userinput1 = createInput.nextInt();
            switch (userinput1) {
                case 1:
                    new StaffController().Create();
                    break;
                case 2:
                    new DoctorController().CreateDoctor();
                    break;
                default:
                    System.out.println("Invalid input: Please enter a number between 1 and 2.");
            }

        } catch (InputMismatchException e) {
            System.out.println("Invalid input: Please enter an integer:");
            createInput.nextLine();

        }
    }

    private static void UpdateUsers() {
        Scanner createInput = new Scanner(System.in);
        System.out.println("Select User whose details you want to update:" +
                "\n1. Staff " +
                "\n2. Doctor "

        );

        try {
            int userinput = createInput.nextInt();
            switch (userinput) {
                case 1:
                    new StaffController().updateStaffUser();
                    break;
                case 2:
                    new DoctorController().updateDoctorUser();
                    break;

                default:
                    System.out.println("Invalid input: Please enter a number between 1 and 2.");
            }

        } catch (InputMismatchException e) {
            System.out.println("Invalid input: Please enter an integer:");
            createInput.nextLine();

        }
    }

    private void DisplayUsers(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select what category to display: " +
                "\n1. Doctor" +
                "\n2. Staff");

        try {
            int index = scanner.nextInt();
            switch (index){
                case 1:
                    new DoctorController().displayDoctor();
                    break;
                case 2:
                    new StaffController().displayStaff();
                    break;
                default:
                    System.out.println("Enter either 1 or 2");
            }
        } catch (InputMismatchException e){
            System.out.println("Error: enter an integer:");
        }

    }

    private void DeleteUsers(){

    }




}
