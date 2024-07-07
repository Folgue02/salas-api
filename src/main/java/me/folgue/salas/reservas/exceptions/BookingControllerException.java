package me.folgue.salas.reservas.exceptions;

/**
 *
 * @author folgue
 */
public abstract class BookingControllerException extends Exception {

    public BookingControllerException(String msg) {
        super(msg);
    }
}
