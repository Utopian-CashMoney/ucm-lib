package com.ucm.lib.email.services;

import com.ucm.lib.email.dao.IVerifiableDAO;
import com.ucm.lib.email.dao.IVerificationDAO;
import com.ucm.lib.email.entity.IVerifiableEntity;
import com.ucm.lib.email.entity.IVerificationEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

public class VerificationService<
        VerifiableEntity extends IVerifiableEntity,
        VerificationEntity extends IVerificationEntity<VerifiableEntity>,
        VerifiableDAO extends IVerifiableDAO<VerifiableEntity>,
        VerificationDAO extends IVerificationDAO<VerificationEntity>
        > {
    VerificationDAO verificationDAO;
    VerifiableDAO verifiableDAO;

    public VerificationService(VerificationDAO verificationDAO, VerifiableDAO verifiableDAO) {
        this.verificationDAO = verificationDAO;
        this.verifiableDAO = verifiableDAO;
    }

    /**
     * Generate (populate) a confirmation entity for a confirmable entity.
     * @author Joshua Podhola.
     * @param verifiableEntity An @Entity that implements IVerifiableEntity.
     * @param verificationEntity An @Entity that implements IVerificationEntity. Is modified in place.
     * @return The modified verificationEntity.
     */
    protected VerificationEntity generateConfirmation(final VerifiableEntity verifiableEntity,
                                            VerificationEntity verificationEntity) {
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
     * @return True if successfully verified, false if the token has expired.
     */
    public Boolean confirm(final String confirmationToken) {
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
