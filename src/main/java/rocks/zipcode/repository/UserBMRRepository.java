package rocks.zipcode.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rocks.zipcode.domain.UserBMR;

/**
 * Spring Data JPA repository for the UserBMR entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserBMRRepository extends JpaRepository<UserBMR, Long> {}
