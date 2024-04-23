package rocks.zipcode.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rocks.zipcode.domain.BloodGlucose;

/**
 * Spring Data JPA repository for the BloodGlucose entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BloodGlucoseRepository extends JpaRepository<BloodGlucose, Long> {}
