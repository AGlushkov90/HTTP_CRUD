package com.glushkov.http_crud.repository.impl;

import com.glushkov.http_crud.model.Event;
import com.glushkov.http_crud.model.File;
import com.glushkov.http_crud.model.User;
import com.glushkov.http_crud.repository.UserRepository;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ORMUserRepositoryImpl implements UserRepository {

    @Override
    public User getByID(Long id) {
        try (Session session = ORMCommonRepository.getSession()) {
            User user = session.get(User.class, id);
            setUnproxyFile(user, session);
            return user;
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        try (Session session = ORMCommonRepository.getSession()) {
            return (List<User>) session.createQuery("FROM User").stream().
                    peek(u -> setUnproxyFile((User) u, session)).collect(Collectors.toList());
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
            itemToSave = session.merge(itemToSave);
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

    private void setUnproxyFile(User user, Session session) {
        session.detach(user);
        Set<Event> events = user.getEvents();
        for (Event event : events) {
            event.setFile(Hibernate.unproxy(event.getFile(), File.class));
        }
    }
}

