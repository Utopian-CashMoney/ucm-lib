package com.ucm.lib.email.services;

import com.ucm.lib.email.dao.IVerificationDAO;
import com.ucm.lib.email.entity.IVerifiableEntity;
import com.ucm.lib.email.entity.IVerificationEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class VerificationServiceTest {
    @Autowired
    VerificationService verificationService;

    static class VerifiableEntity implements IVerifiableEntity {
        public Boolean active;

        @Override
        public Boolean isActive() {
            return active;
        }

        @Override
        public void setActive(Boolean active) {
            this.active = active;
        }
    }

    @SuppressWarnings("FieldCanBeLocal")
    static class VerificationEntity implements IVerificationEntity<VerifiableEntity> {
        public VerifiableEntity entity;
        public LocalDateTime expires;
        public String code;

        @Override
        public void setEntity(VerifiableEntity entity) {
            this.entity = entity;
        }

        @Override
        public void setExpires(LocalDateTime localDateTime) {
            this.expires = localDateTime;
        }

        @Override
        public void setCode(String code) {
            this.code = code;
        }
    }

    static class VerificationDAO implements IVerificationDAO<VerificationEntity> {
        VerificationEntity verificationEntity;

        VerificationDAO(VerificationEntity verificationEntity) {
            this.verificationEntity = verificationEntity;
        }

        //Emulate the behavior of not finding any entity with that code.
        @Override
        public VerificationEntity findFirstByCode(String code) {
            return null;
        }

        //Emulate the behavior of saving successfully.
        @Override
        public VerificationEntity save(VerificationEntity entity) {
            return entity;
        }
    }

    @Test
    void generateConfirmationTest() {
        VerifiableEntity verifiableEntity = new VerifiableEntity();
        verifiableEntity.active = false;
        VerificationEntity verificationEntity = new VerificationEntity();
        VerificationDAO verificationDAO = new VerificationDAO(verificationEntity);
        verificationEntity = verificationService.generateConfirmation(verifiableEntity, verificationEntity, verificationDAO);
        assertNotNull(verificationEntity.code);
        assertEquals(verifiableEntity, verificationEntity.entity);
        assertNotNull(verificationEntity.expires);
    }
}