package me.folgue.salas.rooms.exceptions;

/**
 *
 * @author folgue
 */
public class RoomAlreadyExistsException extends RoomControllerException {

    private final Long salaID;

    /**
     * Creates a new instance of <code>SalaAlreadyExistsException</code> without
     * detail message.
     *
     * @param salaID ID de la sala que se intento crear.
     */
    public RoomAlreadyExistsException(long salaID) {
        super(String.format("Ya existe una sala con el ID %d", salaID));
        this.salaID = salaID;
    }

    public long getSalaID() {
        return this.salaID;
    }
}
