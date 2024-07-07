package me.folgue.salas.salas;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.annotation.Nonnull;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.folgue.salas.reservas.Booking;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salaID;

    @Nonnull
    /**
     * Name of the room.
     */
    private String name;

    @Nonnull
    /**
     * Number of people that the room can hold during a meeting.
     */
    private Integer capacity;

    @Nonnull
    /**
     * Information on where the room is located, formed by a letter and a digit.
     */
    private String location;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Booking> bookings;

    /**
     * Creates an instance of a {@link Sala} with its ID set to 0.
     *
     * @param name Name of the room.
     * @param capacity Number of people that the room can hold during a meeting.
     * @param location The location of the room.
     */
    public Sala(String name, int capacity, String location) {
        this.salaID = 0L;
        this.name = name;
        this.capacity = capacity;
        this.location = location;
    }

    /**
     * Checks if the location of this {@link Sala} instance is valid.
     *
     * @see isValidLocation
     * @return <code>true</code> if it is, <code>false</code> if it isn't.
     */
    public boolean hasValidLocation() {
        return isValidLocation(this.location);
    }

    /**
     * Checks if the given location string is valid (<i>one character + one
     * digit</i>)
     *
     * @param location Location string to be checked.
     * @return <code>true</code> if the location given has a well formed format,
     * <code>false</code> otherwise.
     */
    public static boolean isValidLocation(String location) {
        if (location.length() != 2) {
            return false;
        }

        return Character.isLetter(location.charAt(0)) && Character.isDigit(location.charAt(1));
    }
}
