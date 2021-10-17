package dev.granville.dao;

import java.util.List;

public interface GenericDao<T> {
    T add(T t);
    T delete (T t);
    T update (T t);
    T getById(Integer i);
    T getRandom();
    List<T> getAll();
    void deleteAll();
}
