package org.example.dao;

import jakarta.enterprise.inject.Model;
import org.example.models.entities.Doctor;
import org.example.models.entities.Staff;
import org.example.models.entities.User;
import org.example.utilities.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.Serializable;

@Model
public class AuthenticationDAO implements Serializable {

    public User authenticateUser(String inputEmail, String inputPassword) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            tx = session.beginTransaction();

            // Normalize email input
            String email = inputEmail.trim().toLowerCase();

            User user = null;

            // Try authenticating as Doctor
            Query<Doctor> doctorQuery = session.createQuery(
                    "FROM Doctor d WHERE lower(d.email) = :email AND d.deleted = false", Doctor.class);
            doctorQuery.setParameter("email", email);
            Doctor doctor = doctorQuery.uniqueResult();
            if (doctor != null) {
                user = doctor.getUser();
            } else {
                // If not a doctor, try authenticating as Staff
                Query<Staff> staffQuery = session.createQuery(
                        "FROM Staff s WHERE lower(s.email) = :email AND s.deleted = false", Staff.class);
                staffQuery.setParameter("email", email);
                Staff staff = staffQuery.uniqueResult();
                if (staff != null) {
                    user = staff.getUser();
                }
            }

            tx.commit();

            if (user != null && !user.isDelete()) {
                // Replace with real password hash comparison in production
                if (user.getPassword().equals(inputPassword)) {
                    return user;
                }
            }

            return null; // invalid credentials
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println("Error authenticating user: " + e.getMessage());
            return null;
        }
    }
}
