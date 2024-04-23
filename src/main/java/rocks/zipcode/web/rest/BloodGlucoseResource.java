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
import rocks.zipcode.domain.BloodGlucose;
import rocks.zipcode.repository.BloodGlucoseRepository;
import rocks.zipcode.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link rocks.zipcode.domain.BloodGlucose}.
 */
@RestController
@RequestMapping("/api/blood-glucoses")
@Transactional
public class BloodGlucoseResource {

    private final Logger log = LoggerFactory.getLogger(BloodGlucoseResource.class);

    private static final String ENTITY_NAME = "bloodGlucose";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BloodGlucoseRepository bloodGlucoseRepository;

    public BloodGlucoseResource(BloodGlucoseRepository bloodGlucoseRepository) {
        this.bloodGlucoseRepository = bloodGlucoseRepository;
    }

    /**
     * {@code POST  /blood-glucoses} : Create a new bloodGlucose.
     *
     * @param bloodGlucose the bloodGlucose to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bloodGlucose, or with status {@code 400 (Bad Request)} if the bloodGlucose has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BloodGlucose> createBloodGlucose(@RequestBody BloodGlucose bloodGlucose) throws URISyntaxException {
        log.debug("REST request to save BloodGlucose : {}", bloodGlucose);
        if (bloodGlucose.getId() != null) {
            throw new BadRequestAlertException("A new bloodGlucose cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bloodGlucose = bloodGlucoseRepository.save(bloodGlucose);
        return ResponseEntity.created(new URI("/api/blood-glucoses/" + bloodGlucose.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, bloodGlucose.getId().toString()))
            .body(bloodGlucose);
    }

    /**
     * {@code PUT  /blood-glucoses/:id} : Updates an existing bloodGlucose.
     *
     * @param id the id of the bloodGlucose to save.
     * @param bloodGlucose the bloodGlucose to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bloodGlucose,
     * or with status {@code 400 (Bad Request)} if the bloodGlucose is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bloodGlucose couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BloodGlucose> updateBloodGlucose(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BloodGlucose bloodGlucose
    ) throws URISyntaxException {
        log.debug("REST request to update BloodGlucose : {}, {}", id, bloodGlucose);
        if (bloodGlucose.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bloodGlucose.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bloodGlucoseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bloodGlucose = bloodGlucoseRepository.save(bloodGlucose);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bloodGlucose.getId().toString()))
            .body(bloodGlucose);
    }

    /**
     * {@code PATCH  /blood-glucoses/:id} : Partial updates given fields of an existing bloodGlucose, field will ignore if it is null
     *
     * @param id the id of the bloodGlucose to save.
     * @param bloodGlucose the bloodGlucose to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bloodGlucose,
     * or with status {@code 400 (Bad Request)} if the bloodGlucose is not valid,
     * or with status {@code 404 (Not Found)} if the bloodGlucose is not found,
     * or with status {@code 500 (Internal Server Error)} if the bloodGlucose couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BloodGlucose> partialUpdateBloodGlucose(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BloodGlucose bloodGlucose
    ) throws URISyntaxException {
        log.debug("REST request to partial update BloodGlucose partially : {}, {}", id, bloodGlucose);
        if (bloodGlucose.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bloodGlucose.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bloodGlucoseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BloodGlucose> result = bloodGlucoseRepository
            .findById(bloodGlucose.getId())
            .map(existingBloodGlucose -> {
                if (bloodGlucose.getMeasurement() != null) {
                    existingBloodGlucose.setMeasurement(bloodGlucose.getMeasurement());
                }
                if (bloodGlucose.getMeasurementContent() != null) {
                    existingBloodGlucose.setMeasurementContent(bloodGlucose.getMeasurementContent());
                }
                if (bloodGlucose.getMeasurementType() != null) {
                    existingBloodGlucose.setMeasurementType(bloodGlucose.getMeasurementType());
                }

                return existingBloodGlucose;
            })
            .map(bloodGlucoseRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bloodGlucose.getId().toString())
        );
    }

    /**
     * {@code GET  /blood-glucoses} : get all the bloodGlucoses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bloodGlucoses in body.
     */
    @GetMapping("")
    public List<BloodGlucose> getAllBloodGlucoses() {
        log.debug("REST request to get all BloodGlucoses");
        return bloodGlucoseRepository.findAll();
    }

    /**
     * {@code GET  /blood-glucoses/:id} : get the "id" bloodGlucose.
     *
     * @param id the id of the bloodGlucose to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bloodGlucose, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BloodGlucose> getBloodGlucose(@PathVariable("id") Long id) {
        log.debug("REST request to get BloodGlucose : {}", id);
        Optional<BloodGlucose> bloodGlucose = bloodGlucoseRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(bloodGlucose);
    }

    /**
     * {@code DELETE  /blood-glucoses/:id} : delete the "id" bloodGlucose.
     *
     * @param id the id of the bloodGlucose to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBloodGlucose(@PathVariable("id") Long id) {
        log.debug("REST request to delete BloodGlucose : {}", id);
        bloodGlucoseRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
