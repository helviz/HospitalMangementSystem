package org.example.controller;

import org.example.dao.AppointmentDAO;
import org.example.dao.DoctorDAO;
import org.example.enums.AppointmentStatus;
import org.example.enums.Speciality;
import org.example.models.entities.Appointment;
import org.example.models.entities.Doctor;
import org.example.services.PersonelService;
import org.example.views.AppointmentView;
import org.example.views.DoctorView;
import org.example.views.PatientView;
import org.example.views.PersonelView;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class AppointmentController {
    public AppointmentController(){}

    public void createAppointment(){
        PatientView view = new PatientView();
        PersonelService service = new PersonelService();

        boolean valid;
        Long patientId;

        do {
            patientId = view.getPatientId();
            valid = service.isIdValid(patientId);
            if (!valid) System.out.println(" Error: Please enter a valid id");

        } while(!valid);

        Long doctorId  = getDoctor();

        AppointmentStatus status = AppointmentStatus.IN_PROGRESS;

        LocalDate appointmentDate = new AppointmentView().getAppointmentDate();



        new AppointmentDAO().createAppointment(patientId,doctorId,appointmentDate,status);

    }

    private Long getDoctor() {
        DoctorView view = new DoctorView();
        PersonelService service = new PersonelService();
        Long doctorid;
        System.out.println("Select the Speciality of the doctor." +
                "\n1. GENERAL_MEDICINE" +
                "\n2. PEDIATRICS" +
                "\n3. GYNECOLOGY" +
                "\n4. ORTHOPEDICS" +
                "\n5. DERMATOLOGY");

        Speciality speciality = null;
        Scanner scanner = new Scanner(System.in);
        try {
            int index = scanner.nextInt();

            switch (index) {
                case 1:
                    speciality = Speciality.GENERAL_MEDICINE;
                    break;
                case 2:
                    speciality = Speciality.PEDIATRICS;
                    break;
                case 3:
                    speciality = Speciality.GYNECOLOGY;
                    break;
                case 4:
                    speciality = Speciality.ORTHOPEDICS;
                    break;
                case 5:
                    speciality = Speciality.DERMATOLOGY;
                    break;
                default:
                    System.out.println("Enter an integer between 1 and 6");
            }

        } catch (InputMismatchException e) {
            System.out.println("Please enter an integer");
            scanner.nextLine();
        }

        List<Doctor> doctorsListOfSpecialityType = new DoctorDAO().getDoctorsBySpeciality(speciality);
        for (Doctor doctor : doctorsListOfSpecialityType) {
            int i = 0;
            System.out.println(i + "). " + doctor);
            i += 1;
        }

        System.out.println("Please select a doctor from the above list and enter the id.");
        Scanner scanner1 = new Scanner(System.in);

        boolean valid;

        do {
            doctorid = scanner1.nextLong();
            valid = service.isIdValid(doctorid);
            if (!valid) {
                System.out.println("Errror: Please enter a valid id:");
            }
        } while (!valid);
        return doctorid;
    }





    public void cancelAppointment(){
        AppointmentView view = new AppointmentView();
        PersonelService service = new PersonelService();
        Long appointmentId;
        boolean valid;

        do{
            appointmentId = view.getAppointmentId();
            valid = service.isIdValid(appointmentId);
            if(!valid) System.out.println("Please ente a valid id");


        } while(!valid);

        new AppointmentDAO().cancelAppointment(appointmentId);
        System.out.println("You have successfully cancalled an appointment.");


    }
    public void viewAppointments(){
        DoctorView view = new DoctorView();
        PersonelService service = new PersonelService();

        Long patientId;
        boolean valid;

        do {
            patientId = view.getPatientId();
            valid = service.isIdValid(patientId);
            if (!valid) System.out.println(" Error: Please enter a valid id");

        } while(!valid);

        List<Appointment> patientAppointments = new AppointmentDAO().getAppointmentsByPatient(patientId);

        if (!patientAppointments.isEmpty()) {
            for(Appointment app: patientAppointments){
                int i = 0;
                System.out.println(i + "). " + app);
            }
        } else System.out.println("There are no appointments for this patient");
    }
}
