package org.example.services;

import org.example.dao.PatientDAO;
import org.example.utilies.HibernateUtil;
import org.example.models.entities.Patient;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PatientService extends PersonelService {
    private final PatientDAO patientDAO;

    public PatientService() {
        this.patientDAO = new PatientDAO();
    }


    public Patient savePatient(Patient person) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(person);
        tx.commit();
        session.close();
        return person;
    }

    // Method to retrieve all patients
    public List<Patient> getAllPatients() {
        return patientDAO.getAll();
    }

    // Method to find a patient by ID
    public Patient findPatientById(Long id) {
        return patientDAO.getByID(id);
    }
}
