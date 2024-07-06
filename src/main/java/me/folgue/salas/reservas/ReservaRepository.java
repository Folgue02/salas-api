package me.folgue.salas.reservas;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author folgue
 */
@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    public Optional<Reserva> findById(Long id);
}
