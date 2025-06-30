package me.folgue.salas.bookings.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.folgue.salas.rooms.model.Room;

/**
 * Represents the booking of a {@link Room} in specified range of time.
 *
 * @see Room
 * @author Folgue02
 */
@Entity
@Table(name = "bookings")
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
