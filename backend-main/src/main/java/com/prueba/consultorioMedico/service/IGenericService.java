package com.prueba.consultorioMedico.service;

import java.util.List;
                                //Entity
public interface IGenericService<E> {
    List<E> findAll();
    void add(E e);
}
