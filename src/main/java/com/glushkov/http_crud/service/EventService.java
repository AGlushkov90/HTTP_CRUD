package com.glushkov.http_crud.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glushkov.http_crud.model.Event;
import com.glushkov.http_crud.model.File;
import com.glushkov.http_crud.repository.EventRepository;
import com.glushkov.http_crud.repository.impl.ORMCommonRepository;
import com.glushkov.http_crud.repository.impl.ORMEventRepositoryImpl;
import com.glushkov.http_crud.utils.MapperEntity;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EventService {
    private final EventRepository eventRepository = new ORMEventRepositoryImpl();
    private final ObjectMapper objectMapper = MapperEntity.getObjectMapper();

    public String getByID(Long id) throws JsonProcessingException {

        Transaction tx = null;
        try (Session session = ORMCommonRepository.getSession()) {
            tx = session.beginTransaction();
            Event event = eventRepository.getByID(id);
            setUnproxyEvent(event, session);
            String stringUsers = objectMapper.writeValueAsString(MapperEntity.convertToEventDto(event));
            tx.commit();
            return stringUsers;
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getAll() throws JsonProcessingException {
        Transaction tx = null;
        try (Session session = ORMCommonRepository.getSession()) {
            tx = session.beginTransaction();
            List<Event> events = eventRepository.getAll();
            for (Event event : events) {
                setUnproxyEvent(event, session);
            }
            String stringUsers = objectMapper.writeValueAsString(MapperEntity.convertToEventsDto(events));
            tx.commit();
            return stringUsers;
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String delete(Long id) throws JsonProcessingException {
        return objectMapper.writeValueAsString(String.valueOf(eventRepository.delete(id)));
    }

    private void setUnproxyEvent(Event event, Session session) {
        session.detach(event);
        event.setFile(Hibernate.unproxy(event.getFile(), File.class));
    }
}
