package com.glushkov.http_crud.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glushkov.http_crud.model.Event;
import com.glushkov.http_crud.model.Status;
import com.glushkov.http_crud.model.User;
import com.glushkov.http_crud.repository.EventRepository;
import com.glushkov.http_crud.repository.UserRepository;
import com.glushkov.http_crud.repository.impl.ORMEventRepositoryImpl;
import com.glushkov.http_crud.repository.impl.ORMUserRepositoryImpl;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class EventController extends HttpServlet {
    private final EventRepository eventRepository  = new ORMEventRepositoryImpl();
    ObjectMapper objectMapper = new ObjectMapper();

    private final UserRepository userRepository = new ORMUserRepositoryImpl();

    {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    @Produces({MediaType.APPLICATION_JSON})
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        if (id == null) {
            out(resp, getAll());
        } else {
            out(resp, getByID(id));
        }
    }

    @Override
    @Produces({MediaType.APPLICATION_JSON})
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        if (id == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        out(resp, String.valueOf(eventRepository.delete(Long.parseLong(id))));
    }

    @Override
    @Produces({MediaType.APPLICATION_JSON})
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        User user = null;
        while ((line = reader.readLine()) != null) {
            if (line.contains("user")) {
                user = userRepository.getByID(1L);
                continue;
            }
            buffer.append(line);
        }
        String fromJson = buffer.toString();
        Event event = new Event();
        event.setCreated(Date.valueOf(LocalDate.now()));
        event.setStatus(Status.ACTIVE);
        event.setUser(user);
        out(resp, objectMapper.writeValueAsString(eventRepository.save(event)));
    }

    @Override
    @Produces({MediaType.APPLICATION_JSON})
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String id = req.getParameter("id");
        if (id == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        Event event = eventRepository.getByID(Long.parseLong(id));
        String fromJson = buffer.toString();
        System.out.println(fromJson);
        event.setUpdated(Date.valueOf(LocalDate.now()));
        out(resp, objectMapper.writeValueAsString(eventRepository.edit(event)));
    }

    private void out(HttpServletResponse resp, String jsonString) throws IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(jsonString);
        out.flush();
    }

    private String getByID(String id) throws JsonProcessingException {
        Event event = eventRepository.getByID(Long.parseLong(id));
        return objectMapper.writeValueAsString(event);
    }

    private String getAll() throws JsonProcessingException {
        List<Event> events = eventRepository.getAll();
        return objectMapper.writeValueAsString(events);
    }
}
