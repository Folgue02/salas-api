package me.folgue.salas.bookings.exceptions;

import java.time.LocalDateTime;

/**
 * Represents the scenario where a booking has been attempted to be made for a
 * room that is booked for that time.
 *
 * @author folgue
 */
public class BookingConflictException extends BookingControllerException {

    private final Long roomId;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final LocalDateTime conflictedStartDate;
    private final LocalDateTime conflictedEndDate;

    public BookingConflictException(
            LocalDateTime startDate,
            LocalDateTime endDate,
            LocalDateTime conflictedStartDate,
            LocalDateTime conflictedEndDate,
            long roomId
    ) {
        super(String.format("Se intento hacer una reserva entre '%s' y '%s' que hace conflicto con la reserva entre '%s' y '%s' para la sala con ID '%d'", startDate, endDate, conflictedStartDate, conflictedEndDate, roomId));
        this.startDate = startDate;
        this.endDate = endDate;
        this.roomId = roomId;
        this.conflictedStartDate = conflictedStartDate;
        this.conflictedEndDate = conflictedEndDate;
    }

    public Long getRoomId() {
        return roomId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public LocalDateTime getConflictedStartDate() {
        return conflictedStartDate;
    }

    public LocalDateTime getConflictedEndDate() {
        return conflictedEndDate;
    }
}
