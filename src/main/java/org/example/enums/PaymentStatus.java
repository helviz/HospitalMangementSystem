package org.example.enums;

public enum PaymentStatus {
    PENDING,       // Payment is initiated but not yet completed
    PROCESSING,    // Payment is in progress
    COMPLETED,     // Payment was successful
    FAILED,        // Payment failed due to an error or rejection
    CANCELLED,     // Payment was cancelled by user or system
    REFUNDED       // Payment was refunded
}
