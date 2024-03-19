package com.glushkov.http_crud.repository;




import com.glushkov.http_crud.model.BaseItem;
import org.hibernate.Session;

import java.util.List;

public interface GenericRepository<T extends BaseItem, ID> {
    T getByID(ID id);

    T getByID(ID id, Session session);

    List<T> getAll();

    List<T> getAll(Session session);

    boolean delete(ID id);

    T edit(T item);

    T edit(T item, Session session);

    T save(T item);
}
