package rocks.zipcode.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rocks.zipcode.domain.BodyHeight;

/**
 * Spring Data JPA repository for the BodyHeight entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BodyHeightRepository extends JpaRepository<BodyHeight, Long> {}
