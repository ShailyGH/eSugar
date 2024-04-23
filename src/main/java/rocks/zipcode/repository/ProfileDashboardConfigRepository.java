package rocks.zipcode.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rocks.zipcode.domain.ProfileDashboardConfig;

/**
 * Spring Data JPA repository for the ProfileDashboardConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfileDashboardConfigRepository extends JpaRepository<ProfileDashboardConfig, Long> {}
