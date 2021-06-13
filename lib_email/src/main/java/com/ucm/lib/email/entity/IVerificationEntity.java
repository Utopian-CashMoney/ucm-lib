package com.ucm.lib.email.entity;

import java.time.LocalDateTime;

public interface IVerificationEntity<T extends IVerifiableEntity> {
    void setEntity(T entity);
    T getEntity();
    void setExpires(LocalDateTime localDateTime);
    LocalDateTime getExpires();
    void setCode(String code);
    String getCode();
}
