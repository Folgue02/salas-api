package me.folgue.salas.reservas;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author folgue
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Override
    public Optional<Booking> findById(Long id);

    @Query("""
           SELECT b FROM Booking b
           WHERE b.room.id = :roomId
           """)
    public List<Booking> findByRoomId(@Param("roomId") Long roomId);
}
