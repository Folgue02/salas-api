package me.folgue.salas.rooms.exceptions;

/**
 *
 * @author folgue
 */
public abstract class RoomControllerException extends Exception {

    /**
     * Creates a new instance of <code>SalaControllerException</code> without
     * detail message.
     */
    public RoomControllerException() {
    }

    /**
     * Constructs an instance of <code>SalaControllerException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomControllerException(String msg) {
        super(msg);
    }
}
