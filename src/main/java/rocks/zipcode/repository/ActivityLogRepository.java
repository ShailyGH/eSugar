package rocks.zipcode.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rocks.zipcode.domain.ActivityLog;

/**
 * Spring Data JPA repository for the ActivityLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {}
