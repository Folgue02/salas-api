package me.folgue.salas.salas.exceptions;

/**
 *
 * @author folgue
 */
public class SalaDoesntExistException extends SalaControllerException {

    private final Long salaID;

    /**
     * Creates a new instance of <code>SalaDoesntExistException</code>
     * specifying the id of the room that was tried to be created.
     *
     * @param salaID ID of the room that already exists.
     */
    public SalaDoesntExistException(long salaID) {
        super(String.format("La sala con el ID %d no existe.", salaID));
        this.salaID = salaID;
    }

    public long getSalaID() {
        return this.salaID;
    }
}
