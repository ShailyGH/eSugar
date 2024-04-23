package rocks.zipcode.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rocks.zipcode.domain.CharacteristicData;

/**
 * Spring Data JPA repository for the CharacteristicData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CharacteristicDataRepository extends JpaRepository<CharacteristicData, Long> {}
