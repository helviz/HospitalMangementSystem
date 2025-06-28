package org.example.utilities;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Provide access to the singleton SessionFactory
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    // Properly close the SessionFactory
    public static void shutdown() {
        getSessionFactory().close();
    }
}
