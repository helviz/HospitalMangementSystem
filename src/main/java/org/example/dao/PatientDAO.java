package org.example.dao;

import jakarta.enterprise.inject.Model;
import org.example.models.entities.Patient;
import org.example.utilities.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;


import java.time.LocalDate;
@Model
public class PatientDAO extends BaseDAO<Patient> implements Serializable {
    public PatientDAO(){
        super(Patient.class);
    }

    
    private void updatePatientField(Long id, Consumer<Patient> updater) {
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            Patient patient = session.get(Patient.class, id);
            if (patient != null) {
                updater.accept(patient); // Apply the field update
                session.update(patient);
                tx.commit();
            }
        }
    }

    public List<Patient> searchPatients(String keyword) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Patient> query = session.createQuery(
                    "FROM Patient WHERE (firstName LIKE :keyword OR lastName LIKE :keyword OR email LIKE :keyword) " +
                            "AND deleted = false ORDER BY lastName, firstName",
                    Patient.class
            );
            query.setParameter("keyword", "%" + keyword + "%");
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Error searching patients: " + e.getMessage());
            return List.of();
        }
    }


    public void updatePatient(Patient updatedPatient) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Patient existingPatient = session.get(Patient.class, updatedPatient.getPatientID());
            if (existingPatient != null) {
                // Update all fields at once
                existingPatient.setFirstName(updatedPatient.getFirstName());
                existingPatient.setMiddleName(updatedPatient.getMiddleName());
                existingPatient.setLastName(updatedPatient.getLastName());
                existingPatient.setEmail(updatedPatient.getEmail());
                existingPatient.setContactNumber(updatedPatient.getContactNumber());
                existingPatient.setDateOfBirth(updatedPatient.getDateOfBirth());

                session.merge(updatedPatient);

                tx.commit();
            } else {
                tx.rollback();
                throw new RuntimeException("Patient not found with ID: " + updatedPatient.getPatientID());
            }
        } catch (Exception e) {
            System.out.println("Error updating patient: " + e.getMessage());
            throw new RuntimeException("Failed to update patient", e);
        }
    }

    @Override
    public void updateFirstNameField(Long id, String name) {
        updatePatientField(id, patient -> patient.setFirstName(name));
    }

    @Override
    public void updateMiddleNameField(Long id, String name) {
        updatePatientField(id, patient -> patient.setMiddleName(name));
    }

    @Override
    public void updateLastNameField(Long id, String name) {
        updatePatientField(id, patient -> patient.setLastName(name));
    }

    @Override
    public void updateEmailField(Long id, String email) {
        updatePatientField(id, patient -> patient.setEmail(email));
    }

    @Override
    public void updateContactNumberField(Long id, String contactNumber) {
        updatePatientField(id, patient -> patient.setContactNumber(contactNumber));
    }

    @Override
    public void updateDateOfBirthField(Long id, LocalDate dateOfBirth) {
        updatePatientField(id, patient -> patient.setDateOfBirth(dateOfBirth));
    }




}
