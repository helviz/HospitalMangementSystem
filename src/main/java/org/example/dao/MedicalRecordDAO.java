package org.example.dao;

import org.example.models.entities.MedicalRecord;
import org.example.models.entities.Doctor;
import org.example.models.entities.Patient;

import org.example.utilities.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MedicalRecordDAO {

    /**
     * Create a new medical record
     */
    public boolean createMedicalRecord(Long patientId, Long doctorId, String diagnosis, LocalDate recordDate) {
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

            if (diagnosis == null || diagnosis.trim().isEmpty()) {
                System.out.println("Diagnosis cannot be empty");
                return false;
            }

            MedicalRecord record = new MedicalRecord();
            record.setPatient(patient);
            record.setDoctor(doctor);
            record.setDiagnosis(diagnosis.trim());
            record.setRecordDate(recordDate != null ? recordDate : LocalDate.now());

            session.save(record);
            transaction.commit();

            System.out.println("Medical record created successfully");
            return true;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Error creating medical record: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all medical records for a specific patient
     */
    public List<MedicalRecord> getMedicalRecordsByPatient(Long patientId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<MedicalRecord> query = session.createQuery(
                    "FROM MedicalRecord mr WHERE mr.patient.patientID = :patientId AND mr.deleted = false ORDER BY mr.recordDate DESC",
                    MedicalRecord.class
            );
            query.setParameter("patientId", patientId);
            return query.getResultList();

        } catch (Exception e) {
            System.out.println("Error fetching medical records for patient: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Get all medical records created by a specific doctor
     */
    public List<MedicalRecord> getMedicalRecordsByDoctor(Long doctorId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<MedicalRecord> query = session.createQuery(
                    "FROM MedicalRecord mr WHERE mr.doctor.doctorID = :doctorId AND mr.deleted = false ORDER BY mr.recordDate DESC",
                    MedicalRecord.class
            );
            query.setParameter("doctorId", doctorId);
            return query.getResultList();

        } catch (Exception e) {
            System.out.println("Error fetching medical records for doctor: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Get medical record by ID
     */
    public Optional<MedicalRecord> getMedicalRecordById(Long recordId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            MedicalRecord record = session.get(MedicalRecord.class, recordId);
            if (record != null && !record.isDelete()) {
                return Optional.of(record);
            }
            return Optional.empty();

        } catch (Exception e) {
            System.out.println("Error fetching medical record: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Update medical record diagnosis
     */
    public boolean updateMedicalRecord(Long recordId, String newDiagnosis) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            MedicalRecord record = session.get(MedicalRecord.class, recordId);
            if (record != null && !record.isDelete()) {
                if (newDiagnosis != null && !newDiagnosis.trim().isEmpty()) {
                    record.setDiagnosis(newDiagnosis.trim());
                    session.update(record);
                    transaction.commit();

                    System.out.println("Medical record updated successfully");
                    return true;
                } else {
                    System.out.println("Diagnosis cannot be empty");
                    return false;
                }
            } else {
                System.out.println("Medical record not found or deleted");
                return false;
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Error updating medical record: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete medical record (soft delete)
     */
    public boolean deleteMedicalRecord(Long recordId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            MedicalRecord record = session.get(MedicalRecord.class, recordId);
            if (record != null && !record.isDelete()) {
                record.setDelete(true);
                session.update(record);
                transaction.commit();

                System.out.println("Medical record deleted successfully");
                return true;
            } else {
                System.out.println("Medical record not found or already deleted");
                return false;
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Error deleting medical record: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get medical records by date range for a patient
     */
    public List<MedicalRecord> getMedicalRecordsByPatientAndDateRange(Long patientId, LocalDate startDate, LocalDate endDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<MedicalRecord> query = session.createQuery(
                    "FROM MedicalRecord mr WHERE mr.patient.patientID = :patientId " +
                            "AND mr.recordDate BETWEEN :startDate AND :endDate " +
                            "AND mr.deleted = false ORDER BY mr.recordDate DESC",
                    MedicalRecord.class
            );
            query.setParameter("patientId", patientId);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.getResultList();

        } catch (Exception e) {
            System.out.println("Error fetching medical records by date range: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Search medical records by diagnosis keyword
     */
    public List<MedicalRecord> searchMedicalRecordsByDiagnosis(String keyword) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<MedicalRecord> query = session.createQuery(
                    "FROM MedicalRecord mr WHERE LOWER(mr.diagnosis) LIKE LOWER(:keyword) " +
                            "AND mr.deleted = false ORDER BY mr.recordDate DESC",
                    MedicalRecord.class
            );
            query.setParameter("keyword", "%" + keyword.trim() + "%");
            return query.getResultList();

        } catch (Exception e) {
            System.out.println("Error searching medical records: " + e.getMessage());
            return List.of();
        }
    }
}