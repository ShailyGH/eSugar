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
import rocks.zipcode.domain.BodyHeight;
import rocks.zipcode.repository.BodyHeightRepository;
import rocks.zipcode.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link rocks.zipcode.domain.BodyHeight}.
 */
@RestController
@RequestMapping("/api/body-heights")
@Transactional
public class BodyHeightResource {

    private final Logger log = LoggerFactory.getLogger(BodyHeightResource.class);

    private static final String ENTITY_NAME = "bodyHeight";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BodyHeightRepository bodyHeightRepository;

    public BodyHeightResource(BodyHeightRepository bodyHeightRepository) {
        this.bodyHeightRepository = bodyHeightRepository;
    }

    /**
     * {@code POST  /body-heights} : Create a new bodyHeight.
     *
     * @param bodyHeight the bodyHeight to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bodyHeight, or with status {@code 400 (Bad Request)} if the bodyHeight has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BodyHeight> createBodyHeight(@RequestBody BodyHeight bodyHeight) throws URISyntaxException {
        log.debug("REST request to save BodyHeight : {}", bodyHeight);
        if (bodyHeight.getId() != null) {
            throw new BadRequestAlertException("A new bodyHeight cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bodyHeight = bodyHeightRepository.save(bodyHeight);
        return ResponseEntity.created(new URI("/api/body-heights/" + bodyHeight.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, bodyHeight.getId().toString()))
            .body(bodyHeight);
    }

    /**
     * {@code PUT  /body-heights/:id} : Updates an existing bodyHeight.
     *
     * @param id the id of the bodyHeight to save.
     * @param bodyHeight the bodyHeight to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bodyHeight,
     * or with status {@code 400 (Bad Request)} if the bodyHeight is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bodyHeight couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BodyHeight> updateBodyHeight(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BodyHeight bodyHeight
    ) throws URISyntaxException {
        log.debug("REST request to update BodyHeight : {}, {}", id, bodyHeight);
        if (bodyHeight.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bodyHeight.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bodyHeightRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bodyHeight = bodyHeightRepository.save(bodyHeight);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bodyHeight.getId().toString()))
            .body(bodyHeight);
    }

    /**
     * {@code PATCH  /body-heights/:id} : Partial updates given fields of an existing bodyHeight, field will ignore if it is null
     *
     * @param id the id of the bodyHeight to save.
     * @param bodyHeight the bodyHeight to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bodyHeight,
     * or with status {@code 400 (Bad Request)} if the bodyHeight is not valid,
     * or with status {@code 404 (Not Found)} if the bodyHeight is not found,
     * or with status {@code 500 (Internal Server Error)} if the bodyHeight couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BodyHeight> partialUpdateBodyHeight(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BodyHeight bodyHeight
    ) throws URISyntaxException {
        log.debug("REST request to partial update BodyHeight partially : {}, {}", id, bodyHeight);
        if (bodyHeight.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bodyHeight.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bodyHeightRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BodyHeight> result = bodyHeightRepository
            .findById(bodyHeight.getId())
            .map(existingBodyHeight -> {
                if (bodyHeight.getHeight() != null) {
                    existingBodyHeight.setHeight(bodyHeight.getHeight());
                }

                return existingBodyHeight;
            })
            .map(bodyHeightRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bodyHeight.getId().toString())
        );
    }

    /**
     * {@code GET  /body-heights} : get all the bodyHeights.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bodyHeights in body.
     */
    @GetMapping("")
    public List<BodyHeight> getAllBodyHeights() {
        log.debug("REST request to get all BodyHeights");
        return bodyHeightRepository.findAll();
    }

    /**
     * {@code GET  /body-heights/:id} : get the "id" bodyHeight.
     *
     * @param id the id of the bodyHeight to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bodyHeight, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BodyHeight> getBodyHeight(@PathVariable("id") Long id) {
        log.debug("REST request to get BodyHeight : {}", id);
        Optional<BodyHeight> bodyHeight = bodyHeightRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(bodyHeight);
    }

    /**
     * {@code DELETE  /body-heights/:id} : delete the "id" bodyHeight.
     *
     * @param id the id of the bodyHeight to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBodyHeight(@PathVariable("id") Long id) {
        log.debug("REST request to delete BodyHeight : {}", id);
        bodyHeightRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
