package com.ucm.lib.services;

import com.ucm.lib.dao.IVerificationDAO;
import com.ucm.lib.entity.IVerifiableEntity;
import com.ucm.lib.entity.IVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class VerificationService<
        VerifiableEntity extends IVerifiableEntity,
        VerificationEntity extends IVerificationEntity<VerifiableEntity>,
        VerifiableDAO extends JpaRepository<VerifiableEntity, ?>,
        VerificationDAO extends JpaRepository<VerificationEntity, ?> & IVerificationDAO<VerificationEntity>
        > {
    VerificationDAO verificationDAO;
    VerifiableDAO verifiableDAO;
    EmailService emailService;

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");

    public VerificationService(VerificationDAO verificationDAO, VerifiableDAO verifiableDAO, EmailService emailService) {
        this.verificationDAO = verificationDAO;
        this.verifiableDAO = verifiableDAO;
        this.emailService = emailService;
    }

    /**
     * Generate (populate) a confirmation entity for a confirmable entity.
     * @author Joshua Podhola.
     * @param verifiableEntity An @Entity that implements IVerifiableEntity.
     * @param verificationEntity An @Entity that implements IVerificationEntity. Is modified in place.
     * @return The modified verificationEntity.
     */
    public VerificationEntity generateConfirmation(final VerifiableEntity verifiableEntity,
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

    public void sendVerificationEmail(VerificationEntity verificationEntity, String email, String template) {
        try {
            Context context = new Context();
            context.setVariable("expiration", verificationEntity.getExpires().format(DATETIME_FORMATTER));
            context.setVariable("confirmation_code", verificationEntity.getCode());
            emailService.sendEmail(email, context,template,"Verification Required");
        } catch (MessagingException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Failed to send confirmation email.", e);
        }
    }
}
