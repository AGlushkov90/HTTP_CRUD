package com.glushkov.http_crud.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.glushkov.http_crud.model.User;
import com.glushkov.http_crud.service.UserService;
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


@WebServlet(urlPatterns = {"/api/v1/users", "/api/v1/users/*"})
public class UserRestControllerV1 extends HttpServlet {

    private final UserService userService = new UserService();
    private final JsonUtils jsonUtils = new JsonUtils();

    private final RequestUtils requestUtils = new RequestUtils();
    private final ObjectMapper objectMapper = MapperEntity.getObjectMapper();
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = requestUtils.getIdFromUrl(req);
        if (id == null) {
            jsonUtils.out(resp, objectMapper.writeValueAsString(MapperEntity.convertToUsersDto(userService.getAll())));

        } else {
            jsonUtils.out(resp, objectMapper.writeValueAsString(MapperEntity.convertToUserDto(userService.getByID(id))));
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
        jsonUtils.out(resp, objectMapper.writeValueAsString(userService.delete(id)));
    }

    @Override
    @Produces({MediaType.APPLICATION_JSON})
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder buffer = new StringBuilder();
        String line;
        while ((line = req.getReader().readLine()) != null) {
            buffer.append(line);
        }
        String fromJson = buffer.toString();
        System.out.println(fromJson);
        User user = objectMapper.readValue(fromJson, User.class);
        jsonUtils.out(resp, objectMapper.writeValueAsString(MapperEntity.convertToUserDto(userService.save(userService.save(user)))));
    }

    @Override
    @Produces({MediaType.APPLICATION_JSON})
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = requestUtils.getIdFromUrl(req);
        if (id == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = req.getReader().readLine()) != null) {
                buffer.append(line);
            }
        String fromJson = buffer.toString();
           User userJson = objectMapper.readValue(fromJson, User.class);
        jsonUtils.out(resp, objectMapper.writeValueAsString(MapperEntity.convertToUserDto(userService.edit(id, userJson))));
    }

}
