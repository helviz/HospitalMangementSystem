package org.example.activity;

import org.example.controller.AppointmentController;
import org.example.controller.PatientController;
import org.example.models.entities.User;

import java.util.InputMismatchException;
import java.util.Scanner;

public class PerformReceptionActivity {
    private User user;

    public PerformReceptionActivity(User user){
        this.user  = user;
    }

    public boolean run(boolean systemState){

        System.out.println("Select the operation to perform." +
                "\n1. Manage Patients" +
                "\n2. Manage Appointments" +
                "\n3. Exit ");

        try {
            int index = new Scanner(System.in).nextInt();
            switch (index){
                case 1:
                    managePatients();
                    break;
                case 2:
                    manageAppointments();
                    break;
                case 3:
                    systemState = false;
                default:
                    System.out.println("Enter either 1 or 3");
            }

        } catch(InputMismatchException e){
            System.out.println("Input Error: You entered a non integer.");
        }


        return systemState;
    }

    private void managePatients(){
        System.out.println("Enter an operation to perform: " +
                "\n1. Create Patient." +
                "\n2. Update Patient Details"
                );
        Scanner scanner = new Scanner(System.in);

        try {
            int input = scanner.nextInt();
            switch (input){
                case 1:
                    new PatientController().CreatePatient();
                    break;
                case 2:
                    new PatientController().updatePatient();
                    break;
                default:
                    System.out.println("Please enter either 1 or 2");

            }


        }catch (InputMismatchException e){
            System.out.println("You have entered non integer");
            scanner.nextLine();
        }
    }
    private void manageAppointments() {
        System.out.println("Select the Operation to perform " +
                "\n1. Create Appointment" +
                "\n2. View Appointments" +
                "\n3. Cancel Appointments"
                );


        Scanner scanner = new Scanner(System.in);
        try {
            int index = scanner.nextInt();
            switch (index) {
                case 1:
                    new AppointmentController().createAppointment();
                    break;
                case 2:
                    new AppointmentController().viewAppointments();
                    break;
                case 3:
                    new AppointmentController().cancelAppointment();
                    break;
                default:
                    System.out.println("Please enter a number in the range 1,2,3");
                    break;
            }


        } catch (InputMismatchException e) {
            System.out.println("Error: Please enter an integer.");
            scanner.nextLine();
        }

    }


}
