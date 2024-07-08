package me.folgue.salas.salas;

import java.util.List;
import lombok.extern.java.Log;
import me.folgue.salas.salas.exceptions.SalaAlreadyExistsException;
import me.folgue.salas.salas.exceptions.SalaControllerException;
import me.folgue.salas.salas.exceptions.SalaDoesntExistException;
import me.folgue.salas.salas.exceptions.SalaInvalidCapacityException;
import me.folgue.salas.salas.exceptions.SalaInvalidLocationException;
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
@RequestMapping(path = "/salas")
@Log
public class SalaRestController {

    private final RoomService service;

    public SalaRestController(RoomService service) {
        this.service = service;
    }

    /**
     * @return A list of all the rooms stored in the database.
     */
    @GetMapping("/")
    public List<Sala> getAllSalas() {
        return this.service.getAllRooms();
    }

    /**
     * Returns a room with the given id.
     *
     * @param roomId Id of the room to be returned.
     * @return Room associated with the id specified.
     * @throws SalaControllerException If there is no room with such id.
     */
    @GetMapping("/{id}")
    public Sala getRoomById(@PathVariable("id") Long roomId) throws SalaControllerException {
        log.info(String.format("Attemping to check room with ID %d", roomId));
        return this.service.findRoomById(roomId).orElseThrow(() -> new SalaDoesntExistException(roomId));
    }

    /**
     * Creates a new room with the information specified.
     *
     * @param name Name of the new room.
     * @param capacity Capacity of the new room.
     * @param location Location of the new room.
     * @return The room created by the function.
     * @see Sala
     * @throws SalaControllerException If the capacity number of the room is
     * invalid or the location doesn't follow the valid format ({@link Sala}).
     */
    @PostMapping("/")
    public Sala createSala(@RequestParam String name, @RequestParam Integer capacity, @RequestParam String location) throws SalaControllerException {
        if (!Sala.isValidLocation(location)) {
            throw new SalaInvalidLocationException(location);
        }

        if (capacity < 1) {
            throw new SalaInvalidCapacityException(capacity);
        }

        var room = new Sala(name, capacity, location);
        room = this.service.save(room);
        log.info(String.format("New room with id '%d' created.", room.getId()));
        return room;
    }

    /**
     * Removes the room with the given Id.
     *
     * @param roomId Id of the room to remove.
     * @return The room that has been removed.
     * @throws SalaControllerException If the room doesn't exist.
     */
    @DeleteMapping("/{id}")
    public Sala deleteSala(@PathVariable("id") Long roomId) throws SalaControllerException {
        Sala room = this.service.findRoomById(roomId)
                .orElseThrow(() -> new SalaDoesntExistException(roomId));
        this.service.delete(
                room.getId()
        );
        log.info(String.format("Room with id '%d' was removed.", room.getId()));
        return room;
    }

    /**
     * Updates a room of the given id with the new information.
     *
     * @param roomId Id of the room to update.
     * @param name New name of the room.
     * @param capacity New capacity of the room.
     * @param location New location of the room.
     * @return The new object of the room updated.
     * @throws SalaControllerException If there is no room with such id, the new
     * capacity is invalid, or the location's format is invalid.
     */
    @PutMapping("/{roomId}")
    public Sala updateSala(
            @PathVariable Long roomId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(required = false) String location
    ) throws SalaControllerException {
        Sala sala = this.service.findRoomById(roomId).orElseThrow(() -> new SalaDoesntExistException(roomId));

        if (name != null) {
            sala.setName(name);
        }

        if (capacity != null) {
            if (capacity < 1) {
                throw new SalaInvalidCapacityException(capacity);
            }
            sala.setCapacity(capacity);
        }

        if (location != null) {
            if (!Sala.isValidLocation(location)) {
                throw new SalaInvalidLocationException(location);
            }
            sala.setLocation(location);
        }

        log.info(String.format("Updating room with id '%d', new name '%s', new capacity'%s' and new location '%s'", roomId, name, capacity, location));
        sala = this.service.save(sala);
        return sala;
    }

    @ExceptionHandler(SalaAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public String handleAlreadyExistsException(SalaAlreadyExistsException e) {
        return String.format("Ya existe una sala de codigo %d", e.getSalaID());
    }

    @ExceptionHandler(SalaDoesntExistException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String handleDoesntExistsException(SalaDoesntExistException e) {
        return String.format("No existe una sala con el ID %d", e.getSalaID());
    }

    @ExceptionHandler(SalaInvalidLocationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleDoesntExistsException(SalaInvalidLocationException e) {
        return e.getMessage();
    }

    @ExceptionHandler(SalaInvalidCapacityException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleDoesntExistsException(SalaInvalidCapacityException e) {
        return e.getMessage();
    }

}
