package me.folgue.salas.salas.exceptions;

/**
 *
 * @author folgue
 */
public class SalaInvalidCapacityException extends SalaControllerException {

    private final Integer invalidCapacity;

    public SalaInvalidCapacityException(int invalidCapacity) {
        super(String.format("Invalid capacity specified: %d", invalidCapacity));
        this.invalidCapacity = invalidCapacity;
    }

    public int getInvalidCapacity() {
        return this.invalidCapacity;
    }
}
