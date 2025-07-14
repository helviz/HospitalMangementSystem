package org.example.dao;

import org.example.models.base.SoftDeletable;

import org.example.utilities.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class BaseDAO<T extends SoftDeletable> {
    private final Class<T> entityClass;

    protected BaseDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public Optional<T> getById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            T entity = session.get(entityClass, id);
            if (entity != null && !entity.isDelete()) {
                return Optional.of(entity);
            }
            return Optional.empty();
        } catch (Exception e) {
            System.out.println("Error fetching " + entityClass.getSimpleName() + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<T> getByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<T> query = session.createQuery(
                    "FROM " + entityClass.getSimpleName() + " WHERE (firstName LIKE :name OR lastName LIKE :name) AND deleted = false",
                    entityClass
            );
            query.setParameter("name", "%" + name + "%");
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Error searching by name: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<T> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<T> query = session.createQuery(
                    "FROM " + entityClass.getSimpleName() + " WHERE deleted = false",
                    entityClass
            );
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Error fetching all " + entityClass.getSimpleName() + ": " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public boolean saveToDB(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(entity);
            transaction.commit();
            System.out.println("Saving " + entityClass.getSimpleName() + ": " + entity.toString());
            return true;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Error saving " + entityClass.getSimpleName() + ": " + e.getMessage());
            return false;
        }
    }


    public void deleteByID(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            T entity = session.get(entityClass, id);
            if (entity != null) {
                entity.setDelete(true);
                session.update(entity);
                session.flush();
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Error soft deleting " + entityClass.getSimpleName() + ": " + e.getMessage());
        }
    }

    // Field update methods
    public void updateFirstNameField(Long id, String name) {}
    public void updateMiddleNameField(Long id, String name) {}
    public void updateLastNameField(Long id, String name) {}
    public void updateEmailField(Long id, String email) {}
    public void updateContactNumberField(Long id, String contactNumber) {}
    public void updateDateOfBirthField(Long id, LocalDate dateOfBirth) {}
}