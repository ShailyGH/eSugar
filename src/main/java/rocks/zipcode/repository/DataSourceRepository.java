package rocks.zipcode.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rocks.zipcode.domain.DataSource;

/**
 * Spring Data JPA repository for the DataSource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataSourceRepository extends JpaRepository<DataSource, Long> {}
