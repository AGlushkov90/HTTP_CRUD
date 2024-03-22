package com.glushkov.http_crud.repository.impl;

import com.glushkov.http_crud.model.Event;
import com.glushkov.http_crud.model.File;
import com.glushkov.http_crud.repository.EventRepository;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.stream.Collectors;

public class ORMEventRepositoryImpl implements EventRepository {

    @Override
    public Event getByID(Long id) {
        try (Session session = ORMCommonRepository.getSession()) {
            Event event = session.get(Event.class, id);
            setUnproxyEvent(event, session);
            return event;
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Event> getAll() {
        try (Session session = ORMCommonRepository.getSession()) {
            return (List<Event>) session.createQuery("FROM Event").stream()
                    .peek(e -> setUnproxyEvent((Event) e, session))
                    .collect(Collectors.toList());
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

    private void setUnproxyEvent(Event event, Session session) {
        session.detach(event);
        event.setFile(Hibernate.unproxy(event.getFile(), File.class));
    }
}



