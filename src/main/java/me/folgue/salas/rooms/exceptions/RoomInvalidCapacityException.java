package me.folgue.salas.rooms.exceptions;

/**
 *
 * @author folgue
 */
public class RoomInvalidCapacityException extends RoomControllerException {

    private final Integer invalidCapacity;

    public RoomInvalidCapacityException(int invalidCapacity) {
        super(String.format("Invalid capacity specified: %d", invalidCapacity));
        this.invalidCapacity = invalidCapacity;
    }

    public int getInvalidCapacity() {
        return this.invalidCapacity;
    }
}
