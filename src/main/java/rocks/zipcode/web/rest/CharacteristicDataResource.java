package rocks.zipcode.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import rocks.zipcode.domain.CharacteristicData;
import rocks.zipcode.repository.CharacteristicDataRepository;
import rocks.zipcode.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link rocks.zipcode.domain.CharacteristicData}.
 */
@RestController
@RequestMapping("/api/characteristic-data")
@Transactional
public class CharacteristicDataResource {

    private final Logger log = LoggerFactory.getLogger(CharacteristicDataResource.class);

    private static final String ENTITY_NAME = "characteristicData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CharacteristicDataRepository characteristicDataRepository;

    public CharacteristicDataResource(CharacteristicDataRepository characteristicDataRepository) {
        this.characteristicDataRepository = characteristicDataRepository;
    }

    /**
     * {@code POST  /characteristic-data} : Create a new characteristicData.
     *
     * @param characteristicData the characteristicData to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new characteristicData, or with status {@code 400 (Bad Request)} if the characteristicData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CharacteristicData> createCharacteristicData(@RequestBody CharacteristicData characteristicData)
        throws URISyntaxException {
        log.debug("REST request to save CharacteristicData : {}", characteristicData);
        if (characteristicData.getId() != null) {
            throw new BadRequestAlertException("A new characteristicData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        characteristicData = characteristicDataRepository.save(characteristicData);
        return ResponseEntity.created(new URI("/api/characteristic-data/" + characteristicData.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, characteristicData.getId().toString()))
            .body(characteristicData);
    }

    /**
     * {@code PUT  /characteristic-data/:id} : Updates an existing characteristicData.
     *
     * @param id the id of the characteristicData to save.
     * @param characteristicData the characteristicData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated characteristicData,
     * or with status {@code 400 (Bad Request)} if the characteristicData is not valid,
     * or with status {@code 500 (Internal Server Error)} if the characteristicData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CharacteristicData> updateCharacteristicData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CharacteristicData characteristicData
    ) throws URISyntaxException {
        log.debug("REST request to update CharacteristicData : {}, {}", id, characteristicData);
        if (characteristicData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, characteristicData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!characteristicDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        characteristicData = characteristicDataRepository.save(characteristicData);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, characteristicData.getId().toString()))
            .body(characteristicData);
    }

    /**
     * {@code PATCH  /characteristic-data/:id} : Partial updates given fields of an existing characteristicData, field will ignore if it is null
     *
     * @param id the id of the characteristicData to save.
     * @param characteristicData the characteristicData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated characteristicData,
     * or with status {@code 400 (Bad Request)} if the characteristicData is not valid,
     * or with status {@code 404 (Not Found)} if the characteristicData is not found,
     * or with status {@code 500 (Internal Server Error)} if the characteristicData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CharacteristicData> partialUpdateCharacteristicData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CharacteristicData characteristicData
    ) throws URISyntaxException {
        log.debug("REST request to partial update CharacteristicData partially : {}, {}", id, characteristicData);
        if (characteristicData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, characteristicData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!characteristicDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CharacteristicData> result = characteristicDataRepository
            .findById(characteristicData.getId())
            .map(existingCharacteristicData -> {
                if (characteristicData.getDateOfBirth() != null) {
                    existingCharacteristicData.setDateOfBirth(characteristicData.getDateOfBirth());
                }
                if (characteristicData.getGender() != null) {
                    existingCharacteristicData.setGender(characteristicData.getGender());
                }
                if (characteristicData.getBloodType() != null) {
                    existingCharacteristicData.setBloodType(characteristicData.getBloodType());
                }

                return existingCharacteristicData;
            })
            .map(characteristicDataRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, characteristicData.getId().toString())
        );
    }

    /**
     * {@code GET  /characteristic-data} : get all the characteristicData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of characteristicData in body.
     */
    @GetMapping("")
    public List<CharacteristicData> getAllCharacteristicData() {
        log.debug("REST request to get all CharacteristicData");
        return characteristicDataRepository.findAll();
    }

    /**
     * {@code GET  /characteristic-data/:id} : get the "id" characteristicData.
     *
     * @param id the id of the characteristicData to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the characteristicData, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CharacteristicData> getCharacteristicData(@PathVariable("id") Long id) {
        log.debug("REST request to get CharacteristicData : {}", id);
        Optional<CharacteristicData> characteristicData = characteristicDataRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(characteristicData);
    }

    /**
     * {@code DELETE  /characteristic-data/:id} : delete the "id" characteristicData.
     *
     * @param id the id of the characteristicData to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharacteristicData(@PathVariable("id") Long id) {
        log.debug("REST request to delete CharacteristicData : {}", id);
        characteristicDataRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
