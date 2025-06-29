package org.example.services;

import org.example.dao.StaffDAO;
import org.example.utilities.HibernateUtil;
import org.example.models.entities.Staff;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class StaffService extends PersonelService {
    private final StaffDAO staffDAO;


    public StaffService() {
        this.staffDAO = new StaffDAO();
    }

    public Staff saveStaff(Staff person) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.persist(person);
        tx.commit();
        session.close();
        return person;
    }

    // Method to retrieve all staffs
    public List<Staff> getAllStaffs() {
        return staffDAO.getAll();
    }

    // Method to find a staff by ID
    public Optional<Staff> findStaffById(Long id) {
        return staffDAO.getById(id);
    }

}
