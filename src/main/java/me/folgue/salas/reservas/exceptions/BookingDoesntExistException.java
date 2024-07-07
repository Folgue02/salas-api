package me.folgue.salas.reservas.exceptions;

/**
 *
 * @author folgue
 */
public class BookingDoesntExistException extends BookingControllerException {

    private final Long bookingId;

    /**
     * /**
     * Creates a new instance of <code>ReservaDoesntExistException</code>
     * without detail message.
     *
     * @param bookingId Id of the booking that doesn't exist.
     */
    public BookingDoesntExistException(long bookingId) {
        super(String.format("No existe ninguna reserva con el ID %d", bookingId));
        this.bookingId = bookingId;
    }
}
