package me.folgue.salas.salas.exceptions;

/**
 *
 * @author folgue
 */
public abstract class SalaControllerException extends Exception {

    /**
     * Creates a new instance of <code>SalaControllerException</code> without
     * detail message.
     */
    public SalaControllerException() {
    }

    /**
     * Constructs an instance of <code>SalaControllerException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public SalaControllerException(String msg) {
        super(msg);
    }
}
