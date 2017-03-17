package be.vdab.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import be.vdab.entities.Filiaal;

public interface FiliaalRepository extends JpaRepository<Filiaal, Long> {

	List<Filiaal> findByAdresPostcodeBetweenOrderByNaam(int van, int tot);

}
