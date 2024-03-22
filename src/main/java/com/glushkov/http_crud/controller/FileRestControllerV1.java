package com.glushkov.http_crud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glushkov.http_crud.service.FileService;
import com.glushkov.http_crud.utils.JsonUtils;
import com.glushkov.http_crud.utils.MapperEntity;
import com.glushkov.http_crud.utils.RequestUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.http.Part;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@WebServlet(urlPatterns = {"/api/v1/files", "/api/v1/files/*"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 1024 * 1024 * 5,   // 5 MB
        maxRequestSize = 1024 * 1024 * 10)
public class FileRestControllerV1 extends HttpServlet {

    private final FileService fileService = new FileService();
    private final JsonUtils jsonUtils = new JsonUtils();
    private final RequestUtils requestUtils = new RequestUtils();
    private final ObjectMapper objectMapper = MapperEntity.getObjectMapper();

    @Override
    @Produces({MediaType.APPLICATION_JSON})
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = requestUtils.getIdFromUrl(req);
        if (id == null) {
            getAll(req, resp);
        } else {
            getById(id, req, resp);
        }
    }

    private void getAll(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("Content-type: text/zip");
        resp.setHeader("Content-Disposition",
                "attachment; filename=files.zip");
        Set<java.io.File> files = fileService.getAll();
        try (
                OutputStream outputStream = resp.getOutputStream();
                ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(outputStream))) {
            for (java.io.File file : files) {
                System.out.println("Adding " + file.getName());
                zos.putNextEntry(new ZipEntry(file.getName()));
                FileInputStream fis;
                try {
                    fis = new FileInputStream(file);
                } catch (FileNotFoundException fnfe) {
                    zos.write(("ERROR not find file " + file.getName()).getBytes());
                    zos.closeEntry();
                    System.out.println("Could find file " + file.getAbsolutePath());
                    continue;
                }
                BufferedInputStream fif = new BufferedInputStream(fis);
                int data;
                while ((data = fif.read()) != -1) {
                    zos.write(data);
                }
                fif.close();

                zos.closeEntry();
                System.out.println("Finishedng file " + file.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getById(Long id, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        java.io.File file = fileService.getByID(id);
        if (file != null && file.exists()) {
            String fileName = file.getName();
            String contentType = getServletContext().getMimeType(fileName);
            resp.setContentType(contentType);
            resp.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            try (FileInputStream inputStream = new FileInputStream(file);
                 OutputStream outputStream = resp.getOutputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            resp.getWriter().println("File not found on the server.");
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
        jsonUtils.out(resp, objectMapper.writeValueAsString(String.valueOf(fileService.delete(id))));
    }

    @Override
    @Produces({MediaType.APPLICATION_JSON})
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Long userId = requestUtils.getUserIdFromHeaders(req);
        if (userId == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Part filePart = req.getPart("file");
        if (fileService.save(filePart, userId) != null) {
            resp.getWriter().println("File uploaded successfully!");
        } else {
            resp.getWriter().println("File uploaded not successfully!");
        }
    }

    @Override
    @Produces({MediaType.APPLICATION_JSON})
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        Long id = requestUtils.getIdFromUrl(req);
        if (id == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Part filePart = req.getPart("file");
        if (fileService.edit(filePart, id) != null){
            resp.getWriter().println("File uploaded successfully!");
        } else {
            resp.getWriter().println("File uploaded not successfully!");
        }

    }
}
