package org.example.dao;

import org.example.utilities.HibernateUtil;
import org.example.models.base.SoftDeletable;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;


import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public abstract class BaseDAO<T extends SoftDeletable>{
    private final Class<T> ObjectClass;
    private String table ;


    protected BaseDAO(Class<T> ObjectClass){
        this.ObjectClass = ObjectClass;
        this.table = ObjectClass.getSimpleName();
    }

//    retrieve from DB
    public T getByID(Long ID){
        try(Session session = HibernateUtil.getSessionFactory().getCurrentSession())
        {
            return session.get(ObjectClass, ID);
        } catch (HibernateException e) {
            System.out.println("Error: " + e.getMessage());
            return  null;
        }
    }
    public List<T> getByName(String name){
        try(Session session = HibernateUtil.getSessionFactory().getCurrentSession())
        {
            Query<T> query = session.createQuery("From " + table + " as t WHERE t.firstName=:name or t.middleName=:name or t.lastName =:name" );
            query.setParameter("name", name);
            List list = query.getResultList();
            return list;
        } catch (HibernateException e){
            System.out.println("Error in getByName:" + e.getMessage());
            return Collections.emptyList();
        }
    }
    public List<T> getAll (){
        try(Session session = HibernateUtil.getSessionFactory().getCurrentSession())
        {
            Query<T> query = session.createQuery("FROM " + table, ObjectClass);
            List list = query.getResultList();
            return list;
        } catch (HibernateException e){
            System.out.println("Error in getAll:" + e.getMessage());
            return Collections.emptyList();
        }
    }

//    Update

//    SOFT DELETE
    public void deleteByID(Long ID){
        try(Session session = HibernateUtil.getSessionFactory().getCurrentSession()){
            session.beginTransaction();
            T obj = session.get(ObjectClass, ID);
            if (obj != null) {
                obj.setDelete(true); // Now compiler accepts this
                session.update(obj);
            }
            session.getTransaction().commit();



        } catch (HibernateException e){
            System.out.println("Error in deleting:" + e.getMessage());
        }
    }

//    saving
    public void saveToDB(T obj){
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()){
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(obj);
            tx.commit();
        } catch (HibernateException e){
            System.out.println("Error in saving:" + e.getMessage());
        }
    }

    //    Updates
    public void updateFirstNameField(Long id, String name){}
    public void updateMiddleNameField(Long id, String name){}
    public void updateLastNameField(Long Id, String name){}
    public void updateEmailField(Long id, String email){}
    public void updateContactNumberField(Long id, String contactNumber){}
    public void updateDateOfBirthField(Long id, LocalDate dateOfBirth){}


}
