//package org.example.services;
//
//import org.example.utilies.HibernateUtil;
//import org.example.models.entities.Role;
//import org.hibernate.Session;
//import org.hibernate.Transaction;
//
//public class RoleService {
//
//    public boolean isRoleValid(String role){
//        return role.matches("[a-zA-Z ]+");
//    }
//
//    public Role saveRole(Role role) {
//        Session session = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = session.beginTransaction();
//        session.persist(role);
//        tx.commit();
//        session.close();
//        return role;
//    }
//}
