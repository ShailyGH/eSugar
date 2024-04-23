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
import rocks.zipcode.domain.ActivityLog;
import rocks.zipcode.repository.ActivityLogRepository;
import rocks.zipcode.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link rocks.zipcode.domain.ActivityLog}.
 */
@RestController
@RequestMapping("/api/activity-logs")
@Transactional
public class ActivityLogResource {

    private final Logger log = LoggerFactory.getLogger(ActivityLogResource.class);

    private static final String ENTITY_NAME = "activityLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActivityLogRepository activityLogRepository;

    public ActivityLogResource(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    /**
     * {@code POST  /activity-logs} : Create a new activityLog.
     *
     * @param activityLog the activityLog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new activityLog, or with status {@code 400 (Bad Request)} if the activityLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ActivityLog> createActivityLog(@RequestBody ActivityLog activityLog) throws URISyntaxException {
        log.debug("REST request to save ActivityLog : {}", activityLog);
        if (activityLog.getId() != null) {
            throw new BadRequestAlertException("A new activityLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        activityLog = activityLogRepository.save(activityLog);
        return ResponseEntity.created(new URI("/api/activity-logs/" + activityLog.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, activityLog.getId().toString()))
            .body(activityLog);
    }

    /**
     * {@code PUT  /activity-logs/:id} : Updates an existing activityLog.
     *
     * @param id the id of the activityLog to save.
     * @param activityLog the activityLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activityLog,
     * or with status {@code 400 (Bad Request)} if the activityLog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the activityLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ActivityLog> updateActivityLog(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ActivityLog activityLog
    ) throws URISyntaxException {
        log.debug("REST request to update ActivityLog : {}, {}", id, activityLog);
        if (activityLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, activityLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!activityLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        activityLog = activityLogRepository.save(activityLog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, activityLog.getId().toString()))
            .body(activityLog);
    }

    /**
     * {@code PATCH  /activity-logs/:id} : Partial updates given fields of an existing activityLog, field will ignore if it is null
     *
     * @param id the id of the activityLog to save.
     * @param activityLog the activityLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activityLog,
     * or with status {@code 400 (Bad Request)} if the activityLog is not valid,
     * or with status {@code 404 (Not Found)} if the activityLog is not found,
     * or with status {@code 500 (Internal Server Error)} if the activityLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ActivityLog> partialUpdateActivityLog(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ActivityLog activityLog
    ) throws URISyntaxException {
        log.debug("REST request to partial update ActivityLog partially : {}, {}", id, activityLog);
        if (activityLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, activityLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!activityLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ActivityLog> result = activityLogRepository
            .findById(activityLog.getId())
            .map(existingActivityLog -> {
                if (activityLog.getStartDateTime() != null) {
                    existingActivityLog.setStartDateTime(activityLog.getStartDateTime());
                }
                if (activityLog.getEndDateTime() != null) {
                    existingActivityLog.setEndDateTime(activityLog.getEndDateTime());
                }
                if (activityLog.getDistanceCovered() != null) {
                    existingActivityLog.setDistanceCovered(activityLog.getDistanceCovered());
                }
                if (activityLog.getStepsCount() != null) {
                    existingActivityLog.setStepsCount(activityLog.getStepsCount());
                }
                if (activityLog.getCaloriesBurnt() != null) {
                    existingActivityLog.setCaloriesBurnt(activityLog.getCaloriesBurnt());
                }

                return existingActivityLog;
            })
            .map(activityLogRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, activityLog.getId().toString())
        );
    }

    /**
     * {@code GET  /activity-logs} : get all the activityLogs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of activityLogs in body.
     */
    @GetMapping("")
    public List<ActivityLog> getAllActivityLogs() {
        log.debug("REST request to get all ActivityLogs");
        return activityLogRepository.findAll();
    }

    /**
     * {@code GET  /activity-logs/:id} : get the "id" activityLog.
     *
     * @param id the id of the activityLog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the activityLog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ActivityLog> getActivityLog(@PathVariable("id") Long id) {
        log.debug("REST request to get ActivityLog : {}", id);
        Optional<ActivityLog> activityLog = activityLogRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(activityLog);
    }

    /**
     * {@code DELETE  /activity-logs/:id} : delete the "id" activityLog.
     *
     * @param id the id of the activityLog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivityLog(@PathVariable("id") Long id) {
        log.debug("REST request to delete ActivityLog : {}", id);
        activityLogRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
