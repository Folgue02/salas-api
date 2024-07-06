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

    private final SalaRepository salaRepository;

    public SalaRestController(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
    }

    @GetMapping("/")
    public List<Sala> getAllSalas() {
        return this.salaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Sala getSalaById(@PathVariable("id") Long salaID) throws Exception {
        log.info(String.format("Attemping to check room with ID %d", salaID));
        return this.salaRepository.findBySalaID(salaID).orElseThrow(() -> new SalaDoesntExistException(salaID));
    }

    @PostMapping("/")
    public Sala createSala(@RequestParam String name, @RequestParam Integer capacity, @RequestParam String location) throws SalaControllerException {
        var sala = new Sala(name, capacity, location);
        this.salaRepository.save(sala);
        return sala;
    }

    @DeleteMapping("/{id}")
    public void deleteSala(@PathVariable("id") Long salaID) throws Exception {
        this.salaRepository.delete(
                this.salaRepository.findBySalaID(salaID)
                        .orElseThrow(() -> new SalaDoesntExistException(salaID))
        );
    }

    @PutMapping("/{id}")
    public Sala updateSala(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(required = false) String location
    ) throws SalaControllerException {
        Sala sala = this.salaRepository.findById(id).orElseThrow(() -> new SalaDoesntExistException(id));

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

        log.info(String.format("Updating room with id '%d', new name '%s', new capacity'%s' and new location '%s'", id, name, capacity, location));
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
}
