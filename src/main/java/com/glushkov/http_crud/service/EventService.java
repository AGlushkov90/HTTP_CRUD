package com.glushkov.http_crud.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.glushkov.http_crud.model.Event;
import com.glushkov.http_crud.repository.EventRepository;
import com.glushkov.http_crud.repository.impl.ORMEventRepositoryImpl;

import java.util.List;

public class EventService {
    public EventRepository eventRepository = new ORMEventRepositoryImpl();
    public EventService() {
    }

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event getByID(Long id) {
        return eventRepository.getByID(id);
    }

    public List<Event> getAll() throws JsonProcessingException {
        return eventRepository.getAll();
    }

    public Boolean delete(Long id) {
        return eventRepository.delete(id);
    }
}
