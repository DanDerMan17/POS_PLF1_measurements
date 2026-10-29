package at.kaindorf.plf_1.repositories;

import at.kaindorf.plf_1.entities.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
}