package org.example.dao;

import org.example.models.entities.User;
import org.example.utilities.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class AuthenticationDAO {

    public User authenticateUser(String inputEmail, String inputPassword){
        try(Session session = HibernateUtil.getSessionFactory().getCurrentSession()){
            Transaction tx = session.beginTransaction();
            Query<User> query = session.createQuery("FROM User AS user WHERE user.email=:email AND user.password=:password", User.class);
            query.setParameter("email", inputEmail);
            query.setParameter("password", inputPassword);
            query.setMaxResults(1);

            User user = query.getSingleResult();
            tx.commit();

            if (user != null ) {
                return user;
            } else {
                return null;
            }
        } catch (Exception e) {
        System.out.println("Error AuthenticatingUser: " + e.getMessage());
        return null;
    }

    }

}
