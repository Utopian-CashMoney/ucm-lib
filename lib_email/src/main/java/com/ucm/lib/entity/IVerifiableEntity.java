package com.ucm.lib.entity;

/**
 * Interface for any value that VerificationService should support.
 */
public interface IVerifiableEntity {
    /**
     * Get whether the entity has been verified.
     * @return Boolean is_active
     */
    Boolean isActive();

    /**
     * Set the active status
     * @param active New value for active.
     */
    void setActive(Boolean active);
}
