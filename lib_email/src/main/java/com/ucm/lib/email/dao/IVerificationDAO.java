package com.ucm.lib.email.dao;

import com.ucm.lib.email.entity.IVerificationEntity;

public interface IVerificationDAO<T extends IVerificationEntity<?>> {
    T findFirstByCode(String code);
    T save(T entity);
}
