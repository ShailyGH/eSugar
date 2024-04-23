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
import rocks.zipcode.domain.UserBMR;
import rocks.zipcode.repository.UserBMRRepository;
import rocks.zipcode.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link rocks.zipcode.domain.UserBMR}.
 */
@RestController
@RequestMapping("/api/user-bmrs")
@Transactional
public class UserBMRResource {

    private final Logger log = LoggerFactory.getLogger(UserBMRResource.class);

    private static final String ENTITY_NAME = "userBMR";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserBMRRepository userBMRRepository;

    public UserBMRResource(UserBMRRepository userBMRRepository) {
        this.userBMRRepository = userBMRRepository;
    }

    /**
     * {@code POST  /user-bmrs} : Create a new userBMR.
     *
     * @param userBMR the userBMR to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userBMR, or with status {@code 400 (Bad Request)} if the userBMR has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserBMR> createUserBMR(@RequestBody UserBMR userBMR) throws URISyntaxException {
        log.debug("REST request to save UserBMR : {}", userBMR);
        if (userBMR.getId() != null) {
            throw new BadRequestAlertException("A new userBMR cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userBMR = userBMRRepository.save(userBMR);
        return ResponseEntity.created(new URI("/api/user-bmrs/" + userBMR.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, userBMR.getId().toString()))
            .body(userBMR);
    }

    /**
     * {@code PUT  /user-bmrs/:id} : Updates an existing userBMR.
     *
     * @param id the id of the userBMR to save.
     * @param userBMR the userBMR to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userBMR,
     * or with status {@code 400 (Bad Request)} if the userBMR is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userBMR couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserBMR> updateUserBMR(@PathVariable(value = "id", required = false) final Long id, @RequestBody UserBMR userBMR)
        throws URISyntaxException {
        log.debug("REST request to update UserBMR : {}, {}", id, userBMR);
        if (userBMR.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userBMR.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userBMRRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userBMR = userBMRRepository.save(userBMR);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userBMR.getId().toString()))
            .body(userBMR);
    }

    /**
     * {@code PATCH  /user-bmrs/:id} : Partial updates given fields of an existing userBMR, field will ignore if it is null
     *
     * @param id the id of the userBMR to save.
     * @param userBMR the userBMR to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userBMR,
     * or with status {@code 400 (Bad Request)} if the userBMR is not valid,
     * or with status {@code 404 (Not Found)} if the userBMR is not found,
     * or with status {@code 500 (Internal Server Error)} if the userBMR couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserBMR> partialUpdateUserBMR(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserBMR userBMR
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserBMR partially : {}, {}", id, userBMR);
        if (userBMR.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userBMR.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userBMRRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserBMR> result = userBMRRepository
            .findById(userBMR.getId())
            .map(existingUserBMR -> {
                if (userBMR.getIdVersion() != null) {
                    existingUserBMR.setIdVersion(userBMR.getIdVersion());
                }
                if (userBMR.getBmr() != null) {
                    existingUserBMR.setBmr(userBMR.getBmr());
                }
                if (userBMR.getDtCreated() != null) {
                    existingUserBMR.setDtCreated(userBMR.getDtCreated());
                }
                if (userBMR.getDtModified() != null) {
                    existingUserBMR.setDtModified(userBMR.getDtModified());
                }

                return existingUserBMR;
            })
            .map(userBMRRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userBMR.getId().toString())
        );
    }

    /**
     * {@code GET  /user-bmrs} : get all the userBMRS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userBMRS in body.
     */
    @GetMapping("")
    public List<UserBMR> getAllUserBMRS() {
        log.debug("REST request to get all UserBMRS");
        return userBMRRepository.findAll();
    }

    /**
     * {@code GET  /user-bmrs/:id} : get the "id" userBMR.
     *
     * @param id the id of the userBMR to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userBMR, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserBMR> getUserBMR(@PathVariable("id") Long id) {
        log.debug("REST request to get UserBMR : {}", id);
        Optional<UserBMR> userBMR = userBMRRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userBMR);
    }

    /**
     * {@code DELETE  /user-bmrs/:id} : delete the "id" userBMR.
     *
     * @param id the id of the userBMR to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserBMR(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserBMR : {}", id);
        userBMRRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
