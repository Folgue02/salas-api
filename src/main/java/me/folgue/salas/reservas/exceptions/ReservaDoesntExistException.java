package me.folgue.salas.reservas.exceptions;

/**
 *
 * @author folgue
 */
public class ReservaDoesntExistException extends ReservaControllerException {

    private final Long bookingId;

    /**
     * /**
     * Creates a new instance of <code>ReservaDoesntExistException</code>
     * without detail message.
     *
     * @param bookingId Id of the booking that doesn't exist.
     */
    public ReservaDoesntExistException(long bookingId) {
        super(String.format("No existe ninguna reserva con el ID %d", bookingId));
        this.bookingId = bookingId;
    }
}
