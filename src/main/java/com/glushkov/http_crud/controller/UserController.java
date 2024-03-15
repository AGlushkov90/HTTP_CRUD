package com.glushkov.http_crud.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glushkov.http_crud.model.User;
import com.glushkov.http_crud.model.Status;
import com.glushkov.http_crud.repository.UserRepository;
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

public class UserController extends HttpServlet {
    private final UserRepository userRepository  = new ORMUserRepositoryImpl();
    ObjectMapper objectMapper = new ObjectMapper();

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
        out(resp, String.valueOf(userRepository.delete(Long.parseLong(id))));
    }

    @Override
    @Produces({MediaType.APPLICATION_JSON})
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String fromJson = buffer.toString();
        System.out.println(fromJson);
        User user = objectMapper.readValue(fromJson, User.class);
        user.setCreated(Date.valueOf(LocalDate.now()));
        user.setStatus(Status.ACTIVE);
        out(resp, objectMapper.writeValueAsString(userRepository.save(user)));
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
        User user = userRepository.getByID(Long.parseLong(id));
        String fromJson = buffer.toString();
        System.out.println(fromJson);
        User userJson = objectMapper.readValue(fromJson, User.class);
        user.setUpdated(Date.valueOf(LocalDate.now()));
        user.setName(userJson.getName());

        out(resp, objectMapper.writeValueAsString(userRepository.edit(user)));
    }

    private void out(HttpServletResponse resp, String jsonString) throws IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(jsonString);
        out.flush();
    }

    private String getByID(String id) throws JsonProcessingException {
        User user = userRepository.getByID(Long.parseLong(id));
        return objectMapper.writeValueAsString(user);
    }

    private String getAll() throws JsonProcessingException {
        List<User> users = userRepository.getAll();
        return objectMapper.writeValueAsString(users);
    }
}
