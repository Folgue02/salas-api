package me.folgue.salas.reservas;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.java.Log;
import me.folgue.salas.reservas.exceptions.BookingConflictException;
import me.folgue.salas.reservas.exceptions.BookingInvalidDatesException;
import me.folgue.salas.reservas.exceptions.BookingControllerException;
import me.folgue.salas.reservas.exceptions.BookingDoesntExistException;
import me.folgue.salas.salas.SalaRepository;
import me.folgue.salas.salas.Sala;
import me.folgue.salas.salas.exceptions.SalaControllerException;
import me.folgue.salas.salas.exceptions.SalaDoesntExistException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservas/")
@Log
public class BookingRestController {

    private final BookingRepository repository;
    private final SalaRepository salaRepository;

    public BookingRestController(BookingRepository repository, SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
        this.repository = repository;
    }

    @GetMapping("/")
    public List<Booking> getAll() {
        return this.repository.findAll();
    }

    @PostMapping("/")
    public Booking createBooking(
            @RequestParam String organizer,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm") LocalDateTime endDate,
            @RequestParam Long roomId
    ) throws SalaControllerException, BookingControllerException {
        Sala room = this.salaRepository.findById(roomId).orElseThrow(() -> new SalaDoesntExistException(roomId));
        Booking booking = new Booking(organizer, startDate, endDate, room);

        if (!startDate.isBefore(endDate)) {
            throw new BookingInvalidDatesException(startDate, endDate);
        }

        List<Booking> conflictedBookings = this.getBookingsForRoomInRange(room, startDate, endDate);

        if (!conflictedBookings.isEmpty()) {
            throw new BookingConflictException(startDate, endDate, conflictedBookings.get(0).getStartDate(), conflictedBookings.get(0).getEndDate(), roomId);
        }

        this.repository.save(booking);
        log.info(String.format("New booking for room with id '%d' for organizer '%s' created.", roomId, organizer));
        return booking;
    }

    @GetMapping("/{id}")
    public Booking getBookingById(@PathVariable Long bookingId) throws BookingControllerException {
        return this.repository.findById(bookingId).orElseThrow(() -> new BookingDoesntExistException(bookingId));
    }

    @PutMapping("/{bookingId}")
    public Booking updateBooking(
            @PathVariable Long bookingId,
            @RequestParam(required = false) Optional<String> organizer,
            @RequestParam(required = false) Optional<LocalDateTime> startDateOptional,
            @RequestParam(required = false) Optional<LocalDateTime> endDateOptional,
            @RequestParam(required = false) Optional<Long> roomId
    ) throws SalaControllerException, BookingControllerException {
        Booking booking = this.repository.findById(bookingId).orElseThrow(() -> new BookingDoesntExistException(bookingId));
        LocalDateTime startDate = startDateOptional.orElse(booking.getStartDate());
        LocalDateTime endDate = endDateOptional.orElse(booking.getEndDate());

        Sala room;

        if (roomId.isPresent()) {
            room = this.salaRepository.findById(roomId.get()).orElseThrow(() -> new SalaDoesntExistException(roomId.get()));
        } else {
            room = booking.getRoom();
        }

        List<Booking> conflictedBookings = this.getBookingsForRoomInRange(room, startDate, endDate);

        if (!conflictedBookings.isEmpty()) {
            throw new BookingConflictException(startDate, endDate, conflictedBookings.get(0).getStartDate(), conflictedBookings.get(0).getEndDate(), room.getSalaID());
        }

        booking.setRoom(room);
        if (!startDate.isBefore(endDate)) {
            throw new BookingInvalidDatesException(startDate, endDate);
        }

        booking.setOrganizer(organizer.orElse(booking.getOrganizer()));
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);

        return booking;
    }

    @DeleteMapping("/{bookingId}")
    public Booking deleteBooking(@PathVariable Long bookingId) throws BookingControllerException {
        Booking reserva = this.repository.findById(bookingId).orElseThrow(() -> new BookingDoesntExistException(bookingId));
        this.repository.delete(reserva);
        return reserva;
    }

    /**
     * Returns the bookings that appear during the time range specified for the
     * room given.
     *
     * @param room Room to check.
     * @param startDate Start of the range of time to check.
     * @param endDate End of the range of time to check.
     * @see isRoomAvailable
     * @return A list containing <b>ALL</b> the bookings made for the given room
     * in the specified time period.
     */
    public List<Booking> getBookingsForRoomInRange(Sala room, LocalDateTime startDate, LocalDateTime endDate) {
        return room.getBookings().stream()
                .filter(b -> (b.getStartDate().isAfter(startDate) && b.getEndDate().isAfter(endDate))
                || (b.getStartDate().isAfter(endDate) && b.getEndDate().isBefore(endDate)))
                .toList();
    }

    /**
     * Checks if the specified room is available in the range of time given.
     *
     * @param room Room to check.
     * @param startDate Start of the range of time to check.
     * @param endDate End of the range of time to check.
     * @see getBookingsForRoomInRange
     * @return {@code true} if the room is booked between {@code startDate} and
     * {@code endDate}, otherwise, {@code false}
     */
    public boolean isRoomAvailable(Sala room, LocalDateTime startDate, LocalDateTime endDate) {
        return room.getBookings().stream()
                .noneMatch(b -> (b.getStartDate().isAfter(startDate) && b.getEndDate().isAfter(endDate))
                || (b.getStartDate().isAfter(endDate) && b.getEndDate().isBefore(endDate)));
    }

    @ExceptionHandler(BookingInvalidDatesException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidDates(BookingInvalidDatesException e) {
        return e.getMessage();
    }

    @ExceptionHandler(BookingDoesntExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleBookingNotFound(BookingDoesntExistException e) {
        return e.getMessage();
    }

    @ExceptionHandler(SalaDoesntExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleRoomDoesntExist(SalaDoesntExistException e) {
        return e.getMessage();
    }
    /*
    @ExceptionHandler(SalaDoesntExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleRoomDoesntExist(SalaDoesntExistException e) {
        return e.getMessage();
    }*/
}
