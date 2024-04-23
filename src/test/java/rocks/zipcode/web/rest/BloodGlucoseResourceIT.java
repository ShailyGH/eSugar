package rocks.zipcode.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rocks.zipcode.domain.BloodGlucoseAsserts.*;
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
import rocks.zipcode.domain.BloodGlucose;
import rocks.zipcode.repository.BloodGlucoseRepository;

/**
 * Integration tests for the {@link BloodGlucoseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BloodGlucoseResourceIT {

    private static final Float DEFAULT_MEASUREMENT = 1F;
    private static final Float UPDATED_MEASUREMENT = 2F;

    private static final String DEFAULT_MEASUREMENT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_MEASUREMENT_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_MEASUREMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MEASUREMENT_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/blood-glucoses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BloodGlucoseRepository bloodGlucoseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBloodGlucoseMockMvc;

    private BloodGlucose bloodGlucose;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BloodGlucose createEntity(EntityManager em) {
        BloodGlucose bloodGlucose = new BloodGlucose()
            .measurement(DEFAULT_MEASUREMENT)
            .measurementContent(DEFAULT_MEASUREMENT_CONTENT)
            .measurementType(DEFAULT_MEASUREMENT_TYPE);
        return bloodGlucose;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BloodGlucose createUpdatedEntity(EntityManager em) {
        BloodGlucose bloodGlucose = new BloodGlucose()
            .measurement(UPDATED_MEASUREMENT)
            .measurementContent(UPDATED_MEASUREMENT_CONTENT)
            .measurementType(UPDATED_MEASUREMENT_TYPE);
        return bloodGlucose;
    }

    @BeforeEach
    public void initTest() {
        bloodGlucose = createEntity(em);
    }

    @Test
    @Transactional
    void createBloodGlucose() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BloodGlucose
        var returnedBloodGlucose = om.readValue(
            restBloodGlucoseMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bloodGlucose)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BloodGlucose.class
        );

        // Validate the BloodGlucose in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertBloodGlucoseUpdatableFieldsEquals(returnedBloodGlucose, getPersistedBloodGlucose(returnedBloodGlucose));
    }

    @Test
    @Transactional
    void createBloodGlucoseWithExistingId() throws Exception {
        // Create the BloodGlucose with an existing ID
        bloodGlucose.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBloodGlucoseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bloodGlucose)))
            .andExpect(status().isBadRequest());

        // Validate the BloodGlucose in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBloodGlucoses() throws Exception {
        // Initialize the database
        bloodGlucoseRepository.saveAndFlush(bloodGlucose);

        // Get all the bloodGlucoseList
        restBloodGlucoseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bloodGlucose.getId().intValue())))
            .andExpect(jsonPath("$.[*].measurement").value(hasItem(DEFAULT_MEASUREMENT.doubleValue())))
            .andExpect(jsonPath("$.[*].measurementContent").value(hasItem(DEFAULT_MEASUREMENT_CONTENT)))
            .andExpect(jsonPath("$.[*].measurementType").value(hasItem(DEFAULT_MEASUREMENT_TYPE)));
    }

    @Test
    @Transactional
    void getBloodGlucose() throws Exception {
        // Initialize the database
        bloodGlucoseRepository.saveAndFlush(bloodGlucose);

        // Get the bloodGlucose
        restBloodGlucoseMockMvc
            .perform(get(ENTITY_API_URL_ID, bloodGlucose.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bloodGlucose.getId().intValue()))
            .andExpect(jsonPath("$.measurement").value(DEFAULT_MEASUREMENT.doubleValue()))
            .andExpect(jsonPath("$.measurementContent").value(DEFAULT_MEASUREMENT_CONTENT))
            .andExpect(jsonPath("$.measurementType").value(DEFAULT_MEASUREMENT_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingBloodGlucose() throws Exception {
        // Get the bloodGlucose
        restBloodGlucoseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBloodGlucose() throws Exception {
        // Initialize the database
        bloodGlucoseRepository.saveAndFlush(bloodGlucose);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bloodGlucose
        BloodGlucose updatedBloodGlucose = bloodGlucoseRepository.findById(bloodGlucose.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBloodGlucose are not directly saved in db
        em.detach(updatedBloodGlucose);
        updatedBloodGlucose
            .measurement(UPDATED_MEASUREMENT)
            .measurementContent(UPDATED_MEASUREMENT_CONTENT)
            .measurementType(UPDATED_MEASUREMENT_TYPE);

        restBloodGlucoseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBloodGlucose.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedBloodGlucose))
            )
            .andExpect(status().isOk());

        // Validate the BloodGlucose in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBloodGlucoseToMatchAllProperties(updatedBloodGlucose);
    }

    @Test
    @Transactional
    void putNonExistingBloodGlucose() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bloodGlucose.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBloodGlucoseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bloodGlucose.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bloodGlucose))
            )
            .andExpect(status().isBadRequest());

        // Validate the BloodGlucose in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBloodGlucose() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bloodGlucose.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBloodGlucoseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bloodGlucose))
            )
            .andExpect(status().isBadRequest());

        // Validate the BloodGlucose in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBloodGlucose() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bloodGlucose.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBloodGlucoseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bloodGlucose)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BloodGlucose in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBloodGlucoseWithPatch() throws Exception {
        // Initialize the database
        bloodGlucoseRepository.saveAndFlush(bloodGlucose);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bloodGlucose using partial update
        BloodGlucose partialUpdatedBloodGlucose = new BloodGlucose();
        partialUpdatedBloodGlucose.setId(bloodGlucose.getId());

        partialUpdatedBloodGlucose.measurementContent(UPDATED_MEASUREMENT_CONTENT).measurementType(UPDATED_MEASUREMENT_TYPE);

        restBloodGlucoseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBloodGlucose.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBloodGlucose))
            )
            .andExpect(status().isOk());

        // Validate the BloodGlucose in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBloodGlucoseUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBloodGlucose, bloodGlucose),
            getPersistedBloodGlucose(bloodGlucose)
        );
    }

    @Test
    @Transactional
    void fullUpdateBloodGlucoseWithPatch() throws Exception {
        // Initialize the database
        bloodGlucoseRepository.saveAndFlush(bloodGlucose);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bloodGlucose using partial update
        BloodGlucose partialUpdatedBloodGlucose = new BloodGlucose();
        partialUpdatedBloodGlucose.setId(bloodGlucose.getId());

        partialUpdatedBloodGlucose
            .measurement(UPDATED_MEASUREMENT)
            .measurementContent(UPDATED_MEASUREMENT_CONTENT)
            .measurementType(UPDATED_MEASUREMENT_TYPE);

        restBloodGlucoseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBloodGlucose.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBloodGlucose))
            )
            .andExpect(status().isOk());

        // Validate the BloodGlucose in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBloodGlucoseUpdatableFieldsEquals(partialUpdatedBloodGlucose, getPersistedBloodGlucose(partialUpdatedBloodGlucose));
    }

    @Test
    @Transactional
    void patchNonExistingBloodGlucose() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bloodGlucose.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBloodGlucoseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bloodGlucose.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bloodGlucose))
            )
            .andExpect(status().isBadRequest());

        // Validate the BloodGlucose in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBloodGlucose() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bloodGlucose.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBloodGlucoseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bloodGlucose))
            )
            .andExpect(status().isBadRequest());

        // Validate the BloodGlucose in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBloodGlucose() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bloodGlucose.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBloodGlucoseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bloodGlucose)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BloodGlucose in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBloodGlucose() throws Exception {
        // Initialize the database
        bloodGlucoseRepository.saveAndFlush(bloodGlucose);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bloodGlucose
        restBloodGlucoseMockMvc
            .perform(delete(ENTITY_API_URL_ID, bloodGlucose.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bloodGlucoseRepository.count();
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

    protected BloodGlucose getPersistedBloodGlucose(BloodGlucose bloodGlucose) {
        return bloodGlucoseRepository.findById(bloodGlucose.getId()).orElseThrow();
    }

    protected void assertPersistedBloodGlucoseToMatchAllProperties(BloodGlucose expectedBloodGlucose) {
        assertBloodGlucoseAllPropertiesEquals(expectedBloodGlucose, getPersistedBloodGlucose(expectedBloodGlucose));
    }

    protected void assertPersistedBloodGlucoseToMatchUpdatableProperties(BloodGlucose expectedBloodGlucose) {
        assertBloodGlucoseAllUpdatablePropertiesEquals(expectedBloodGlucose, getPersistedBloodGlucose(expectedBloodGlucose));
    }
}
