package me.folgue.salas.rooms.exceptions;

import me.folgue.salas.rooms.Room;

/**
 * Represents the scenario where either a {@link Room} contains a location with
 * an invalid format, or location with invalid format has been specified.
 *
 * @author folgue
 */
public class RoomInvalidLocationException extends RoomControllerException {

    /**
     * The invalid location that caused the exception.
     */
    private final String invalidLocation;

    public RoomInvalidLocationException(String invalidLocation) {
        super(String.format("The specified location is invalid: %s", invalidLocation));
        this.invalidLocation = invalidLocation;
    }

    public String getInvalidLocation() {
        return this.invalidLocation;
    }
}
