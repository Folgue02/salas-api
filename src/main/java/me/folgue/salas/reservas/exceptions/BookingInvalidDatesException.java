package me.folgue.salas.reservas.exceptions;

import java.time.LocalDateTime;

/**
 *
 * @author folgue
 */
public class BookingInvalidDatesException extends BookingControllerException {

    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public BookingInvalidDatesException(LocalDateTime startDate, LocalDateTime endDate) {
        super(String.format("Las fechas especificadas para una reserva son invalidas: \n\tComienzo: %s\n\tFinal: %s", startDate, endDate));
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDateTime getStartDate() {
        return this.startDate;
    }

    public LocalDateTime getEndDate() {
        return this.endDate;
    }
}
