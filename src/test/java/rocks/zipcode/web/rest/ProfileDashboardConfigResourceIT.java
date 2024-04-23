package rocks.zipcode.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rocks.zipcode.domain.ProfileDashboardConfigAsserts.*;
import static rocks.zipcode.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import rocks.zipcode.IntegrationTest;
import rocks.zipcode.domain.ProfileDashboardConfig;
import rocks.zipcode.repository.ProfileDashboardConfigRepository;

/**
 * Integration tests for the {@link ProfileDashboardConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProfileDashboardConfigResourceIT {

    private static final String DEFAULT_IS_BLOOD_GLUCOSE_SHOWN = "AAAAAAAAAA";
    private static final String UPDATED_IS_BLOOD_GLUCOSE_SHOWN = "BBBBBBBBBB";

    private static final String DEFAULT_IS_BLOOD_PRESSURE_SHOWN = "AAAAAAAAAA";
    private static final String UPDATED_IS_BLOOD_PRESSURE_SHOWN = "BBBBBBBBBB";

    private static final String DEFAULT_IS_BODY_COMPOSITION_SHOWN = "AAAAAAAAAA";
    private static final String UPDATED_IS_BODY_COMPOSITION_SHOWN = "BBBBBBBBBB";

    private static final String DEFAULT_IS_BLOOD_CHOLESTEROL_SHOWN = "AAAAAAAAAA";
    private static final String UPDATED_IS_BLOOD_CHOLESTEROL_SHOWN = "BBBBBBBBBB";

    private static final String DEFAULT_IS_BODY_HEIGHT_SHOWN = "AAAAAAAAAA";
    private static final String UPDATED_IS_BODY_HEIGHT_SHOWN = "BBBBBBBBBB";

    private static final String DEFAULT_IS_BODY_WEIGHT_SHOWN = "AAAAAAAAAA";
    private static final String UPDATED_IS_BODY_WEIGHT_SHOWN = "BBBBBBBBBB";

    private static final String DEFAULT_IS_CALORIES_BURNT_SHOWN = "AAAAAAAAAA";
    private static final String UPDATED_IS_CALORIES_BURNT_SHOWN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/profile-dashboard-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProfileDashboardConfigRepository profileDashboardConfigRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfileDashboardConfigMockMvc;

    private ProfileDashboardConfig profileDashboardConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfileDashboardConfig createEntity(EntityManager em) {
        ProfileDashboardConfig profileDashboardConfig = new ProfileDashboardConfig()
            .isBloodGlucoseShown(DEFAULT_IS_BLOOD_GLUCOSE_SHOWN)
            .isBloodPressureShown(DEFAULT_IS_BLOOD_PRESSURE_SHOWN)
            .isBodyCompositionShown(DEFAULT_IS_BODY_COMPOSITION_SHOWN)
            .isBloodCholesterolShown(DEFAULT_IS_BLOOD_CHOLESTEROL_SHOWN)
            .isBodyHeightShown(DEFAULT_IS_BODY_HEIGHT_SHOWN)
            .isBodyWeightShown(DEFAULT_IS_BODY_WEIGHT_SHOWN)
            .isCaloriesBurntShown(DEFAULT_IS_CALORIES_BURNT_SHOWN);
        return profileDashboardConfig;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfileDashboardConfig createUpdatedEntity(EntityManager em) {
        ProfileDashboardConfig profileDashboardConfig = new ProfileDashboardConfig()
            .isBloodGlucoseShown(UPDATED_IS_BLOOD_GLUCOSE_SHOWN)
            .isBloodPressureShown(UPDATED_IS_BLOOD_PRESSURE_SHOWN)
            .isBodyCompositionShown(UPDATED_IS_BODY_COMPOSITION_SHOWN)
            .isBloodCholesterolShown(UPDATED_IS_BLOOD_CHOLESTEROL_SHOWN)
            .isBodyHeightShown(UPDATED_IS_BODY_HEIGHT_SHOWN)
            .isBodyWeightShown(UPDATED_IS_BODY_WEIGHT_SHOWN)
            .isCaloriesBurntShown(UPDATED_IS_CALORIES_BURNT_SHOWN);
        return profileDashboardConfig;
    }

    @BeforeEach
    public void initTest() {
        profileDashboardConfig = createEntity(em);
    }

    @Test
    @Transactional
    void createProfileDashboardConfig() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProfileDashboardConfig
        var returnedProfileDashboardConfig = om.readValue(
            restProfileDashboardConfigMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profileDashboardConfig)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProfileDashboardConfig.class
        );

        // Validate the ProfileDashboardConfig in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProfileDashboardConfigUpdatableFieldsEquals(
            returnedProfileDashboardConfig,
            getPersistedProfileDashboardConfig(returnedProfileDashboardConfig)
        );
    }

    @Test
    @Transactional
    void createProfileDashboardConfigWithExistingId() throws Exception {
        // Create the ProfileDashboardConfig with an existing ID
        profileDashboardConfig.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfileDashboardConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profileDashboardConfig)))
            .andExpect(status().isBadRequest());

        // Validate the ProfileDashboardConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProfileDashboardConfigs() throws Exception {
        // Initialize the database
        profileDashboardConfigRepository.saveAndFlush(profileDashboardConfig);

        // Get all the profileDashboardConfigList
        restProfileDashboardConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profileDashboardConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].isBloodGlucoseShown").value(hasItem(DEFAULT_IS_BLOOD_GLUCOSE_SHOWN)))
            .andExpect(jsonPath("$.[*].isBloodPressureShown").value(hasItem(DEFAULT_IS_BLOOD_PRESSURE_SHOWN)))
            .andExpect(jsonPath("$.[*].isBodyCompositionShown").value(hasItem(DEFAULT_IS_BODY_COMPOSITION_SHOWN)))
            .andExpect(jsonPath("$.[*].isBloodCholesterolShown").value(hasItem(DEFAULT_IS_BLOOD_CHOLESTEROL_SHOWN)))
            .andExpect(jsonPath("$.[*].isBodyHeightShown").value(hasItem(DEFAULT_IS_BODY_HEIGHT_SHOWN)))
            .andExpect(jsonPath("$.[*].isBodyWeightShown").value(hasItem(DEFAULT_IS_BODY_WEIGHT_SHOWN)))
            .andExpect(jsonPath("$.[*].isCaloriesBurntShown").value(hasItem(DEFAULT_IS_CALORIES_BURNT_SHOWN)));
    }

    @Test
    @Transactional
    void getProfileDashboardConfig() throws Exception {
        // Initialize the database
        profileDashboardConfigRepository.saveAndFlush(profileDashboardConfig);

        // Get the profileDashboardConfig
        restProfileDashboardConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, profileDashboardConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(profileDashboardConfig.getId().intValue()))
            .andExpect(jsonPath("$.isBloodGlucoseShown").value(DEFAULT_IS_BLOOD_GLUCOSE_SHOWN))
            .andExpect(jsonPath("$.isBloodPressureShown").value(DEFAULT_IS_BLOOD_PRESSURE_SHOWN))
            .andExpect(jsonPath("$.isBodyCompositionShown").value(DEFAULT_IS_BODY_COMPOSITION_SHOWN))
            .andExpect(jsonPath("$.isBloodCholesterolShown").value(DEFAULT_IS_BLOOD_CHOLESTEROL_SHOWN))
            .andExpect(jsonPath("$.isBodyHeightShown").value(DEFAULT_IS_BODY_HEIGHT_SHOWN))
            .andExpect(jsonPath("$.isBodyWeightShown").value(DEFAULT_IS_BODY_WEIGHT_SHOWN))
            .andExpect(jsonPath("$.isCaloriesBurntShown").value(DEFAULT_IS_CALORIES_BURNT_SHOWN));
    }

    @Test
    @Transactional
    void getNonExistingProfileDashboardConfig() throws Exception {
        // Get the profileDashboardConfig
        restProfileDashboardConfigMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfileDashboardConfig() throws Exception {
        // Initialize the database
        profileDashboardConfigRepository.saveAndFlush(profileDashboardConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profileDashboardConfig
        ProfileDashboardConfig updatedProfileDashboardConfig = profileDashboardConfigRepository
            .findById(profileDashboardConfig.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedProfileDashboardConfig are not directly saved in db
        em.detach(updatedProfileDashboardConfig);
        updatedProfileDashboardConfig
            .isBloodGlucoseShown(UPDATED_IS_BLOOD_GLUCOSE_SHOWN)
            .isBloodPressureShown(UPDATED_IS_BLOOD_PRESSURE_SHOWN)
            .isBodyCompositionShown(UPDATED_IS_BODY_COMPOSITION_SHOWN)
            .isBloodCholesterolShown(UPDATED_IS_BLOOD_CHOLESTEROL_SHOWN)
            .isBodyHeightShown(UPDATED_IS_BODY_HEIGHT_SHOWN)
            .isBodyWeightShown(UPDATED_IS_BODY_WEIGHT_SHOWN)
            .isCaloriesBurntShown(UPDATED_IS_CALORIES_BURNT_SHOWN);

        restProfileDashboardConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProfileDashboardConfig.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProfileDashboardConfig))
            )
            .andExpect(status().isOk());

        // Validate the ProfileDashboardConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProfileDashboardConfigToMatchAllProperties(updatedProfileDashboardConfig);
    }

    @Test
    @Transactional
    void putNonExistingProfileDashboardConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profileDashboardConfig.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfileDashboardConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, profileDashboardConfig.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profileDashboardConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfileDashboardConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfileDashboardConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profileDashboardConfig.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfileDashboardConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profileDashboardConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfileDashboardConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfileDashboardConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profileDashboardConfig.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfileDashboardConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profileDashboardConfig)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfileDashboardConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfileDashboardConfigWithPatch() throws Exception {
        // Initialize the database
        profileDashboardConfigRepository.saveAndFlush(profileDashboardConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profileDashboardConfig using partial update
        ProfileDashboardConfig partialUpdatedProfileDashboardConfig = new ProfileDashboardConfig();
        partialUpdatedProfileDashboardConfig.setId(profileDashboardConfig.getId());

        partialUpdatedProfileDashboardConfig
            .isBloodGlucoseShown(UPDATED_IS_BLOOD_GLUCOSE_SHOWN)
            .isBloodPressureShown(UPDATED_IS_BLOOD_PRESSURE_SHOWN)
            .isBodyCompositionShown(UPDATED_IS_BODY_COMPOSITION_SHOWN)
            .isBodyWeightShown(UPDATED_IS_BODY_WEIGHT_SHOWN)
            .isCaloriesBurntShown(UPDATED_IS_CALORIES_BURNT_SHOWN);

        restProfileDashboardConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfileDashboardConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfileDashboardConfig))
            )
            .andExpect(status().isOk());

        // Validate the ProfileDashboardConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfileDashboardConfigUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProfileDashboardConfig, profileDashboardConfig),
            getPersistedProfileDashboardConfig(profileDashboardConfig)
        );
    }

    @Test
    @Transactional
    void fullUpdateProfileDashboardConfigWithPatch() throws Exception {
        // Initialize the database
        profileDashboardConfigRepository.saveAndFlush(profileDashboardConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profileDashboardConfig using partial update
        ProfileDashboardConfig partialUpdatedProfileDashboardConfig = new ProfileDashboardConfig();
        partialUpdatedProfileDashboardConfig.setId(profileDashboardConfig.getId());

        partialUpdatedProfileDashboardConfig
            .isBloodGlucoseShown(UPDATED_IS_BLOOD_GLUCOSE_SHOWN)
            .isBloodPressureShown(UPDATED_IS_BLOOD_PRESSURE_SHOWN)
            .isBodyCompositionShown(UPDATED_IS_BODY_COMPOSITION_SHOWN)
            .isBloodCholesterolShown(UPDATED_IS_BLOOD_CHOLESTEROL_SHOWN)
            .isBodyHeightShown(UPDATED_IS_BODY_HEIGHT_SHOWN)
            .isBodyWeightShown(UPDATED_IS_BODY_WEIGHT_SHOWN)
            .isCaloriesBurntShown(UPDATED_IS_CALORIES_BURNT_SHOWN);

        restProfileDashboardConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfileDashboardConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfileDashboardConfig))
            )
            .andExpect(status().isOk());

        // Validate the ProfileDashboardConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfileDashboardConfigUpdatableFieldsEquals(
            partialUpdatedProfileDashboardConfig,
            getPersistedProfileDashboardConfig(partialUpdatedProfileDashboardConfig)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProfileDashboardConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profileDashboardConfig.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfileDashboardConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, profileDashboardConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(profileDashboardConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfileDashboardConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfileDashboardConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profileDashboardConfig.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfileDashboardConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(profileDashboardConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfileDashboardConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfileDashboardConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profileDashboardConfig.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfileDashboardConfigMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(profileDashboardConfig))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfileDashboardConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfileDashboardConfig() throws Exception {
        // Initialize the database
        profileDashboardConfigRepository.saveAndFlush(profileDashboardConfig);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the profileDashboardConfig
        restProfileDashboardConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, profileDashboardConfig.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return profileDashboardConfigRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ProfileDashboardConfig getPersistedProfileDashboardConfig(ProfileDashboardConfig profileDashboardConfig) {
        return profileDashboardConfigRepository.findById(profileDashboardConfig.getId()).orElseThrow();
    }

    protected void assertPersistedProfileDashboardConfigToMatchAllProperties(ProfileDashboardConfig expectedProfileDashboardConfig) {
        assertProfileDashboardConfigAllPropertiesEquals(
            expectedProfileDashboardConfig,
            getPersistedProfileDashboardConfig(expectedProfileDashboardConfig)
        );
    }

    protected void assertPersistedProfileDashboardConfigToMatchUpdatableProperties(ProfileDashboardConfig expectedProfileDashboardConfig) {
        assertProfileDashboardConfigAllUpdatablePropertiesEquals(
            expectedProfileDashboardConfig,
            getPersistedProfileDashboardConfig(expectedProfileDashboardConfig)
        );
    }
}
