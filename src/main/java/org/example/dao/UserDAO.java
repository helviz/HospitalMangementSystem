package org.example.dao;

import jakarta.enterprise.inject.Model;
import org.example.models.entities.Staff;
import org.example.models.entities.User;
import org.example.utilities.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.Serializable;
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




}
