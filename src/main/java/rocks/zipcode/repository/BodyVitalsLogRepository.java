package rocks.zipcode.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rocks.zipcode.domain.BodyVitalsLog;

/**
 * Spring Data JPA repository for the BodyVitalsLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BodyVitalsLogRepository extends JpaRepository<BodyVitalsLog, Long> {}
