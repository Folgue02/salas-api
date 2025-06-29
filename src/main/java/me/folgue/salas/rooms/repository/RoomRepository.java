package me.folgue.salas.rooms.repository;

import java.util.Optional;
import java.util.Set;

import me.folgue.salas.rooms.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author folgue
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Set<Room> findByName(String name);

    @Override
    Optional<Room> findById(Long id);

}
