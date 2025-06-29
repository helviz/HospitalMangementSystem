package org.example.managedBeans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.dao.AppointmentDAO;
import org.example.dao.DoctorDAO;
import org.example.dao.PatientDAO;
import org.example.enums.AppointmentStatus;
import org.example.models.entities.Appointment;
import org.example.models.entities.Doctor;
import org.example.models.entities.Patient;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Named
@SessionScoped
public class AppointmentBean implements Serializable {
    private Appointment appointment;
    private List<Appointment> appointments;
    private List<Appointment> filteredAppointments;
    private List<Patient> patients;
    private List<Doctor> doctors;
    private LocalDate selectedDate;
    private AppointmentStatus selectedStatus;

    @Inject
    private AppointmentDAO appointmentDAO;

    @Inject
    private PatientDAO patientDAO;

    @Inject
    private DoctorDAO doctorDAO;

    @PostConstruct
    public void init() {
        appointment = new Appointment();
        appointments = appointmentDAO.getAll();
        filteredAppointments = appointments;
        patients = patientDAO.getAll();
        doctors = doctorDAO.getAll();
    }

    public void create() {
        if (appointmentDAO.createAppointment(
                appointment.getPatient().getPatientID(),
                appointment.getDoctor().getDoctorID(),
                appointment.getAppointmentDateTime(),
                appointment.getStatus())) {

            appointments = appointmentDAO.getAll();
            filteredAppointments = appointments;
            appointment = new Appointment();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Appointment created successfully"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to create appointment", null));
        }
    }

    public void updateStatus(Long id, AppointmentStatus status) {
        if (appointmentDAO.updateAppointmentStatus(id, status)) {
            appointments = appointmentDAO.getAll();
            filteredAppointments = appointments;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Appointment status updated"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to update status", null));
        }
    }

    public void cancel(Long id) {
        if (appointmentDAO.cancelAppointment(id)) {
            appointments = appointmentDAO.getAll();
            filteredAppointments = appointments;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Appointment cancelled"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to cancel appointment", null));
        }
    }

    public void loadAppointment(Long id) {
        appointment = appointmentDAO.getAppointmentById(id).orElse(new Appointment());
        if (appointment.getAppointmentID() == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Appointment not found", null));
        }
    }

    public void filterByDate() {
        if (selectedDate != null) {
            filteredAppointments = appointments.stream()
                    .filter(a -> a.getAppointmentDateTime().equals(selectedDate))
                    .toList();
        } else {
            filteredAppointments = appointments;
        }
    }

    public void filterByStatus() {
        if (selectedStatus != null) {
            filteredAppointments = appointments.stream()
                    .filter(a -> a.getStatus() == selectedStatus)
                    .toList();
        } else {
            filteredAppointments = appointments;
        }
    }

    public void resetFilters() {
        selectedDate = null;
        selectedStatus = null;
        filteredAppointments = appointments;
    }

    public List<Appointment> getAppointmentsForDoctor(Long doctorId) {
        return appointmentDAO.getAppointmentsByDoctor(doctorId);
    }

    public List<Appointment> getAppointmentsForPatient(Long patientId) {
        return appointmentDAO.getAppointmentsByPatient(patientId);
    }

    // Getters and Setters
    public Appointment getAppointment() { return appointment; }
    public void setAppointment(Appointment appointment) { this.appointment = appointment; }
    public List<Appointment> getAppointments() { return appointments; }
    public List<Appointment> getFilteredAppointments() { return filteredAppointments; }
    public void setFilteredAppointments(List<Appointment> filteredAppointments) {
        this.filteredAppointments = filteredAppointments;
    }
    public List<Patient> getPatients() { return patients; }
    public List<Doctor> getDoctors() { return doctors; }
    public LocalDate getSelectedDate() { return selectedDate; }
    public void setSelectedDate(LocalDate selectedDate) { this.selectedDate = selectedDate; }
    public AppointmentStatus getSelectedStatus() { return selectedStatus; }
    public void setSelectedStatus(AppointmentStatus selectedStatus) { this.selectedStatus = selectedStatus; }
    public AppointmentStatus[] getAppointmentStatuses() { return AppointmentStatus.values(); }
}