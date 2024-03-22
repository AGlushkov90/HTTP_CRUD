package com.glushkov.http_crud.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.glushkov.http_crud.model.Event;
import com.glushkov.http_crud.model.File;
import com.glushkov.http_crud.model.Status;
import com.glushkov.http_crud.repository.EventRepository;
import com.glushkov.http_crud.repository.impl.ORMFileRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    void whenGivenId_shouldReturnEvent() {
        Event event = new Event(1L, new File(1L, "first", "",
                Date.valueOf(LocalDate.now()), null, Status.ACTIVE), Date.valueOf(LocalDate.now()), null, Status.ACTIVE);
        when(eventRepository.getByID(event.getId())).thenReturn(event);
        Event eventResult = eventService.getByID(event.getId());
        assertEquals(event, eventResult);
        verify(eventRepository).getByID(event.getId());
    }
    @Test
    void whenGivenId_shouldReturnNull() {
        Event event = new Event(1L, new File(1L, "first", "",
                Date.valueOf(LocalDate.now()), null, Status.ACTIVE), Date.valueOf(LocalDate.now()), null, Status.ACTIVE);
        when(eventRepository.getByID(event.getId())).thenReturn(null);
        Event eventResult = eventService.getByID(event.getId());
        assertNull(eventResult);
        verify(eventRepository).getByID(event.getId());
    }

    @Test
    void shouldReturnAllEvents() throws JsonProcessingException {
        List<Event> events = Arrays.asList(
                new Event(1L, new File(1L, "first", "",
                        Date.valueOf(LocalDate.now()), null, Status.ACTIVE), Date.valueOf(LocalDate.now()), null, Status.ACTIVE),
                new Event(1L, new File(1L, "first", "",
                        Date.valueOf(LocalDate.now()), null, Status.ACTIVE), Date.valueOf(LocalDate.now()), null, Status.ACTIVE));
        when(eventRepository.getAll()).thenReturn(events);
        List<Event> postsResult = (List<Event>) eventService.getAll();
        assertEquals(events, postsResult);
        verify(eventRepository).getAll();
    }

    @Test
    void whenGivenId_shouldDeleteEvent_ifFound() {
        Event event = new Event(1L, new File(1L, "first", "",
                Date.valueOf(LocalDate.now()), null, Status.ACTIVE), Date.valueOf(LocalDate.now()), null, Status.ACTIVE);
        when(eventRepository.delete(ArgumentMatchers.any(Long.class))).thenReturn(true);
        boolean deleted = eventService.delete(event.getId());
        assertTrue(deleted);
        verify(eventRepository).delete(event.getId());
    }
}