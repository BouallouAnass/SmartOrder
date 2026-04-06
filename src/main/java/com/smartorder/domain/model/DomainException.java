package com.smartorder.domain.model;

/**
 * Base exception for domain rule violations.
 * Thrown by aggregates when business invariants are broken.
 * Caught by the REST adapter and translated to HTTP 422 or 400.
 *
 * Usage in your aggregates:
 *   throw new DomainException("Cannot add lines to a PLACED order");
 *   throw new DomainException("Insufficient stock for product: " + productId);
 */
public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
