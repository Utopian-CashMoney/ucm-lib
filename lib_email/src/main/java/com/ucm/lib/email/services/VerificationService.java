package com.ucm.lib.email.services;

import com.ucm.lib.email.dao.IVerifiableDAO;
import com.ucm.lib.email.dao.IVerificationDAO;
import com.ucm.lib.email.entity.IVerifiableEntity;
import com.ucm.lib.email.entity.IVerificationEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VerificationService {
    /**
     * Generate (populate) a confirmation entity for a confirmable entity.
     * @author Joshua Podhola.
     * @param verifiableEntity An @Entity that implements IVerifiableEntity.
     * @param verificationEntity An @Entity that implements IVerificationEntity. Is modified in place.
     * @param verificationDAO A @Repository that implements IVerificationDAO.
     * @param <VerifiableEntity> The type of entity that should be verified.
     * @param <VerificationEntity> The type of entity that should store the verification information.
     * @param <VerificationDAO> The type of repository that accesses the verification information.
     * @return The modified verificationEntity.
     */
    protected <VerifiableEntity extends IVerifiableEntity,
               VerificationEntity extends IVerificationEntity<VerifiableEntity>,
               VerificationDAO extends IVerificationDAO<VerificationEntity>>
    VerificationEntity generateConfirmation(final VerifiableEntity verifiableEntity,
                                            VerificationEntity verificationEntity,
                                            final VerificationDAO verificationDAO) {
        verificationEntity.setEntity(verifiableEntity);
        verificationEntity.setExpires(LocalDateTime.now().plusDays(1));
        String s;
        do {
            s = UUID.randomUUID().toString();
            verificationEntity.setCode(s);
        } while(verificationDAO.findFirstByCode(s) != null);
        return verificationDAO.save(verificationEntity);
    }

    /**
     * Use the given confirmationcode to confirm an entity.
     * @param confirmationToken The token that should be used for confirmation.
     * @param verificationDAO The DAO that should be used for the verification entities.
     * @param verifiableDAO The DAO that should be used
     * @param <VerifiableEntity> The entity type to verify.
     * @param <VerificationEntity> The entity storing the confirmation information.
     * @param <VerificationDAO> The DAO type that stores the confirmation information.
     * @param <VerifiableDAO> The DAO type that stores the entity type.
     * @return True if successfully verified, false if the token has expired.
     */
    public <VerifiableEntity extends IVerifiableEntity,
            VerificationEntity extends IVerificationEntity<VerifiableEntity>,
            VerificationDAO extends IVerificationDAO<VerificationEntity>,
            VerifiableDAO extends IVerifiableDAO<VerifiableEntity>>
    Boolean confirm(final String confirmationToken,
                    final VerificationDAO verificationDAO,
                    final VerifiableDAO verifiableDAO) {
        VerificationEntity verificationEntity = verificationDAO.findFirstByCode(confirmationToken);
        if(verificationEntity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No confirmation found with that token.");
        }
        if (verificationEntity.getExpires().isBefore(LocalDateTime.now())) {
            verificationDAO.delete(verificationEntity);
            return false;
        }
        else {
            VerifiableEntity verifiableEntity = verificationEntity.getEntity();
            verifiableEntity.setActive(true);
            verifiableDAO.save(verifiableEntity);
            verificationDAO.delete(verificationEntity);
            return true;
        }
    }
}
