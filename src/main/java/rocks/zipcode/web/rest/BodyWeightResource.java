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
import rocks.zipcode.domain.BodyWeight;
import rocks.zipcode.repository.BodyWeightRepository;
import rocks.zipcode.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link rocks.zipcode.domain.BodyWeight}.
 */
@RestController
@RequestMapping("/api/body-weights")
@Transactional
public class BodyWeightResource {

    private final Logger log = LoggerFactory.getLogger(BodyWeightResource.class);

    private static final String ENTITY_NAME = "bodyWeight";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BodyWeightRepository bodyWeightRepository;

    public BodyWeightResource(BodyWeightRepository bodyWeightRepository) {
        this.bodyWeightRepository = bodyWeightRepository;
    }

    /**
     * {@code POST  /body-weights} : Create a new bodyWeight.
     *
     * @param bodyWeight the bodyWeight to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bodyWeight, or with status {@code 400 (Bad Request)} if the bodyWeight has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BodyWeight> createBodyWeight(@RequestBody BodyWeight bodyWeight) throws URISyntaxException {
        log.debug("REST request to save BodyWeight : {}", bodyWeight);
        if (bodyWeight.getId() != null) {
            throw new BadRequestAlertException("A new bodyWeight cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bodyWeight = bodyWeightRepository.save(bodyWeight);
        return ResponseEntity.created(new URI("/api/body-weights/" + bodyWeight.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, bodyWeight.getId().toString()))
            .body(bodyWeight);
    }

    /**
     * {@code PUT  /body-weights/:id} : Updates an existing bodyWeight.
     *
     * @param id the id of the bodyWeight to save.
     * @param bodyWeight the bodyWeight to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bodyWeight,
     * or with status {@code 400 (Bad Request)} if the bodyWeight is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bodyWeight couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BodyWeight> updateBodyWeight(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BodyWeight bodyWeight
    ) throws URISyntaxException {
        log.debug("REST request to update BodyWeight : {}, {}", id, bodyWeight);
        if (bodyWeight.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bodyWeight.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bodyWeightRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bodyWeight = bodyWeightRepository.save(bodyWeight);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bodyWeight.getId().toString()))
            .body(bodyWeight);
    }

    /**
     * {@code PATCH  /body-weights/:id} : Partial updates given fields of an existing bodyWeight, field will ignore if it is null
     *
     * @param id the id of the bodyWeight to save.
     * @param bodyWeight the bodyWeight to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bodyWeight,
     * or with status {@code 400 (Bad Request)} if the bodyWeight is not valid,
     * or with status {@code 404 (Not Found)} if the bodyWeight is not found,
     * or with status {@code 500 (Internal Server Error)} if the bodyWeight couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BodyWeight> partialUpdateBodyWeight(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BodyWeight bodyWeight
    ) throws URISyntaxException {
        log.debug("REST request to partial update BodyWeight partially : {}, {}", id, bodyWeight);
        if (bodyWeight.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bodyWeight.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bodyWeightRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BodyWeight> result = bodyWeightRepository
            .findById(bodyWeight.getId())
            .map(existingBodyWeight -> {
                if (bodyWeight.getWeight() != null) {
                    existingBodyWeight.setWeight(bodyWeight.getWeight());
                }

                return existingBodyWeight;
            })
            .map(bodyWeightRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bodyWeight.getId().toString())
        );
    }

    /**
     * {@code GET  /body-weights} : get all the bodyWeights.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bodyWeights in body.
     */
    @GetMapping("")
    public List<BodyWeight> getAllBodyWeights() {
        log.debug("REST request to get all BodyWeights");
        return bodyWeightRepository.findAll();
    }

    /**
     * {@code GET  /body-weights/:id} : get the "id" bodyWeight.
     *
     * @param id the id of the bodyWeight to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bodyWeight, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BodyWeight> getBodyWeight(@PathVariable("id") Long id) {
        log.debug("REST request to get BodyWeight : {}", id);
        Optional<BodyWeight> bodyWeight = bodyWeightRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(bodyWeight);
    }

    /**
     * {@code DELETE  /body-weights/:id} : delete the "id" bodyWeight.
     *
     * @param id the id of the bodyWeight to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBodyWeight(@PathVariable("id") Long id) {
        log.debug("REST request to delete BodyWeight : {}", id);
        bodyWeightRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
