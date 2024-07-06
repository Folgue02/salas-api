package me.folgue.salas.salas;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author folgue
 */
@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {

    List<Sala> findByName(String name);

    Optional<Sala> findBySalaID(Long id);

}
