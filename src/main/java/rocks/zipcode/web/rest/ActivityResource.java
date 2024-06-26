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
import rocks.zipcode.domain.Activity;
import rocks.zipcode.repository.ActivityRepository;
import rocks.zipcode.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link rocks.zipcode.domain.Activity}.
 */
@RestController
@RequestMapping("/api/activities")
@Transactional
public class ActivityResource {

    private final Logger log = LoggerFactory.getLogger(ActivityResource.class);

    private static final String ENTITY_NAME = "activity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActivityRepository activityRepository;

    public ActivityResource(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    /**
     * {@code POST  /activities} : Create a new activity.
     *
     * @param activity the activity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new activity, or with status {@code 400 (Bad Request)} if the activity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Activity> createActivity(@RequestBody Activity activity) throws URISyntaxException {
        log.debug("REST request to save Activity : {}", activity);
        if (activity.getId() != null) {
            throw new BadRequestAlertException("A new activity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        activity = activityRepository.save(activity);
        return ResponseEntity.created(new URI("/api/activities/" + activity.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, activity.getId().toString()))
            .body(activity);
    }

    /**
     * {@code PUT  /activities/:id} : Updates an existing activity.
     *
     * @param id the id of the activity to save.
     * @param activity the activity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activity,
     * or with status {@code 400 (Bad Request)} if the activity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the activity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Activity> updateActivity(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Activity activity
    ) throws URISyntaxException {
        log.debug("REST request to update Activity : {}, {}", id, activity);
        if (activity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, activity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!activityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        activity = activityRepository.save(activity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, activity.getId().toString()))
            .body(activity);
    }

    /**
     * {@code PATCH  /activities/:id} : Partial updates given fields of an existing activity, field will ignore if it is null
     *
     * @param id the id of the activity to save.
     * @param activity the activity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activity,
     * or with status {@code 400 (Bad Request)} if the activity is not valid,
     * or with status {@code 404 (Not Found)} if the activity is not found,
     * or with status {@code 500 (Internal Server Error)} if the activity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Activity> partialUpdateActivity(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Activity activity
    ) throws URISyntaxException {
        log.debug("REST request to partial update Activity partially : {}, {}", id, activity);
        if (activity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, activity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!activityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Activity> result = activityRepository
            .findById(activity.getId())
            .map(existingActivity -> {
                if (activity.getActivityName() != null) {
                    existingActivity.setActivityName(activity.getActivityName());
                }
                if (activity.getActivityMultiplier() != null) {
                    existingActivity.setActivityMultiplier(activity.getActivityMultiplier());
                }

                return existingActivity;
            })
            .map(activityRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, activity.getId().toString())
        );
    }

    /**
     * {@code GET  /activities} : get all the activities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of activities in body.
     */
    @GetMapping("")
    public List<Activity> getAllActivities() {
        log.debug("REST request to get all Activities");
        return activityRepository.findAll();
    }

    /**
     * {@code GET  /activities/:id} : get the "id" activity.
     *
     * @param id the id of the activity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the activity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivity(@PathVariable("id") Long id) {
        log.debug("REST request to get Activity : {}", id);
        Optional<Activity> activity = activityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(activity);
    }

    /**
     * {@code DELETE  /activities/:id} : delete the "id" activity.
     *
     * @param id the id of the activity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable("id") Long id) {
        log.debug("REST request to delete Activity : {}", id);
        activityRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
