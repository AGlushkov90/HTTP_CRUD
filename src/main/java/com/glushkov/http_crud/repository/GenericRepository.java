package com.glushkov.http_crud.repository;




import com.glushkov.http_crud.model.BaseItem;

import java.util.Collection;
import java.util.List;

public interface GenericRepository<T extends BaseItem, ID> {
    T getByID(ID id);

    List<T> getAll();

    boolean delete(ID id);

    T edit(T item);

    T save(T item);
}
