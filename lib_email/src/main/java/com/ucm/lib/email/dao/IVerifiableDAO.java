package com.ucm.lib.email.dao;

import com.ucm.lib.email.entity.IVerifiableEntity;
import com.ucm.lib.email.entity.IVerificationEntity;

public interface IVerifiableDAO<T extends IVerifiableEntity> {
    T save(T entity);
}
