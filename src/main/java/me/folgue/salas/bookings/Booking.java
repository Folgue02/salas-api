package me.folgue.salas.bookings;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.folgue.salas.rooms.Room;

/**
 * Represents the booking of a {@link Room} in specified range of time.
 *
 * @see Room
 * @author Folgue02
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nonnull
    private String organizer;

    @Nonnull
    private LocalDateTime startDate;

    @Nonnull
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    public Booking(String organizer, LocalDateTime startDate, LocalDateTime endDate, Room room) {
        this.organizer = organizer;
        this.startDate = startDate;
        this.endDate = endDate;
        this.room = room;
    }
}
