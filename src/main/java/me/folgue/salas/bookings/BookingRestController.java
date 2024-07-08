package me.folgue.salas.bookings;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.java.Log;
import me.folgue.salas.bookings.exceptions.BookingConflictException;
import me.folgue.salas.bookings.exceptions.BookingInvalidDatesException;
import me.folgue.salas.bookings.exceptions.BookingControllerException;
import me.folgue.salas.bookings.exceptions.BookingDoesntExistException;
import me.folgue.salas.rooms.RoomService;
import me.folgue.salas.rooms.Room;
import me.folgue.salas.rooms.exceptions.RoomControllerException;
import me.folgue.salas.rooms.exceptions.RoomDoesntExistException;
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

    private final RoomService roomService;
    private final BookingService bookingService;

    public BookingRestController(RoomService roomService, BookingService bookingService) {
        this.roomService = roomService;
        this.bookingService = bookingService;
    }

    /**
     *
     * @return A list containing all the bookings stored in the database.
     */
    @GetMapping("/")
    public List<Booking> getAll() {
        return this.bookingService.getAll();
    }

    /**
     * Route that can be used to create booking associated with a room.
     *
     * @param organizer Organizer of the room.
     * @param startDate Start of the booking.
     * @param endDate End of the booking.
     * @param roomId Id of the room to book.
     * @return The booking object generated.
     * @throws RoomControllerException If there is no room with such ID.
     * @throws BookingControllerException If the range of dates for the booking
     * are invalid, or there is a conflict with a different booking of the same
     * room.
     */
    @PostMapping("/")
    public Booking createBooking(
            @RequestParam String organizer,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm") LocalDateTime endDate,
            @RequestParam Long roomId
    ) throws RoomControllerException, BookingControllerException {
        Room room = this.roomService.findRoomById(roomId).orElseThrow(() -> new RoomDoesntExistException(roomId));
        Booking booking = new Booking(organizer, startDate, endDate, room);

        if (!startDate.isBefore(endDate)) {
            throw new BookingInvalidDatesException(startDate, endDate);
        }

        List<Booking> conflictedBookings = this.bookingService.getBookingsForRoomInRange(room.getId(), startDate, endDate);

        if (!conflictedBookings.isEmpty()) {
            throw new BookingConflictException(startDate, endDate, conflictedBookings.get(0).getStartDate(), conflictedBookings.get(0).getEndDate(), roomId);
        }

        booking = this.bookingService.save(booking);
        log.info(String.format("New booking (id '%d') for room with id '%d' for organizer '%s' created.", booking.getId(), roomId, organizer));
        return booking;
    }

    /**
     * Returns the room associated with the id specified.
     *
     * @param bookingId Id of the booking to be returned.
     * @return Booking associated with the id specified.
     * @throws BookingControllerException If there is no booking with such id.
     */
    @GetMapping("/{bookingId}")
    public Booking getBookingById(@PathVariable Long bookingId) throws BookingControllerException {
        return this.bookingService.findById(bookingId).orElseThrow(() -> new BookingDoesntExistException(bookingId));
    }

    /**
     * Updates a previously created booking with the ID given.
     *
     * @param bookingId ID of the booking to be updated.
     * @param organizer New organizer of the booking.
     * @param startDateOptional New start of the booking.
     * @param endDateOptional New end of the booking.
     * @param roomId ID of the new room be booked.
     * @return
     * @throws RoomControllerException If the new room specified doesn't exist.
     * @throws BookingControllerException If the new dates have a conflict with
     * a different booking for the same room or the date range is invalid.
     */
    @PutMapping("/{bookingId}")
    public Booking updateBooking(
            @PathVariable Long bookingId,
            @RequestParam(required = false) String organizer,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm") LocalDateTime startDateOptional,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm") LocalDateTime endDateOptional,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm") Long roomId
    ) throws RoomControllerException, BookingControllerException {
        Booking booking = this.bookingService.findById(bookingId).orElseThrow(() -> new BookingDoesntExistException(bookingId));
        LocalDateTime startDate = startDateOptional == null ? booking.getStartDate() : startDateOptional;
        LocalDateTime endDate = endDateOptional == null ? booking.getEndDate() : endDateOptional;

        Room room;

        if (roomId != null) {
            room = this.roomService.findRoomById(roomId).orElseThrow(() -> new RoomDoesntExistException(roomId));
        } else {
            room = booking.getRoom();
        }

        List<Booking> conflictedBookings = this.bookingService.getBookingsForRoomInRange(room.getId(), startDate, endDate)
                .stream()
                .filter(b -> !b.getId().equals(room.getId()))
                .toList();

        if (!conflictedBookings.isEmpty()) {
            throw new BookingConflictException(
                    startDate,
                    endDate,
                    conflictedBookings.get(0).getStartDate(),
                    conflictedBookings.get(0).getEndDate(),
                    room.getId()
            );
        }

        booking.setRoom(room);
        if (!startDate.isBefore(endDate)) {
            throw new BookingInvalidDatesException(startDate, endDate);
        }

        booking.setOrganizer(organizer == null ? booking.getOrganizer() : organizer);
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);

        return booking;
    }

    /**
     * Deletes a booking.
     *
     * @param bookingId Id of the booking to be removed.
     * @return The booking object that was removed from the database.
     * @throws BookingControllerException
     */
    @DeleteMapping("/{bookingId}")
    public Booking deleteBooking(@PathVariable Long bookingId) throws BookingControllerException {
        Booking reserva = this.bookingService.findById(bookingId).orElseThrow(() -> new BookingDoesntExistException(bookingId));
        this.bookingService.delete(reserva.getId());
        return reserva;
    }

    /**
     * @param roomId Id of the booking to be returned.
     * @return A list containing all the bookings related to the room specified.
     * <b>NOTE</b>: This function doesn't check if the room specified doesn't
     * exist, it just returns bookings related to rooms with that id.
     */
    @GetMapping("/sala/{roomId}")
    public List<Booking> getBookingByRoomId(@PathVariable Long roomId) {
        // TODO: Additional?
        return this.bookingService.getBookingsForRoom(roomId);
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
    public boolean isRoomAvailable(Room room, LocalDateTime startDate, LocalDateTime endDate) {
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

    @ExceptionHandler(RoomDoesntExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleRoomDoesntExist(RoomDoesntExistException e) {
        return e.getMessage();
    }

    @ExceptionHandler(BookingConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleBookingConflict(BookingConflictException e) {
        return e.getMessage();
    }
}
