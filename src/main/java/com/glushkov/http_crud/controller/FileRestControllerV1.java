package com.glushkov.http_crud.controller;

import com.glushkov.http_crud.service.FileService;
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

@WebServlet(urlPatterns = {"/api/v1/files", "/api/v1/files/*"})
public class FileRestControllerV1 extends HttpServlet {

    private final FileService fileService = new FileService();
    private final JsonUtils jsonUtils = new JsonUtils();
    private final RequestUtils requestUtils = new RequestUtils();

    @Override
    @Produces({MediaType.APPLICATION_JSON})
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = requestUtils.getIdFromUrl(req);
        if (id == null) {
            jsonUtils.out(resp, fileService.getAll());

        } else {
            jsonUtils.out(resp, fileService.getByID(id));
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
        jsonUtils.out(resp, fileService.delete(id));
    }

    @Override
    @Produces({MediaType.APPLICATION_JSON})
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long userId = requestUtils.getUserIdFromHeaders(req);
        if (userId == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        BufferedReader reader = req.getReader();
        jsonUtils.out(resp, fileService.save(reader, userId));
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
        jsonUtils.out(resp, fileService.edit(id, reader));
    }


}
