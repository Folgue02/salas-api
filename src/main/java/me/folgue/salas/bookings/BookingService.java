package me.folgue.salas.bookings;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository repository) {
        this.bookingRepository = repository;
    }

    /**
     * @return A list containing all the bookings in the database.
     */
    public List<Booking> getAll() {
        return this.bookingRepository.findAll();
    }

    /**
     * Looks for a booking with the given id and returns it.
     *
     * @param bookingId Id of the booking to look for.
     * @return An empty {@link Optional} if no room was found, or an
     * {@link Optional} with the booking of the id specified.
     */
    public Optional<Booking> findById(long bookingId) {
        return this.bookingRepository.findById(bookingId);
    }

    /**
     * Saves the instance of the booking to the database.
     *
     * @param booking Booking to be saved.
     * @return The booking saved by the repository.
     */
    public Booking save(Booking booking) {
        return this.bookingRepository.save(booking);
    }

    /**
     * Attempts to delete a booking with the id {@code bookingId}, if it doesn't
     * exist, or couldn't be deleted, this function will silently be
     * ignored(<i>No exception thrown</i>).
     *
     * @param bookingId Id of the booking to be removed.
     */
    public void delete(long bookingId) {
        this.bookingRepository.deleteById(bookingId);
    }

    /**
     * Returns all the bookings associated with a room of the given id.
     *
     * @param roomId Id of the room of the bookings to be returned.
     * @see getBookingsForRoomInRange
     * @return A list of the bookings related to the room.
     */
    public List<Booking> getBookingsForRoom(long roomId) {
        return this.bookingRepository.findByRoomId(roomId);
    }

    /**
     * Returns all the bookings associated with the room of the given id, that
     * take place between {@code startDate} and {@code endDate}.
     *
     * @param roomId Id of the room of the bookings to be returned.
     * @param startDate Start of the range of time.
     * @param endDate End of the range of time.
     * @see getBookingsForRoom
     * @return A list of all the bookings that take place between
     * {@code startDate} and {@code endDate} for the room with the id
     * {@code roomId}.
     */
    public List<Booking> getBookingsForRoomInRange(long roomId, LocalDateTime startDate, LocalDateTime endDate) {
        return this.getBookingsForRoom(roomId).stream()
                .filter(b -> BookingUtils.isDateRangeInRange(startDate, endDate, b.getStartDate(), b.getEndDate()))
                .toList();
    }
}
