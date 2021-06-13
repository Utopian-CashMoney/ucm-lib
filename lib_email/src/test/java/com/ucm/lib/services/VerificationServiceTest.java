package com.ucm.lib.services;

import com.ucm.lib.dao.IVerifiableDAO;
import com.ucm.lib.dao.IVerificationDAO;
import com.ucm.lib.entity.IVerifiableEntity;
import com.ucm.lib.entity.IVerificationEntity;
import org.junit.jupiter.api.BeforeEach;
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
    private EmailService emailService;
    static class VerifiableEntity implements IVerifiableEntity {
        private Boolean active;

        @Override
        public Boolean isActive() {
            return active;
        }

        @Override
        public void setActive(Boolean active) {
            this.active = active;
        }
    }

    static class VerificationEntity implements IVerificationEntity<VerifiableEntity> {
        private VerifiableEntity entity;
        private LocalDateTime expires;
        private String code;

        @Override
        public void setEntity(VerifiableEntity entity) {
            this.entity = entity;
        }

        @Override
        public VerifiableEntity getEntity() {
            return entity;
        }

        @Override
        public void setExpires(LocalDateTime localDateTime) {
            this.expires = localDateTime;
        }

        @Override
        public LocalDateTime getExpires() {
            return expires;
        }

        @Override
        public void setCode(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return code;
        }
    }

    static class VerificationDAO implements IVerificationDAO<VerificationEntity> {
        private VerificationEntity verificationEntity;

        VerificationDAO(VerificationEntity verificationEntity) {
            this.verificationEntity = verificationEntity;
        }

        @Override
        public VerificationEntity findFirstByCode(String code) {
            if(verificationEntity != null && verificationEntity.getCode().equals(code)) return verificationEntity;
            return null;
        }

        //Emulate the behavior of saving successfully.
        @Override
        public VerificationEntity save(VerificationEntity entity) {
            return entity;
        }

        @Override
        public void delete(VerificationEntity entity) {
        }
    }

    static class VerifiableDAO implements IVerifiableDAO<VerifiableEntity> {
        @Override
        public VerifiableEntity save(VerifiableEntity entity) {
            return entity;
        }
    }

    final VerificationService<
            VerifiableEntity,
            VerificationEntity,
            VerifiableDAO,
            VerificationDAO>
            verificationService = new VerificationService<>(
                    new VerificationDAO(null),
                    new VerifiableDAO(),
                    null
            );

    @BeforeEach
    void setUp() {
        //Janky, but required
        verificationService.emailService = emailService;
    }

    @Test
    void generateConfirmationTest() {
        VerifiableEntity verifiableEntity = new VerifiableEntity();
        verifiableEntity.setActive(false);
        VerificationEntity verificationEntity = new VerificationEntity();
        verificationService.verificationDAO.verificationEntity = null;
        verificationEntity = verificationService.generateConfirmation(verifiableEntity, verificationEntity);
        assertNotNull(verificationEntity.code);
        assertEquals(verifiableEntity, verificationEntity.entity);
        assertNotNull(verificationEntity.expires);
    }

    @Test
    void confirmTest() {
        VerifiableEntity verifiableEntity = new VerifiableEntity();
        verifiableEntity.setActive(false);
        VerificationEntity verificationEntity = new VerificationEntity();
        verificationEntity.setCode("CODE");
        verificationEntity.setExpires(LocalDateTime.now().plusDays(1));
        verificationEntity.setEntity(verifiableEntity);
        verificationService.verificationDAO.verificationEntity = verificationEntity;
        assertTrue(verificationService.confirm("CODE"));
        assertTrue(verifiableEntity.isActive());
    }

    @Test
    void sendVerificationEmailTest() {
        VerifiableEntity verifiableEntity = new VerifiableEntity();
        verifiableEntity.setActive(false);
        VerificationEntity verificationEntity = new VerificationEntity();
        verificationEntity.setCode("CODE");
        verificationEntity.setExpires(LocalDateTime.now().plusDays(1));
        verificationEntity.setEntity(verifiableEntity);
        verificationService.sendVerificationEmail(verificationEntity, "test@test.test", "html/confirm_account");
    }
}