package rocks.zipcode.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rocks.zipcode.domain.BodyWeightAsserts.*;
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
import rocks.zipcode.domain.BodyWeight;
import rocks.zipcode.repository.BodyWeightRepository;

/**
 * Integration tests for the {@link BodyWeightResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BodyWeightResourceIT {

    private static final Float DEFAULT_WEIGHT = 1F;
    private static final Float UPDATED_WEIGHT = 2F;

    private static final String ENTITY_API_URL = "/api/body-weights";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BodyWeightRepository bodyWeightRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBodyWeightMockMvc;

    private BodyWeight bodyWeight;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BodyWeight createEntity(EntityManager em) {
        BodyWeight bodyWeight = new BodyWeight().weight(DEFAULT_WEIGHT);
        return bodyWeight;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BodyWeight createUpdatedEntity(EntityManager em) {
        BodyWeight bodyWeight = new BodyWeight().weight(UPDATED_WEIGHT);
        return bodyWeight;
    }

    @BeforeEach
    public void initTest() {
        bodyWeight = createEntity(em);
    }

    @Test
    @Transactional
    void createBodyWeight() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BodyWeight
        var returnedBodyWeight = om.readValue(
            restBodyWeightMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bodyWeight)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BodyWeight.class
        );

        // Validate the BodyWeight in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertBodyWeightUpdatableFieldsEquals(returnedBodyWeight, getPersistedBodyWeight(returnedBodyWeight));
    }

    @Test
    @Transactional
    void createBodyWeightWithExistingId() throws Exception {
        // Create the BodyWeight with an existing ID
        bodyWeight.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBodyWeightMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bodyWeight)))
            .andExpect(status().isBadRequest());

        // Validate the BodyWeight in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBodyWeights() throws Exception {
        // Initialize the database
        bodyWeightRepository.saveAndFlush(bodyWeight);

        // Get all the bodyWeightList
        restBodyWeightMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bodyWeight.getId().intValue())))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.doubleValue())));
    }

    @Test
    @Transactional
    void getBodyWeight() throws Exception {
        // Initialize the database
        bodyWeightRepository.saveAndFlush(bodyWeight);

        // Get the bodyWeight
        restBodyWeightMockMvc
            .perform(get(ENTITY_API_URL_ID, bodyWeight.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bodyWeight.getId().intValue()))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingBodyWeight() throws Exception {
        // Get the bodyWeight
        restBodyWeightMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBodyWeight() throws Exception {
        // Initialize the database
        bodyWeightRepository.saveAndFlush(bodyWeight);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bodyWeight
        BodyWeight updatedBodyWeight = bodyWeightRepository.findById(bodyWeight.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBodyWeight are not directly saved in db
        em.detach(updatedBodyWeight);
        updatedBodyWeight.weight(UPDATED_WEIGHT);

        restBodyWeightMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBodyWeight.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedBodyWeight))
            )
            .andExpect(status().isOk());

        // Validate the BodyWeight in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBodyWeightToMatchAllProperties(updatedBodyWeight);
    }

    @Test
    @Transactional
    void putNonExistingBodyWeight() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bodyWeight.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBodyWeightMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bodyWeight.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bodyWeight))
            )
            .andExpect(status().isBadRequest());

        // Validate the BodyWeight in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBodyWeight() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bodyWeight.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodyWeightMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bodyWeight))
            )
            .andExpect(status().isBadRequest());

        // Validate the BodyWeight in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBodyWeight() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bodyWeight.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodyWeightMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bodyWeight)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BodyWeight in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBodyWeightWithPatch() throws Exception {
        // Initialize the database
        bodyWeightRepository.saveAndFlush(bodyWeight);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bodyWeight using partial update
        BodyWeight partialUpdatedBodyWeight = new BodyWeight();
        partialUpdatedBodyWeight.setId(bodyWeight.getId());

        restBodyWeightMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBodyWeight.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBodyWeight))
            )
            .andExpect(status().isOk());

        // Validate the BodyWeight in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBodyWeightUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBodyWeight, bodyWeight),
            getPersistedBodyWeight(bodyWeight)
        );
    }

    @Test
    @Transactional
    void fullUpdateBodyWeightWithPatch() throws Exception {
        // Initialize the database
        bodyWeightRepository.saveAndFlush(bodyWeight);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bodyWeight using partial update
        BodyWeight partialUpdatedBodyWeight = new BodyWeight();
        partialUpdatedBodyWeight.setId(bodyWeight.getId());

        partialUpdatedBodyWeight.weight(UPDATED_WEIGHT);

        restBodyWeightMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBodyWeight.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBodyWeight))
            )
            .andExpect(status().isOk());

        // Validate the BodyWeight in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBodyWeightUpdatableFieldsEquals(partialUpdatedBodyWeight, getPersistedBodyWeight(partialUpdatedBodyWeight));
    }

    @Test
    @Transactional
    void patchNonExistingBodyWeight() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bodyWeight.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBodyWeightMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bodyWeight.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bodyWeight))
            )
            .andExpect(status().isBadRequest());

        // Validate the BodyWeight in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBodyWeight() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bodyWeight.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodyWeightMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bodyWeight))
            )
            .andExpect(status().isBadRequest());

        // Validate the BodyWeight in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBodyWeight() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bodyWeight.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodyWeightMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bodyWeight)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BodyWeight in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBodyWeight() throws Exception {
        // Initialize the database
        bodyWeightRepository.saveAndFlush(bodyWeight);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bodyWeight
        restBodyWeightMockMvc
            .perform(delete(ENTITY_API_URL_ID, bodyWeight.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bodyWeightRepository.count();
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

    protected BodyWeight getPersistedBodyWeight(BodyWeight bodyWeight) {
        return bodyWeightRepository.findById(bodyWeight.getId()).orElseThrow();
    }

    protected void assertPersistedBodyWeightToMatchAllProperties(BodyWeight expectedBodyWeight) {
        assertBodyWeightAllPropertiesEquals(expectedBodyWeight, getPersistedBodyWeight(expectedBodyWeight));
    }

    protected void assertPersistedBodyWeightToMatchUpdatableProperties(BodyWeight expectedBodyWeight) {
        assertBodyWeightAllUpdatablePropertiesEquals(expectedBodyWeight, getPersistedBodyWeight(expectedBodyWeight));
    }
}
