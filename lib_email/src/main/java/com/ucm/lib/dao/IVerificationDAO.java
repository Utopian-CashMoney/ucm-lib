package com.ucm.lib.dao;

import com.ucm.lib.entity.IVerificationEntity;

public interface IVerificationDAO<T extends IVerificationEntity<?>> {
    T findFirstByCode(String code);
}
