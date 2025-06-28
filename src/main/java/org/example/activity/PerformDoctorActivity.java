package org.example.activity;

import org.example.controller.DoctorController;
import org.example.models.entities.User;

import java.util.InputMismatchException;
import java.util.Scanner;

public class PerformDoctorActivity {
    private User user;
    private Long doctorId;

    public PerformDoctorActivity(User user){
        this.user = user;
        this.doctorId = determineDoctorId(user);

    }

    public boolean run (boolean systemState){

        Scanner doctorInput = new Scanner(System.in);
        System.out.println("Enter the Operations to perform." +
                "\n1. View Appointments" +
                "\n2. View Patients past Medical Records" +
                "\n3. Create Medical Record for a patient." +
                "\n4. Exit");
        try {
            int index = doctorInput.nextInt();
            switch (index){
                case 1:
                    ViewAppointments(doctorId);
                    break;
                case 2:
                    ViewMedicalRecords();
                    break;
                case 3:
                    CreateMedicalPatientRecord(doctorId);
                    break;
                case 4:
                    systemState = false;
                default:
                    System.out.println("Select either (1, 2, 3, 4)");
            }
        } catch (InputMismatchException e){
            System.out.println("Input Error: Only integers accepted");
        }

        return systemState;

    }

    private Long determineDoctorId(User user) {
        if (user != null && user.getDoctor() != null) {
            return user.getDoctor().getDoctorID();
        }
        return null;
    }

    private void ViewAppointments(Long doctorId){
        new DoctorController().viewDoctorAppointment(doctorId);
    }
    private void ViewMedicalRecords(){
        new DoctorController().viewPatientMedicalRecords();
    }
    private void CreateMedicalPatientRecord(Long doctorId){
        new DoctorController().writeMedicalDiagnosis(doctorId);
    }

}
