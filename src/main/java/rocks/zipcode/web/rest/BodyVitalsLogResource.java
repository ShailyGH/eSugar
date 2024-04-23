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
import rocks.zipcode.domain.BodyVitalsLog;
import rocks.zipcode.repository.BodyVitalsLogRepository;
import rocks.zipcode.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link rocks.zipcode.domain.BodyVitalsLog}.
 */
@RestController
@RequestMapping("/api/body-vitals-logs")
@Transactional
public class BodyVitalsLogResource {

    private final Logger log = LoggerFactory.getLogger(BodyVitalsLogResource.class);

    private static final String ENTITY_NAME = "bodyVitalsLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BodyVitalsLogRepository bodyVitalsLogRepository;

    public BodyVitalsLogResource(BodyVitalsLogRepository bodyVitalsLogRepository) {
        this.bodyVitalsLogRepository = bodyVitalsLogRepository;
    }

    /**
     * {@code POST  /body-vitals-logs} : Create a new bodyVitalsLog.
     *
     * @param bodyVitalsLog the bodyVitalsLog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bodyVitalsLog, or with status {@code 400 (Bad Request)} if the bodyVitalsLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BodyVitalsLog> createBodyVitalsLog(@RequestBody BodyVitalsLog bodyVitalsLog) throws URISyntaxException {
        log.debug("REST request to save BodyVitalsLog : {}", bodyVitalsLog);
        if (bodyVitalsLog.getId() != null) {
            throw new BadRequestAlertException("A new bodyVitalsLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bodyVitalsLog = bodyVitalsLogRepository.save(bodyVitalsLog);
        return ResponseEntity.created(new URI("/api/body-vitals-logs/" + bodyVitalsLog.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, bodyVitalsLog.getId().toString()))
            .body(bodyVitalsLog);
    }

    /**
     * {@code PUT  /body-vitals-logs/:id} : Updates an existing bodyVitalsLog.
     *
     * @param id the id of the bodyVitalsLog to save.
     * @param bodyVitalsLog the bodyVitalsLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bodyVitalsLog,
     * or with status {@code 400 (Bad Request)} if the bodyVitalsLog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bodyVitalsLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BodyVitalsLog> updateBodyVitalsLog(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BodyVitalsLog bodyVitalsLog
    ) throws URISyntaxException {
        log.debug("REST request to update BodyVitalsLog : {}, {}", id, bodyVitalsLog);
        if (bodyVitalsLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bodyVitalsLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bodyVitalsLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bodyVitalsLog = bodyVitalsLogRepository.save(bodyVitalsLog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bodyVitalsLog.getId().toString()))
            .body(bodyVitalsLog);
    }

    /**
     * {@code PATCH  /body-vitals-logs/:id} : Partial updates given fields of an existing bodyVitalsLog, field will ignore if it is null
     *
     * @param id the id of the bodyVitalsLog to save.
     * @param bodyVitalsLog the bodyVitalsLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bodyVitalsLog,
     * or with status {@code 400 (Bad Request)} if the bodyVitalsLog is not valid,
     * or with status {@code 404 (Not Found)} if the bodyVitalsLog is not found,
     * or with status {@code 500 (Internal Server Error)} if the bodyVitalsLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BodyVitalsLog> partialUpdateBodyVitalsLog(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BodyVitalsLog bodyVitalsLog
    ) throws URISyntaxException {
        log.debug("REST request to partial update BodyVitalsLog partially : {}, {}", id, bodyVitalsLog);
        if (bodyVitalsLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bodyVitalsLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bodyVitalsLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BodyVitalsLog> result = bodyVitalsLogRepository
            .findById(bodyVitalsLog.getId())
            .map(existingBodyVitalsLog -> {
                if (bodyVitalsLog.getDtCreated() != null) {
                    existingBodyVitalsLog.setDtCreated(bodyVitalsLog.getDtCreated());
                }

                return existingBodyVitalsLog;
            })
            .map(bodyVitalsLogRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bodyVitalsLog.getId().toString())
        );
    }

    /**
     * {@code GET  /body-vitals-logs} : get all the bodyVitalsLogs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bodyVitalsLogs in body.
     */
    @GetMapping("")
    public List<BodyVitalsLog> getAllBodyVitalsLogs() {
        log.debug("REST request to get all BodyVitalsLogs");
        return bodyVitalsLogRepository.findAll();
    }

    /**
     * {@code GET  /body-vitals-logs/:id} : get the "id" bodyVitalsLog.
     *
     * @param id the id of the bodyVitalsLog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bodyVitalsLog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BodyVitalsLog> getBodyVitalsLog(@PathVariable("id") Long id) {
        log.debug("REST request to get BodyVitalsLog : {}", id);
        Optional<BodyVitalsLog> bodyVitalsLog = bodyVitalsLogRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(bodyVitalsLog);
    }

    /**
     * {@code DELETE  /body-vitals-logs/:id} : delete the "id" bodyVitalsLog.
     *
     * @param id the id of the bodyVitalsLog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBodyVitalsLog(@PathVariable("id") Long id) {
        log.debug("REST request to delete BodyVitalsLog : {}", id);
        bodyVitalsLogRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
