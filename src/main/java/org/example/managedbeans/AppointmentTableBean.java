package org.example.managedbeans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.enums.AppointmentStatus;
import org.example.models.entities.Appointment;
import org.example.services.AppointmentService;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Named("appointmentTableBean")
@ViewScoped
public class AppointmentTableBean implements Serializable {

    private List<Appointment> appointments;
    private Appointment selectedAppointment;

    private Long doctorId;
    private LocalDate startDate;
    private LocalDate endDate;

    @Inject
    private AppointmentService appointmentService;

    @PostConstruct
    public void init() {
        appointments = appointmentService.getAllAppointments();
    }

    public void refreshAppointments() {
        appointments = appointmentService.getAllAppointments();
    }

    public void filterByDoctorAndDateRange() {
        if (doctorId != null && startDate != null && endDate != null) {
            appointments = appointmentService.getAppointmentsByDoctorAndDateRange(doctorId, startDate, endDate);
        }
    }

    public void updateAppointmentStatus(AppointmentStatus newStatus) {
        if (selectedAppointment != null && selectedAppointment.getAppointmentID() != null && newStatus != null) {
            appointmentService.updateAppointmentStatus(selectedAppointment.getAppointmentID(), newStatus);
            refreshAppointments();
            selectedAppointment = null;
        }
    }

    public void cancelSelectedAppointment() {
        if (selectedAppointment != null && selectedAppointment.getAppointmentID() != null) {
            appointmentService.cancelAppointment(selectedAppointment.getAppointmentID());
            refreshAppointments();
            selectedAppointment = null;
        }
    }

    // Getters and setters

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public Appointment getSelectedAppointment() {
        return selectedAppointment;
    }

    public void setSelectedAppointment(Appointment selectedAppointment) {
        this.selectedAppointment = selectedAppointment;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public AppointmentStatus[] getStatuses() {
        return AppointmentStatus.values();
    }
}
