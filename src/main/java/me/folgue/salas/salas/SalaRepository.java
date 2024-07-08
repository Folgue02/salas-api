package me.folgue.salas.salas;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author folgue
 */
@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {

    Set<Sala> findByName(String name);

    @Override
    Optional<Sala> findById(Long id);

}
