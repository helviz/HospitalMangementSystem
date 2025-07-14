package org.example;

import org.example.dao.AppointmentDAO;
import org.example.services.Impl.MedicalRecordServiceImpl;
import org.example.services.MedicalRecordService;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        System.out.println("System is running");

        AppointmentDAO appointmentDAO = new AppointmentDAO();
        Long patientId = Long.valueOf(68);
        Long doctorId = Long.valueOf(1);
        LocalDateTime  noon = LocalDateTime.of(2025, 7, 11, 12, 0);
        LocalDateTime  afternoon = LocalDateTime.of(2025, 7, 11, 3, 0);
        boolean success;
        success = appointmentDAO.createAppointment(patientId, doctorId, afternoon);

        if (success) System.out.println("Created an appointment at afternoon" );





    }
}