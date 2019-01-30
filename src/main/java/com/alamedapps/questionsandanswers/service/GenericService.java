package com.alamedapps.questionsandanswers.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface GenericService<T, ID extends Serializable> {

    void saveOrUpdate(T object);

    void delete(T object);

    Optional<T> findById(int id);

    List<T> findAll();

}
