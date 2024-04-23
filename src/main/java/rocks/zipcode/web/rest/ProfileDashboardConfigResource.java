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
import rocks.zipcode.domain.ProfileDashboardConfig;
import rocks.zipcode.repository.ProfileDashboardConfigRepository;
import rocks.zipcode.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link rocks.zipcode.domain.ProfileDashboardConfig}.
 */
@RestController
@RequestMapping("/api/profile-dashboard-configs")
@Transactional
public class ProfileDashboardConfigResource {

    private final Logger log = LoggerFactory.getLogger(ProfileDashboardConfigResource.class);

    private static final String ENTITY_NAME = "profileDashboardConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfileDashboardConfigRepository profileDashboardConfigRepository;

    public ProfileDashboardConfigResource(ProfileDashboardConfigRepository profileDashboardConfigRepository) {
        this.profileDashboardConfigRepository = profileDashboardConfigRepository;
    }

    /**
     * {@code POST  /profile-dashboard-configs} : Create a new profileDashboardConfig.
     *
     * @param profileDashboardConfig the profileDashboardConfig to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new profileDashboardConfig, or with status {@code 400 (Bad Request)} if the profileDashboardConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProfileDashboardConfig> createProfileDashboardConfig(@RequestBody ProfileDashboardConfig profileDashboardConfig)
        throws URISyntaxException {
        log.debug("REST request to save ProfileDashboardConfig : {}", profileDashboardConfig);
        if (profileDashboardConfig.getId() != null) {
            throw new BadRequestAlertException("A new profileDashboardConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        profileDashboardConfig = profileDashboardConfigRepository.save(profileDashboardConfig);
        return ResponseEntity.created(new URI("/api/profile-dashboard-configs/" + profileDashboardConfig.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, profileDashboardConfig.getId().toString()))
            .body(profileDashboardConfig);
    }

    /**
     * {@code PUT  /profile-dashboard-configs/:id} : Updates an existing profileDashboardConfig.
     *
     * @param id the id of the profileDashboardConfig to save.
     * @param profileDashboardConfig the profileDashboardConfig to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated profileDashboardConfig,
     * or with status {@code 400 (Bad Request)} if the profileDashboardConfig is not valid,
     * or with status {@code 500 (Internal Server Error)} if the profileDashboardConfig couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProfileDashboardConfig> updateProfileDashboardConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProfileDashboardConfig profileDashboardConfig
    ) throws URISyntaxException {
        log.debug("REST request to update ProfileDashboardConfig : {}, {}", id, profileDashboardConfig);
        if (profileDashboardConfig.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profileDashboardConfig.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!profileDashboardConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        profileDashboardConfig = profileDashboardConfigRepository.save(profileDashboardConfig);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, profileDashboardConfig.getId().toString()))
            .body(profileDashboardConfig);
    }

    /**
     * {@code PATCH  /profile-dashboard-configs/:id} : Partial updates given fields of an existing profileDashboardConfig, field will ignore if it is null
     *
     * @param id the id of the profileDashboardConfig to save.
     * @param profileDashboardConfig the profileDashboardConfig to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated profileDashboardConfig,
     * or with status {@code 400 (Bad Request)} if the profileDashboardConfig is not valid,
     * or with status {@code 404 (Not Found)} if the profileDashboardConfig is not found,
     * or with status {@code 500 (Internal Server Error)} if the profileDashboardConfig couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfileDashboardConfig> partialUpdateProfileDashboardConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProfileDashboardConfig profileDashboardConfig
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProfileDashboardConfig partially : {}, {}", id, profileDashboardConfig);
        if (profileDashboardConfig.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profileDashboardConfig.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!profileDashboardConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProfileDashboardConfig> result = profileDashboardConfigRepository
            .findById(profileDashboardConfig.getId())
            .map(existingProfileDashboardConfig -> {
                if (profileDashboardConfig.getIsBloodGlucoseShown() != null) {
                    existingProfileDashboardConfig.setIsBloodGlucoseShown(profileDashboardConfig.getIsBloodGlucoseShown());
                }
                if (profileDashboardConfig.getIsBloodPressureShown() != null) {
                    existingProfileDashboardConfig.setIsBloodPressureShown(profileDashboardConfig.getIsBloodPressureShown());
                }
                if (profileDashboardConfig.getIsBodyCompositionShown() != null) {
                    existingProfileDashboardConfig.setIsBodyCompositionShown(profileDashboardConfig.getIsBodyCompositionShown());
                }
                if (profileDashboardConfig.getIsBloodCholesterolShown() != null) {
                    existingProfileDashboardConfig.setIsBloodCholesterolShown(profileDashboardConfig.getIsBloodCholesterolShown());
                }
                if (profileDashboardConfig.getIsBodyHeightShown() != null) {
                    existingProfileDashboardConfig.setIsBodyHeightShown(profileDashboardConfig.getIsBodyHeightShown());
                }
                if (profileDashboardConfig.getIsBodyWeightShown() != null) {
                    existingProfileDashboardConfig.setIsBodyWeightShown(profileDashboardConfig.getIsBodyWeightShown());
                }
                if (profileDashboardConfig.getIsCaloriesBurntShown() != null) {
                    existingProfileDashboardConfig.setIsCaloriesBurntShown(profileDashboardConfig.getIsCaloriesBurntShown());
                }

                return existingProfileDashboardConfig;
            })
            .map(profileDashboardConfigRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, profileDashboardConfig.getId().toString())
        );
    }

    /**
     * {@code GET  /profile-dashboard-configs} : get all the profileDashboardConfigs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of profileDashboardConfigs in body.
     */
    @GetMapping("")
    public List<ProfileDashboardConfig> getAllProfileDashboardConfigs() {
        log.debug("REST request to get all ProfileDashboardConfigs");
        return profileDashboardConfigRepository.findAll();
    }

    /**
     * {@code GET  /profile-dashboard-configs/:id} : get the "id" profileDashboardConfig.
     *
     * @param id the id of the profileDashboardConfig to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the profileDashboardConfig, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProfileDashboardConfig> getProfileDashboardConfig(@PathVariable("id") Long id) {
        log.debug("REST request to get ProfileDashboardConfig : {}", id);
        Optional<ProfileDashboardConfig> profileDashboardConfig = profileDashboardConfigRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(profileDashboardConfig);
    }

    /**
     * {@code DELETE  /profile-dashboard-configs/:id} : delete the "id" profileDashboardConfig.
     *
     * @param id the id of the profileDashboardConfig to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfileDashboardConfig(@PathVariable("id") Long id) {
        log.debug("REST request to delete ProfileDashboardConfig : {}", id);
        profileDashboardConfigRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
