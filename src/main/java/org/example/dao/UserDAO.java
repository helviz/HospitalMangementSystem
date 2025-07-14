package org.example.dao;

import jakarta.enterprise.inject.Model;
import org.example.models.entities.Staff;
import org.example.models.entities.User;
import org.example.utilities.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Model
public class UserDAO  extends BaseDAO<User> implements Serializable {
    public UserDAO(){
        super(User.class);
    }


    public void updatePasswordField(Long id, String password) {
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                user.setPassword(password);
                session.update(user);
                tx.commit();
            }
        }
    }

    public boolean checkEmailUniqueness(String email) {
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            Transaction tx = session.beginTransaction();

            // Query all non-deleted users
            List<User> users = session.createQuery(
                            "FROM User u WHERE u.deleted = false", User.class)
                    .getResultList();

            // Extract emails and check match
            boolean exists = users.stream()
                    .map(User::getEmail)
                    .filter(Objects::nonNull)
                    .anyMatch(e -> e.equalsIgnoreCase(email));

            tx.commit();
            return !exists; // Return true if unique
        }
    }

    public Optional<User> findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            Transaction tx = session.beginTransaction();

            String hql = """
                            SELECT u
                            FROM User u
                            JOIN u.doctor d
                            WHERE d.email = :email
                            AND u.deleted = false
                            """;

            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("email", email);

            User user = query.uniqueResult();

            tx.commit();
            return Optional.ofNullable(user);
        }
    }






}
