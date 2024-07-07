package me.folgue.salas.reservas;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author folgue
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    public Optional<Booking> findById(Long id);
}
