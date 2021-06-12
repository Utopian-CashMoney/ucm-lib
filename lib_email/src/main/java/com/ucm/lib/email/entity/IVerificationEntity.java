package com.ucm.lib.email.entity;

import java.time.LocalDateTime;

public interface IVerificationEntity<T extends IVerifiableEntity> {
    void setEntity(T entity);
    void setExpires(LocalDateTime localDateTime);
    void setCode(String code);
}
