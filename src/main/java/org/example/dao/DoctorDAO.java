package org.example.dao;
import jakarta.enterprise.inject.Model;
import org.example.enums.Speciality;
import org.example.models.entities.Doctor;

import org.example.models.entities.Doctor;
import org.example.utilities.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;


import java.time.LocalDate;
@Model
public class DoctorDAO extends BaseDAO<Doctor>  implements Serializable {
    public DoctorDAO() {
        super(Doctor.class);
    }
    private void updateDoctorField(Long id, Consumer<Doctor> updater) {
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            Doctor doctor = session.get(Doctor.class, id);
            if (doctor != null) {
                updater.accept(doctor); // Apply the field update
                session.update(doctor);
                tx.commit();
            }
        }
    }

//    get doctor by speciality
    public List<Doctor> getDoctorsBySpeciality(Speciality speciality) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Doctor> query = session.createQuery(
                    "FROM Doctor d WHERE d.speciality = :speciality AND d.deleted = false ORDER BY d.lastName, d.firstName",
                    Doctor.class
            );
            query.setParameter("speciality", speciality);
            return query.getResultList();

        } catch (Exception e) {
            System.out.println("Error fetching doctors by speciality: " + e.getMessage());
            return List.of();
        }
    }

    public void updateDoctor(Doctor updatedDoctor) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Doctor existingDoctor = session.get(Doctor.class, updatedDoctor.getDoctorID());
            if (existingDoctor != null) {
                // Update all fields at once
                existingDoctor.setFirstName(updatedDoctor.getFirstName());
                existingDoctor.setMiddleName(updatedDoctor.getMiddleName());
                existingDoctor.setLastName(updatedDoctor.getLastName());
                existingDoctor.setEmail(updatedDoctor.getEmail());
                existingDoctor.setContactNumber(updatedDoctor.getContactNumber());
                existingDoctor.setDateOfBirth(updatedDoctor.getDateOfBirth());
                existingDoctor.getUser().setPassword(updatedDoctor.getUser().getPassword());

                session.merge(updatedDoctor);

                tx.commit();
            } else {
                tx.rollback();
                throw new RuntimeException("Doctor not found with ID: " + updatedDoctor.getDoctorID());
            }
        } catch (Exception e) {
            System.out.println("Error updating patient: " + e.getMessage());
            throw new RuntimeException("Failed to update patient", e);
        }
    }



    @Override
    public void updateFirstNameField(Long id, String name) {
        updateDoctorField(id, doctor -> doctor.setFirstName(name));
    }

    @Override
    public void updateMiddleNameField(Long id, String name) {
        updateDoctorField(id, doctor -> doctor.setMiddleName(name));
    }

    @Override
    public void updateLastNameField(Long id, String name) {
        updateDoctorField(id, doctor -> doctor.setLastName(name));
    }

    @Override
    public void updateEmailField(Long id, String email) {
        updateDoctorField(id, doctor -> doctor.setEmail(email));
    }

    @Override
    public void updateContactNumberField(Long id, String contactNumber) {
        updateDoctorField(id, doctor -> doctor.setContactNumber(contactNumber));
    }

    @Override
    public void updateDateOfBirthField(Long id, LocalDate dateOfBirth) {
        updateDoctorField(id, doctor -> doctor.setDateOfBirth(dateOfBirth));
    }
}
