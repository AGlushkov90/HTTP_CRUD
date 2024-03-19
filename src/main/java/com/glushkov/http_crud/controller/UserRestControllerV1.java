package com.glushkov.http_crud.controller;


import com.glushkov.http_crud.service.UserService;
import com.glushkov.http_crud.utils.JsonUtils;
import com.glushkov.http_crud.utils.RequestUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;


@WebServlet(urlPatterns = {"/api/v1/users", "/api/v1/users/*"})
public class UserRestControllerV1 extends HttpServlet {

    private final UserService userService = new UserService();
    private final JsonUtils jsonUtils = new JsonUtils();

    private final RequestUtils requestUtils = new RequestUtils();


    @Override
    @Produces({MediaType.APPLICATION_JSON})
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = requestUtils.getIdFromUrl(req);
        if (id == null) {
            jsonUtils.out(resp, userService.getAll());

        } else {
            jsonUtils.out(resp, userService.getByID(id));
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
        jsonUtils.out(resp, userService.delete(id));
    }

    @Override
    @Produces({MediaType.APPLICATION_JSON})
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader reader = req.getReader();
        jsonUtils.out(resp, userService.save(reader));
    }

    @Override
    @Produces({MediaType.APPLICATION_JSON})
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = requestUtils.getIdFromUrl(req);
        if (id == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        BufferedReader reader = req.getReader();
        jsonUtils.out(resp, userService.edit(id, reader));
    }

}
