package org.example.models.entities;

import jakarta.persistence.*;
import org.example.enums.AppointmentStatus;
import org.example.models.base.SoftDeletable;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name="appointments")
public class Appointment implements SoftDeletable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="appointment_id")
    private Long appointmentID;

    @Column(name="appointment_date_time")
    private LocalDate appointmentDateTime;

    @Column(name ="appointment_status")
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    private boolean deleted = false;

    public void setAppointmentID(Long appointmentID) {
        this.appointmentID = appointmentID;
    }

    public Long getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentDateTime(LocalDate appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public LocalDate getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setPatient(Patient patient){
        this.patient = patient;
    }

    public void setDoctor(Doctor doctor){
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    @Override
    public void setDelete(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean isDelete() {
        return this.deleted;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentID=" + appointmentID +
                ", appointmentDateTime=" + appointmentDateTime +
                ", status=" + status +
                ", patient=" + (patient != null ? patient.getFirstName() + " " + patient.getLastName() : "N/A") +
                ", doctor=" + (doctor != null ? doctor.getFirstName() + " " + doctor.getLastName() : "N/A") +
                '}';


    }

}