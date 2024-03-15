package com.glushkov.http_crud.repository.impl;

import com.glushkov.http_crud.model.Event;
import com.glushkov.http_crud.model.File;
import com.glushkov.http_crud.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class ORMCommonRepository {

    private final static SessionFactory sessionFactory;

    static {
        sessionFactory = new Configuration()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Event.class)
                .addAnnotatedClass(File.class)
                .buildSessionFactory();
    }

    public static synchronized Session getSession() {
        return sessionFactory.openSession();
    }
}
