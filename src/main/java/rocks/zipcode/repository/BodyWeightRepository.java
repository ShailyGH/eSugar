package rocks.zipcode.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rocks.zipcode.domain.BodyWeight;

/**
 * Spring Data JPA repository for the BodyWeight entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BodyWeightRepository extends JpaRepository<BodyWeight, Long> {}
