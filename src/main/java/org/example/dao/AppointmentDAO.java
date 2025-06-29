package org.example.dao;

import jakarta.enterprise.inject.Model;
import org.example.models.entities.Appointment;
import org.example.models.entities.Doctor;
import org.example.models.entities.Patient;
import org.example.enums.AppointmentStatus;

import org.example.utilities.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Model
public class AppointmentDAO extends BaseDAO<Appointment> implements Serializable {

    public AppointmentDAO() {
        super(Appointment.class);
    }
    /**
     * Create a new appointment
     */
    public boolean createAppointment(Long patientId, Long doctorId, LocalDate appointmentDate, AppointmentStatus status) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Fetch patient and doctor
            Patient patient = session.get(Patient.class, patientId);
            Doctor doctor = session.get(Doctor.class, doctorId);

            if (patient == null || doctor == null) {
                System.out.println("Patient or Doctor not found");
                return false;
            }

            // Check if doctor is available at that time
            if (isDoctorAvailable(session, doctorId, appointmentDate)) {
                Appointment appointment = new Appointment();
                appointment.setAppointmentDateTime(appointmentDate);
                appointment.setStatus(status != null ? status : AppointmentStatus.SCHEDULED);
                appointment.setPatient(patient);
                appointment.setDoctor(doctor);

                session.save(appointment);
                transaction.commit();

                System.out.println("Appointment created successfully");
                return true;
            } else {
                System.out.println("Doctor is not available at the specified time");
                return false;
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Error creating appointment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all appointments for a specific doctor
     */
    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery(
                    "FROM Appointment a WHERE a.doctor.doctorID = :doctorId AND a.deleted = false ORDER BY a.appointmentDateTime",
                    Appointment.class
            );
            query.setParameter("doctorId", doctorId);
            return query.getResultList();

        } catch (Exception e) {
            System.out.println("Error fetching appointments for doctor: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Get all appointments for a specific patient
     */
    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery(
                    "FROM Appointment a WHERE a.patient.patientID = :patientId AND a.deleted = false ORDER BY a.appointmentDateTime",
                    Appointment.class
            );
            query.setParameter("patientId", patientId);
            return query.getResultList();

        } catch (Exception e) {
            System.out.println("Error fetching appointments for patient: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Update appointment status
     */
    public boolean updateAppointmentStatus(Long appointmentId, AppointmentStatus newStatus) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Appointment appointment = session.get(Appointment.class, appointmentId);
            if (appointment != null && !appointment.isDelete()) {
                appointment.setStatus(newStatus);
                session.update(appointment);
                transaction.commit();

                System.out.println("Appointment status updated successfully");
                return true;
            } else {
                System.out.println("Appointment not found or deleted");
                return false;
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Error updating appointment status: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get appointment by ID
     */
    public Optional<Appointment> getAppointmentById(Long appointmentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Appointment appointment = session.get(Appointment.class, appointmentId);
            if (appointment != null && !appointment.isDelete()) {
                return Optional.of(appointment);
            }
            return Optional.empty();

        } catch (Exception e) {
            System.out.println("Error fetching appointment: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Cancel appointment (soft delete)
     */
    public boolean cancelAppointment(Long appointmentId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Appointment appointment = session.get(Appointment.class, appointmentId);
            if (appointment != null && !appointment.isDelete()) {
                appointment.setStatus(AppointmentStatus.CANCELLED);
                appointment.setDelete(true);
                session.update(appointment);
                transaction.commit();

                System.out.println("Appointment cancelled successfully");
                return true;
            } else {
                System.out.println("Appointment not found or already cancelled");
                return false;
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Error cancelling appointment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get appointments by date range for a doctor
     */
    public List<Appointment> getAppointmentsByDoctorAndDateRange(Long doctorId, LocalDate startDate, LocalDate endDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery(
                    "FROM Appointment a WHERE a.doctor.doctorID = :doctorId " +
                            "AND a.appointmentDateTime BETWEEN :startDate AND :endDate " +
                            "AND a.deleted = false ORDER BY a.appointmentDateTime",
                    Appointment.class
            );
            query.setParameter("doctorId", doctorId);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.getResultList();

        } catch (Exception e) {
            System.out.println("Error fetching appointments by date range: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Check if doctor is available at specified date/time
     */
    private boolean isDoctorAvailable(Session session, Long doctorId, LocalDate appointmentDate) {
        try {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(a) FROM Appointment a WHERE a.doctor.doctorID = :doctorId " +
                            "AND a.appointmentDateTime = :date AND a.status != 'CANCELLED' AND a.deleted = false",
                    Long.class
            );
            query.setParameter("doctorId", doctorId);
            query.setParameter("date", appointmentDate);

            Long count = query.getSingleResult();
            return count == 0;

        } catch (Exception e) {
            System.out.println("Error checking doctor availability: " + e.getMessage());
            return false;
        }
    }
}