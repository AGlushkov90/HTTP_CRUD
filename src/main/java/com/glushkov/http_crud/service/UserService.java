package com.glushkov.http_crud.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glushkov.http_crud.model.Event;
import com.glushkov.http_crud.model.File;
import com.glushkov.http_crud.model.User;
import com.glushkov.http_crud.model.Status;
import com.glushkov.http_crud.repository.EventRepository;
import com.glushkov.http_crud.repository.impl.ORMCommonRepository;
import com.glushkov.http_crud.repository.impl.ORMEventRepositoryImpl;
import com.glushkov.http_crud.repository.impl.ORMUserRepositoryImpl;
import com.glushkov.http_crud.utils.MapperEntity;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class UserService {
    private final ORMUserRepositoryImpl userRepository = new ORMUserRepositoryImpl();
    private final EventRepository eventRepository = new ORMEventRepositoryImpl();
    private final ObjectMapper objectMapper = MapperEntity.getObjectMapper();

    public String getByID(Long id) throws JsonProcessingException {
        Transaction tx = null;
        try (Session session = ORMCommonRepository.getSession()) {
            tx = session.beginTransaction();
            User user = userRepository.getByID(id);
            setUnproxyFile(user, session);
            String stringUsers = objectMapper.writeValueAsString(MapperEntity.convertToUserDto(user));
            tx.commit();
            return stringUsers;
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getAll() throws JsonProcessingException {
        Transaction tx = null;
        try (Session session = ORMCommonRepository.getSession()) {
            tx = session.beginTransaction();
            List<User> users = userRepository.getAll(session);
            for (User user : users) {
                setUnproxyFile(user, session);
            }
            String stringUsers = objectMapper.writeValueAsString(MapperEntity.convertToUsersDto(users));
            tx.commit();
            return stringUsers;

        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String delete(Long id) throws JsonProcessingException {
        return objectMapper.writeValueAsString(userRepository.delete(id));
    }

    public String save(BufferedReader reader) throws IOException {
        StringBuilder buffer = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String fromJson = buffer.toString();
        System.out.println(fromJson);
        User user = objectMapper.readValue(fromJson, User.class);
        user.setStatus(Status.ACTIVE);
        return objectMapper.writeValueAsString(MapperEntity.convertToUserDto(userRepository.save(user)));
    }


    public String edit(Long id, BufferedReader reader) throws IOException {
        Transaction tx = null;
        try (Session session = ORMCommonRepository.getSession()) {
            tx = session.beginTransaction();

            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            User user = userRepository.getByID(id);
            session.detach(user);

            for (Event event : user.getEvents()) {
                event.setFile(Hibernate.unproxy(event.getFile(), File.class));
            }
            String fromJson = buffer.toString();
            User userJson = objectMapper.readValue(fromJson, User.class);
            user.setName(userJson.getName());
            String stringUsers = objectMapper.writeValueAsString(MapperEntity.convertToUserDto(userRepository.edit(user)));
            tx.commit();
            return stringUsers;

        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setUnproxyFile(User user, Session session) {
        session.detach(user);
        Set<Event> events = user.getEvents();
        for (Event event : events) {
            event.setFile(Hibernate.unproxy(event.getFile(), File.class));
        }
    }
}


