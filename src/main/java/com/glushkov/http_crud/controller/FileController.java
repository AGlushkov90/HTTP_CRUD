package com.glushkov.http_crud.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glushkov.http_crud.model.Event;
import com.glushkov.http_crud.model.File;
import com.glushkov.http_crud.model.Status;
import com.glushkov.http_crud.repository.EventRepository;
import com.glushkov.http_crud.repository.FileRepository;
import com.glushkov.http_crud.repository.impl.ORMEventRepositoryImpl;
import com.glushkov.http_crud.repository.impl.ORMFileRepositoryImpl;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;


public class FileController extends HttpServlet {
    private final FileRepository fileRepository = new ORMFileRepositoryImpl();
    private final EventRepository eventRepository = new ORMEventRepositoryImpl();
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
        out(resp, String.valueOf(fileRepository.delete(Long.parseLong(id))));
    }

    @Override
    @Produces({MediaType.APPLICATION_JSON})
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        Event event = null;
        while ((line = reader.readLine()) != null) {
            if (line.contains("event")) {
                event = eventRepository.getByID(2L);
                continue;
            }
            buffer.append(line);
        }
        String fromJson = buffer.toString();
        System.out.println(fromJson);
        File file = objectMapper.readValue(fromJson, File.class);
        file.setCreated(Date.valueOf(LocalDate.now()));
        file.setStatus(Status.ACTIVE);
        file.setEvent(event);
        out(resp, objectMapper.writeValueAsString(fileRepository.save(file)));
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
        File file = fileRepository.getByID(Long.parseLong(id));
        String fromJson = buffer.toString();
        System.out.println(fromJson);
        File fileJson = objectMapper.readValue(fromJson, File.class);
        file.setName(fileJson.getName());
        file.setFilePath(fileJson.getFilePath());
        file.setUpdated(Date.valueOf(LocalDate.now()));
        out(resp, objectMapper.writeValueAsString(fileRepository.edit(file)));
    }

    private void out(HttpServletResponse resp, String jsonString) throws IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(jsonString);
        out.flush();
    }

    private String getByID(String id) throws JsonProcessingException {
        File file = fileRepository.getByID(Long.parseLong(id));
        return objectMapper.writeValueAsString(file);
    }

    private String getAll() throws JsonProcessingException {
        List<File> files = fileRepository.getAll();
        return objectMapper.writeValueAsString(files);
    }
}
