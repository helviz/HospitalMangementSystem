package org.example.services.Impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.example.dao.AppointmentDAO;
import org.example.enums.AppointmentStatus;
import org.example.models.entities.Appointment;
import org.example.services.AppointmentService;
import org.example.services.ServiceException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AppointmentServiceImpl extends BaseService implements AppointmentService {

    @Inject
    private AppointmentDAO appointmentDAO;

    public boolean createAppointment(Long patientId, Long doctorId, LocalDate appointmentDate, AppointmentStatus status) {
        isIdValid(patientId);
        isIdValid(doctorId);
        if (appointmentDate == null) {
            throw new ServiceException("Appointment date cannot be null");
        }
        return appointmentDAO.createAppointment(patientId, doctorId, appointmentDate, status);
    }

    public Optional<Appointment> getAppointmentById(Long appointmentId) {
        isIdValid(appointmentId);
        return appointmentDAO.getAppointmentById(appointmentId);
    }

    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        isIdValid(doctorId);
        return appointmentDAO.getAppointmentsByDoctor(doctorId);
    }

    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        isIdValid(patientId);
        return appointmentDAO.getAppointmentsByPatient(patientId);
    }

    public boolean updateAppointmentStatus(Long appointmentId, AppointmentStatus newStatus) {
        isIdValid(appointmentId);
        if (newStatus == null) {
            throw new ServiceException("Appointment status cannot be null");
        }
        return appointmentDAO.updateAppointmentStatus(appointmentId, newStatus);
    }

    public boolean cancelAppointment(Long appointmentId) {
        isIdValid(appointmentId);
        return appointmentDAO.cancelAppointment(appointmentId);
    }

    public List<Appointment> getAppointmentsByDoctorAndDateRange(Long doctorId, LocalDate startDate, LocalDate endDate) {
        isIdValid(doctorId);
        if (startDate == null || endDate == null) {
            throw new ServiceException("Start and end dates cannot be null");
        }
        if (endDate.isBefore(startDate)) {
            throw new ServiceException("End date cannot be before start date");
        }
        return appointmentDAO.getAppointmentsByDoctorAndDateRange(doctorId, startDate, endDate);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentDAO.getAll();
    }
}
