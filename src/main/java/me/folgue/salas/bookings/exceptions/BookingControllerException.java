package me.folgue.salas.bookings.exceptions;

/**
 *
 * @author folgue
 */
public abstract class BookingControllerException extends Exception {

    public BookingControllerException(String msg) {
        super(msg);
    }
}
