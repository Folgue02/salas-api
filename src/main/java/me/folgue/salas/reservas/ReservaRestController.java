package me.folgue.salas.reservas;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.java.Log;
import me.folgue.salas.reservas.exceptions.ReservaControllerException;
import me.folgue.salas.reservas.exceptions.ReservaDoesntExistException;
import me.folgue.salas.salas.SalaRepository;
import me.folgue.salas.salas.Sala;
import me.folgue.salas.salas.exceptions.SalaControllerException;
import me.folgue.salas.salas.exceptions.SalaDoesntExistException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservas/")
@Log
public class ReservaRestController {

    private final ReservaRepository repository;
    private final SalaRepository salaRepository;

    public ReservaRestController(ReservaRepository repository, SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
        this.repository = repository;
    }

    @GetMapping("/")
    public List<Reserva> getAll() {
        return this.repository.findAll();
    }

    @PostMapping("/")
    public Reserva createBooking(
            @RequestParam String organizer,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm") LocalDateTime endDate,
            @RequestParam Long roomId
    ) throws SalaControllerException {
        Sala room = this.salaRepository.findById(roomId).orElseThrow(() -> new SalaDoesntExistException(roomId));
        Reserva booking = new Reserva(organizer, startDate, endDate, room);
        this.repository.save(booking);
        log.info(String.format("New booking for room with id '%d' for organizer '%s' created.", roomId, organizer));
        return booking;
    }

    @GetMapping("/{id}")
    public Reserva getBookingById(@PathVariable Long bookingId) throws ReservaControllerException {
        return this.repository.findById(bookingId).orElseThrow(() -> new ReservaDoesntExistException(bookingId));
    }

    @PutMapping("/{id}")
    public Reserva updateBooking(
            @PathVariable Long bookingId,
            @RequestParam(required = false) Optional<String> organizer,
            @RequestParam(required = false) Optional<LocalDateTime> startDate,
            @RequestParam(required = false) Optional<LocalDateTime> endDate,
            @RequestParam(required = false) Optional<Long> roomId
    ) throws SalaControllerException, ReservaControllerException {
        Reserva booking = this.repository.findById(bookingId).orElseThrow(() -> new ReservaDoesntExistException(bookingId));
        if (roomId.isPresent()) {
            Sala room = this.salaRepository.findById(roomId.get()).orElseThrow(() -> new SalaDoesntExistException(roomId.get()));
            booking.setRoom(room);
        }
        booking.setOrganizer(organizer.orElse(booking.getOrganizer()));
        booking.setStartDate(startDate.orElse(booking.getStartDate()));
        booking.setEndDate(endDate.orElse(booking.getEndDate()));

        return booking;
    }

    // TODO: Create exception handlers
}
