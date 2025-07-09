package org.example.managedbeans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.enums.AppointmentStatus;
import org.example.services.AppointmentService;



import java.io.Serializable;
import java.time.LocalDate;

@Named("appointmentFormBean")
@ViewScoped
public class AppointmentFormBean implements Serializable {

    private Long patientId;
    private Long doctorId;
    private LocalDate appointmentDate;
    private AppointmentStatus status;

    @Inject
    private AppointmentService appointmentService;

    @PostConstruct
    public void init() {
        appointmentDate = LocalDate.now();
        status = AppointmentStatus.SCHEDULED; // default
    }

    public void saveAppointment() {
        if (patientId != null && doctorId != null && appointmentDate != null && status != null) {
            boolean success = appointmentService.createAppointment(
                    patientId,
                    doctorId,
                    appointmentDate,
                    status
            );
            if (success) {
                resetForm();
            }
        }
    }

    private void resetForm() {
        patientId = null;
        doctorId = null;
        appointmentDate = LocalDate.now();
        status = AppointmentStatus.SCHEDULED;
    }

    // Getters and setters

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public AppointmentStatus[] getStatuses() {
        return AppointmentStatus.values();
    }
}
