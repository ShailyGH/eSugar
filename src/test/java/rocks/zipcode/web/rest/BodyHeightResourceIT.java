package rocks.zipcode.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rocks.zipcode.domain.BodyHeightAsserts.*;
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
import rocks.zipcode.domain.BodyHeight;
import rocks.zipcode.repository.BodyHeightRepository;

/**
 * Integration tests for the {@link BodyHeightResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BodyHeightResourceIT {

    private static final Float DEFAULT_HEIGHT = 1F;
    private static final Float UPDATED_HEIGHT = 2F;

    private static final String ENTITY_API_URL = "/api/body-heights";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BodyHeightRepository bodyHeightRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBodyHeightMockMvc;

    private BodyHeight bodyHeight;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BodyHeight createEntity(EntityManager em) {
        BodyHeight bodyHeight = new BodyHeight().height(DEFAULT_HEIGHT);
        return bodyHeight;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BodyHeight createUpdatedEntity(EntityManager em) {
        BodyHeight bodyHeight = new BodyHeight().height(UPDATED_HEIGHT);
        return bodyHeight;
    }

    @BeforeEach
    public void initTest() {
        bodyHeight = createEntity(em);
    }

    @Test
    @Transactional
    void createBodyHeight() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BodyHeight
        var returnedBodyHeight = om.readValue(
            restBodyHeightMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bodyHeight)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BodyHeight.class
        );

        // Validate the BodyHeight in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertBodyHeightUpdatableFieldsEquals(returnedBodyHeight, getPersistedBodyHeight(returnedBodyHeight));
    }

    @Test
    @Transactional
    void createBodyHeightWithExistingId() throws Exception {
        // Create the BodyHeight with an existing ID
        bodyHeight.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBodyHeightMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bodyHeight)))
            .andExpect(status().isBadRequest());

        // Validate the BodyHeight in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBodyHeights() throws Exception {
        // Initialize the database
        bodyHeightRepository.saveAndFlush(bodyHeight);

        // Get all the bodyHeightList
        restBodyHeightMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bodyHeight.getId().intValue())))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT.doubleValue())));
    }

    @Test
    @Transactional
    void getBodyHeight() throws Exception {
        // Initialize the database
        bodyHeightRepository.saveAndFlush(bodyHeight);

        // Get the bodyHeight
        restBodyHeightMockMvc
            .perform(get(ENTITY_API_URL_ID, bodyHeight.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bodyHeight.getId().intValue()))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingBodyHeight() throws Exception {
        // Get the bodyHeight
        restBodyHeightMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBodyHeight() throws Exception {
        // Initialize the database
        bodyHeightRepository.saveAndFlush(bodyHeight);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bodyHeight
        BodyHeight updatedBodyHeight = bodyHeightRepository.findById(bodyHeight.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBodyHeight are not directly saved in db
        em.detach(updatedBodyHeight);
        updatedBodyHeight.height(UPDATED_HEIGHT);

        restBodyHeightMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBodyHeight.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedBodyHeight))
            )
            .andExpect(status().isOk());

        // Validate the BodyHeight in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBodyHeightToMatchAllProperties(updatedBodyHeight);
    }

    @Test
    @Transactional
    void putNonExistingBodyHeight() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bodyHeight.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBodyHeightMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bodyHeight.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bodyHeight))
            )
            .andExpect(status().isBadRequest());

        // Validate the BodyHeight in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBodyHeight() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bodyHeight.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodyHeightMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bodyHeight))
            )
            .andExpect(status().isBadRequest());

        // Validate the BodyHeight in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBodyHeight() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bodyHeight.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodyHeightMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bodyHeight)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BodyHeight in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBodyHeightWithPatch() throws Exception {
        // Initialize the database
        bodyHeightRepository.saveAndFlush(bodyHeight);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bodyHeight using partial update
        BodyHeight partialUpdatedBodyHeight = new BodyHeight();
        partialUpdatedBodyHeight.setId(bodyHeight.getId());

        partialUpdatedBodyHeight.height(UPDATED_HEIGHT);

        restBodyHeightMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBodyHeight.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBodyHeight))
            )
            .andExpect(status().isOk());

        // Validate the BodyHeight in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBodyHeightUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBodyHeight, bodyHeight),
            getPersistedBodyHeight(bodyHeight)
        );
    }

    @Test
    @Transactional
    void fullUpdateBodyHeightWithPatch() throws Exception {
        // Initialize the database
        bodyHeightRepository.saveAndFlush(bodyHeight);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bodyHeight using partial update
        BodyHeight partialUpdatedBodyHeight = new BodyHeight();
        partialUpdatedBodyHeight.setId(bodyHeight.getId());

        partialUpdatedBodyHeight.height(UPDATED_HEIGHT);

        restBodyHeightMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBodyHeight.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBodyHeight))
            )
            .andExpect(status().isOk());

        // Validate the BodyHeight in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBodyHeightUpdatableFieldsEquals(partialUpdatedBodyHeight, getPersistedBodyHeight(partialUpdatedBodyHeight));
    }

    @Test
    @Transactional
    void patchNonExistingBodyHeight() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bodyHeight.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBodyHeightMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bodyHeight.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bodyHeight))
            )
            .andExpect(status().isBadRequest());

        // Validate the BodyHeight in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBodyHeight() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bodyHeight.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodyHeightMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bodyHeight))
            )
            .andExpect(status().isBadRequest());

        // Validate the BodyHeight in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBodyHeight() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bodyHeight.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodyHeightMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bodyHeight)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BodyHeight in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBodyHeight() throws Exception {
        // Initialize the database
        bodyHeightRepository.saveAndFlush(bodyHeight);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bodyHeight
        restBodyHeightMockMvc
            .perform(delete(ENTITY_API_URL_ID, bodyHeight.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bodyHeightRepository.count();
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

    protected BodyHeight getPersistedBodyHeight(BodyHeight bodyHeight) {
        return bodyHeightRepository.findById(bodyHeight.getId()).orElseThrow();
    }

    protected void assertPersistedBodyHeightToMatchAllProperties(BodyHeight expectedBodyHeight) {
        assertBodyHeightAllPropertiesEquals(expectedBodyHeight, getPersistedBodyHeight(expectedBodyHeight));
    }

    protected void assertPersistedBodyHeightToMatchUpdatableProperties(BodyHeight expectedBodyHeight) {
        assertBodyHeightAllUpdatablePropertiesEquals(expectedBodyHeight, getPersistedBodyHeight(expectedBodyHeight));
    }
}
