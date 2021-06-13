package com.ucm.lib.dao;

import com.ucm.lib.entity.IVerifiableEntity;

public interface IVerifiableDAO<T extends IVerifiableEntity> {
    T save(T entity);
}
