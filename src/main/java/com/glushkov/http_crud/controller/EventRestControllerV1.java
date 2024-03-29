package com.glushkov.http_crud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glushkov.http_crud.service.EventService;
import com.glushkov.http_crud.utils.JsonUtils;
import com.glushkov.http_crud.utils.MapperEntity;
import com.glushkov.http_crud.utils.RequestUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@WebServlet(urlPatterns = {"/api/v1/events", "/api/v1/events/*"})
public class EventRestControllerV1 extends HttpServlet {
    private final EventService eventService = new EventService();
    private final JsonUtils jsonUtils = new JsonUtils();
    private final RequestUtils requestUtils = new RequestUtils();
    private final ObjectMapper objectMapper = MapperEntity.getObjectMapper();

    @Override
    @Produces({MediaType.APPLICATION_JSON})
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = requestUtils.getIdFromUrl(req);
        if (id == null) {
            jsonUtils.out(resp, objectMapper.writeValueAsString(MapperEntity.convertToEventsDto(eventService.getAll())));

        } else {
            jsonUtils.out(resp, objectMapper.writeValueAsString(MapperEntity.convertToEventDto(eventService.getByID(id))));
        }
    }

    @Override
    @Produces({MediaType.APPLICATION_JSON})
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = requestUtils.getIdFromUrl(req);
        if (id == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        jsonUtils.out(resp, objectMapper.writeValueAsString(String.valueOf(eventService.delete(id))));
    }
}
