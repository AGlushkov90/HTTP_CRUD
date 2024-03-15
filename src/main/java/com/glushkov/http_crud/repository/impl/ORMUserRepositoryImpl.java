package com.glushkov.http_crud.repository.impl;

import com.glushkov.http_crud.model.User;
import com.glushkov.http_crud.repository.UserRepository;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ORMUserRepositoryImpl implements UserRepository {

    @Override
    public User getByID(Long id) {
        try (Session session = ORMCommonRepository.getSession()) {
            return session.get(User.class, id);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        try (Session session = ORMCommonRepository.getSession()) {
            List<User> users = session.createQuery("FROM User").list();
            return users;
        } catch (HibernateException e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        Transaction tx = null;
        try (Session session = ORMCommonRepository.getSession()) {
            tx = session.beginTransaction();
            User user = session.load(User.class, id);
            session.delete(user);
            tx.commit();
            return true;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();

        }
        return false;
    }

    @Override
    public User save(User itemToSave) {
        Transaction tx = null;
        try (Session session = ORMCommonRepository.getSession()) {
            tx = session.beginTransaction();
            session.merge(itemToSave);
            tx.commit();
            return itemToSave;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User edit(User itemToUpdate) {
        Transaction tx = null;
        try (Session session = ORMCommonRepository.getSession()) {
            tx = session.beginTransaction();
            session.update(itemToUpdate);
            tx.commit();
            return itemToUpdate;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();

        }
        return null;
    }
}

