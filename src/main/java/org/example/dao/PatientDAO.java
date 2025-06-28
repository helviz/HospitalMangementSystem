package org.example.dao;

import org.example.models.entities.Patient;
import org.example.utilities.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.function.Consumer;


import java.time.LocalDate;

public class PatientDAO extends BaseDAO<Patient>{
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
