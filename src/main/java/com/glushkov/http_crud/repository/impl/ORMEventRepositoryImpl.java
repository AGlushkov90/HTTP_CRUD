package com.glushkov.http_crud.repository.impl;

import com.glushkov.http_crud.model.Event;
import com.glushkov.http_crud.repository.EventRepository;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ORMEventRepositoryImpl implements EventRepository {

    @Override
    public Event getByID(Long id) {
        try (Session session = ORMCommonRepository.getSession()) {
            return session.get(Event.class, id);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Event> getAll() {
        try (Session session = ORMCommonRepository.getSession()) {
            List<Event> events = session.createQuery("FROM Event").list();
            return events;
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
            Event event = session.load(Event.class, id);
            session.delete(event);
            tx.commit();
            return true;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();

        }
        return false;
    }

    @Override
    public Event save(Event itemToSave) {
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
    public Event edit(Event itemToUpdate) {
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

