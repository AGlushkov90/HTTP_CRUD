package com.glushkov.http_crud.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.glushkov.http_crud.utils.MapperEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class FileService {

    private final FileRepository fileRepository = new ORMFileRepositoryImpl();
    private final EventRepository eventRepository = new ORMEventRepositoryImpl();
    private final UserRepository userRepository = new ORMUserRepositoryImpl();
    private final ObjectMapper objectMapper = MapperEntity.getObjectMapper();

    public String getByID(Long id) throws JsonProcessingException {
        File file = fileRepository.getByID(id);
        return objectMapper.writeValueAsString(MapperEntity.convertToFileDto(file));
    }

    public String getAll() throws JsonProcessingException {
        List<File> files = fileRepository.getAll();
        return objectMapper.writeValueAsString(MapperEntity.convertToFilesDto(files));
    }

    public String delete(Long id) throws JsonProcessingException {
        return objectMapper.writeValueAsString(String.valueOf(fileRepository.delete(id)));
    }

    public String save(BufferedReader reader, Long userId) throws IOException {
        StringBuilder buffer = new StringBuilder();
        String line;
        byte[] decodedBytes = null;
        while ((line = reader.readLine()) != null) {
//            if(line.startsWith("    \"data")){
////               decodedBytes = Base64.getDecoder().decode(line.substring(12, line.length()-1));
//               decodedBytes = Base64.getDecoder().decode("");
//continue;
//            }
            buffer.append(line);
        }
        String fromJson = buffer.toString();
        System.out.println(fromJson);
        File file = objectMapper.readValue(fromJson, File.class);
        file.setStatus(Status.ACTIVE);
        file.setData(decodedBytes);

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
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return objectMapper.writeValueAsString(MapperEntity.convertToFileDto(file));
    }


    public String edit(Long id, BufferedReader reader) throws IOException {

        StringBuilder buffer = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        File file = fileRepository.getByID(id);
        String fromJson = buffer.toString();
        System.out.println(fromJson);
        File fileJson = objectMapper.readValue(fromJson, File.class);
        file.setName(fileJson.getName());
        file.setFilePath(fileJson.getFilePath());
        return objectMapper.writeValueAsString(MapperEntity.convertToFileDto(fileRepository.edit(file)));
    }
}
