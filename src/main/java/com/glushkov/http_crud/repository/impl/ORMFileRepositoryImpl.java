package com.glushkov.http_crud.repository.impl;

import com.glushkov.http_crud.model.File;
import com.glushkov.http_crud.repository.FileRepository;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ORMFileRepositoryImpl implements FileRepository {

    @Override
    public File getByID(Long id) {
        try (Session session = ORMCommonRepository.getSession()) {
            return session.get(File.class, id);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<File> getAll() {
        try (Session session = ORMCommonRepository.getSession()) {
            List<File> files = session.createQuery("FROM File").list();
            return files;
        } catch (HibernateException e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        Transaction tx = null;
        try (Session session = ORMCommonRepository.getSession()) {
            tx = session.beginTransaction();
            File file = session.load(File.class, id);
            session.delete(file);
            tx.commit();
            return true;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();

        }
        return false;
    }

    @Override
    public File save(File itemToSave) {
        Transaction tx = null;
        try (Session session = ORMCommonRepository.getSession()) {
            tx = session.beginTransaction();
            itemToSave = session.merge(itemToSave);
            tx.commit();
            return itemToSave;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public File edit(File itemToUpdate) {
        Transaction tx = null;
        try (Session session = ORMCommonRepository.getSession()) {
            tx = session.beginTransaction();
            session.update(itemToUpdate);
            tx.commit();
            return itemToUpdate;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();

        }
        return null;
    }

}

