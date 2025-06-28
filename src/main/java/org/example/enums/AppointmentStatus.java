package org.example.enums;

public enum AppointmentStatus {
    SCHEDULED,     // Appointment is booked and confirmed
    RESCHEDULED,   // Appointment was moved to a new time
    CANCELLED,     // Appointment was cancelled by user or system
    COMPLETED,     // Appointment was successfully held
    NO_SHOW,       // User missed the appointment
    IN_PROGRESS,   // Appointment is currently happening
    PENDING,       // Waiting for confirmation/approval
    EXPIRED        // Appointment time passed without any update
}
