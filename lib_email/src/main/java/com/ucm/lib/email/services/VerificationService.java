package com.ucm.lib.email.services;

import com.ucm.lib.email.dao.IVerificationDAO;
import com.ucm.lib.email.entity.IVerifiableEntity;
import com.ucm.lib.email.entity.IVerificationEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VerificationService {
    /**
     * Generate (populate) a confirmation entity for a confirmable entity.
     * @author Joshua Podhola.
     * @param verifiableEntity An @Entity that implements IVerifiableEntity.
     * @param verificationEntity An @Entity that implements IVerificationEntity. Is modified in place.
     * @param verificationRepository A @Repository that implements IVerificationDAO.
     * @param <T> The type of entity that should be verified.
     * @param <S> The type of entity that should store the verification information.
     * @param <D> The type of repository that accesses the verification information.
     * @return The modified verificationEntity.
     */
    protected <T extends IVerifiableEntity, S extends IVerificationEntity<T>, D extends IVerificationDAO<S>>
    S generateConfirmation(final T verifiableEntity,
                                                S verificationEntity,
                                                final D verificationRepository) {
        verificationEntity.setEntity(verifiableEntity);
        verificationEntity.setExpires(LocalDateTime.now().plusDays(1));
        String s;
        do {
            s = UUID.randomUUID().toString();
            verificationEntity.setCode(s);
        } while(verificationRepository.findFirstByCode(s) != null);
        return verificationRepository.save(verificationEntity);
    }
}
