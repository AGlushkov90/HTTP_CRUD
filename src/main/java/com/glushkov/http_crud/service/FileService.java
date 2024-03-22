package com.glushkov.http_crud.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.glushkov.http_crud.model.Event;
import com.glushkov.http_crud.model.File;
import com.glushkov.http_crud.model.Status;
import com.glushkov.http_crud.repository.EventRepository;
import com.glushkov.http_crud.repository.FileRepository;
import com.glushkov.http_crud.repository.UserRepository;
import com.glushkov.http_crud.repository.impl.ORMCommonRepository;
import com.glushkov.http_crud.repository.impl.ORMEventRepositoryImpl;
import com.glushkov.http_crud.repository.impl.ORMFileRepositoryImpl;
import com.glushkov.http_crud.repository.impl.ORMUserRepositoryImpl;
import jakarta.servlet.http.Part;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

public class FileService {

    public FileRepository fileRepository = new ORMFileRepositoryImpl();
    private final EventRepository eventRepository = new ORMEventRepositoryImpl();
    private final UserRepository userRepository = new ORMUserRepositoryImpl();

    private final String uploadPath = "C:\\project java\\HTTP_CRUD\\src\\main\\resources\\files";

    public FileService() {
    }

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public java.io.File getByID(Long id) {
        File file = fileRepository.getByID(id);
        if (file == null){
            return null;
        }
        return new java.io.File(file.getFilePath());
    }

    public Set<java.io.File> getAll() throws JsonProcessingException {
        return fileRepository.getAll().stream().map(f -> new java.io.File(f.getFilePath())).collect(Collectors.toSet());
    }

    public Boolean delete(Long id) throws JsonProcessingException {
        return fileRepository.delete(id);
    }

    public File save(Part filePart, Long userId) throws IOException {

        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        InputStream inputStream = filePart.getInputStream();
        String filePath = uploadPath + java.io.File.separator + fileName;
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            Files.copy(inputStream, path);
        }
        File file = new File();
        file.setStatus(Status.ACTIVE);
        file.setName(fileName);
        file.setFilePath(filePath);
        Transaction tx = null;
        try (Session session = ORMCommonRepository.getSession()) {
            tx = session.beginTransaction();
            file = fileRepository.save(file);
            Event event = new Event();
            event.setFile(file);
            event.setUser(userRepository.getByID(userId));
            event.setStatus(Status.ACTIVE);
            eventRepository.save(event);
            tx.commit();
            return file;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return null;
        }
    }

    public File edit(Part filePart, Long id) throws IOException {
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        InputStream inputStream = filePart.getInputStream();
        String filePath = uploadPath + java.io.File.separator + fileName;
        Files.copy(inputStream, Paths.get(filePath));
        File file = fileRepository.getByID(id);
        try{
        file.setStatus(Status.ACTIVE);
        file.setName(fileName);
        file.setFilePath(filePath);
        fileRepository.save(file);
        return file;
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }
}
