package org.example.services;

import org.example.dao.DoctorDAO;
import org.example.utilies.HibernateUtil;
import org.example.models.entities.Doctor;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class DoctorService extends PersonelService{

    private final DoctorDAO doctorDAO;

    public DoctorService() {
        this.doctorDAO = new DoctorDAO();
    }

    public Doctor saveDoctor(Doctor person) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.persist(person);
        tx.commit();
        session.close();
        return person;
    }



    // Method to retrieve all doctors
    public List<Doctor> getAllDoctors() {
        return doctorDAO.getAll();
    }

    // Method to find a doctor by ID
    public Doctor findDoctorById(Long id) {
        return doctorDAO.getByID(id);
    }

}
