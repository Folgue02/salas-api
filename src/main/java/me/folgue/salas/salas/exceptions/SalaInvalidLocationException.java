package me.folgue.salas.salas.exceptions;

import me.folgue.salas.salas.Sala;

/**
 * Represents the scenario where either a {@link Sala} contains a location with
 * an invalid format, or location with invalid format has been specified.
 *
 * @author folgue
 */
public class SalaInvalidLocationException extends SalaControllerException {

    /**
     * The invalid location that caused the exception.
     */
    private final String invalidLocation;

    public SalaInvalidLocationException(String invalidLocation) {
        super(String.format("The specified location is invalid: %s", invalidLocation));
        this.invalidLocation = invalidLocation;
    }

    public String getInvalidLocation() {
        return this.invalidLocation;
    }
}
