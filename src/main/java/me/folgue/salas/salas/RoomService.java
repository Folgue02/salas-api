package me.folgue.salas.salas;

import java.util.List;
import java.util.Optional;
import me.folgue.salas.reservas.BookingRepository;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final SalaRepository repository;
    private final BookingRepository bookingRepository;

    public RoomService(SalaRepository repository, BookingRepository bookingRepository) {
        this.repository = repository;
        this.bookingRepository = bookingRepository;
    }

    /**
     * Looks for a room with the id specified.
     *
     * @param roomId Id of the room.
     * @return An {@link Optional} containing the room if it exists, if not, an
     * empty {@link Optional} gets returned.
     */
    public Optional<Sala> findRoomById(long roomId) {
        return this.repository.findById(roomId);
    }

    /**
     * @return A list containing all the rooms stored in the database.
     */
    public List<Sala> getAllRooms() {
        return this.repository.findAll();
    }

    /**
     * Saves the room object to the database.
     *
     * @param room Room to be saved in the database.
     * @return The room that was saved.
     */
    public Sala save(Sala room) {
        return this.repository.save(room);
    }

    /**
     * Deletes the room associated with the given {@code roomId}, if there is no
     * room with such {@code roomId}, this function won't do anything. <br>
     *
     * <b>NOTE: It also removes all bookings related to the room before removing
     * it.</b>
     *
     * @param roomId
     */
    public void delete(long roomId) {
        // Prevent attempting to remove bookings of a room that doesn't exist.
        if (!this.repository.existsById(roomId)) {
            return;
        }

        this.bookingRepository.findAll().stream()
                .filter(b -> b.getRoom().getId() == roomId)
                .forEach(this.bookingRepository::delete);

        this.repository.deleteById(roomId);
    }
}
