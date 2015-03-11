package org.uniknow.agiledev.ddd.domain.shared;

/**
 * Represents an object which is distinguished by its identity, rather than its attributes.
 */
public interface Entity<T> {

    /**
     * Compares {@code Entities} by their identity, not by attributes.
     *
     * @param other The other {@code Entity}.
     *              .
     * @return true if the identities are the same, regardless of other attributes.
     */
    boolean sameIdentity(T other);
}
