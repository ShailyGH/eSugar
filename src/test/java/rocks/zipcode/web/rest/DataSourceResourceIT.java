package rocks.zipcode.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rocks.zipcode.domain.DataSourceAsserts.*;
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
import rocks.zipcode.domain.DataSource;
import rocks.zipcode.repository.DataSourceRepository;

/**
 * Integration tests for the {@link DataSourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DataSourceResourceIT {

    private static final String DEFAULT_SOURCE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/data-sources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DataSourceRepository dataSourceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDataSourceMockMvc;

    private DataSource dataSource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataSource createEntity(EntityManager em) {
        DataSource dataSource = new DataSource().sourceName(DEFAULT_SOURCE_NAME);
        return dataSource;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataSource createUpdatedEntity(EntityManager em) {
        DataSource dataSource = new DataSource().sourceName(UPDATED_SOURCE_NAME);
        return dataSource;
    }

    @BeforeEach
    public void initTest() {
        dataSource = createEntity(em);
    }

    @Test
    @Transactional
    void createDataSource() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DataSource
        var returnedDataSource = om.readValue(
            restDataSourceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dataSource)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DataSource.class
        );

        // Validate the DataSource in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDataSourceUpdatableFieldsEquals(returnedDataSource, getPersistedDataSource(returnedDataSource));
    }

    @Test
    @Transactional
    void createDataSourceWithExistingId() throws Exception {
        // Create the DataSource with an existing ID
        dataSource.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataSourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dataSource)))
            .andExpect(status().isBadRequest());

        // Validate the DataSource in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDataSources() throws Exception {
        // Initialize the database
        dataSourceRepository.saveAndFlush(dataSource);

        // Get all the dataSourceList
        restDataSourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataSource.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceName").value(hasItem(DEFAULT_SOURCE_NAME)));
    }

    @Test
    @Transactional
    void getDataSource() throws Exception {
        // Initialize the database
        dataSourceRepository.saveAndFlush(dataSource);

        // Get the dataSource
        restDataSourceMockMvc
            .perform(get(ENTITY_API_URL_ID, dataSource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dataSource.getId().intValue()))
            .andExpect(jsonPath("$.sourceName").value(DEFAULT_SOURCE_NAME));
    }

    @Test
    @Transactional
    void getNonExistingDataSource() throws Exception {
        // Get the dataSource
        restDataSourceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDataSource() throws Exception {
        // Initialize the database
        dataSourceRepository.saveAndFlush(dataSource);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dataSource
        DataSource updatedDataSource = dataSourceRepository.findById(dataSource.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDataSource are not directly saved in db
        em.detach(updatedDataSource);
        updatedDataSource.sourceName(UPDATED_SOURCE_NAME);

        restDataSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDataSource.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDataSource))
            )
            .andExpect(status().isOk());

        // Validate the DataSource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDataSourceToMatchAllProperties(updatedDataSource);
    }

    @Test
    @Transactional
    void putNonExistingDataSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dataSource.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dataSource.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dataSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataSource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDataSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dataSource.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dataSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataSource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDataSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dataSource.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataSourceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dataSource)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataSource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDataSourceWithPatch() throws Exception {
        // Initialize the database
        dataSourceRepository.saveAndFlush(dataSource);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dataSource using partial update
        DataSource partialUpdatedDataSource = new DataSource();
        partialUpdatedDataSource.setId(dataSource.getId());

        partialUpdatedDataSource.sourceName(UPDATED_SOURCE_NAME);

        restDataSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDataSource))
            )
            .andExpect(status().isOk());

        // Validate the DataSource in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDataSourceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDataSource, dataSource),
            getPersistedDataSource(dataSource)
        );
    }

    @Test
    @Transactional
    void fullUpdateDataSourceWithPatch() throws Exception {
        // Initialize the database
        dataSourceRepository.saveAndFlush(dataSource);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dataSource using partial update
        DataSource partialUpdatedDataSource = new DataSource();
        partialUpdatedDataSource.setId(dataSource.getId());

        partialUpdatedDataSource.sourceName(UPDATED_SOURCE_NAME);

        restDataSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDataSource))
            )
            .andExpect(status().isOk());

        // Validate the DataSource in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDataSourceUpdatableFieldsEquals(partialUpdatedDataSource, getPersistedDataSource(partialUpdatedDataSource));
    }

    @Test
    @Transactional
    void patchNonExistingDataSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dataSource.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dataSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dataSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataSource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDataSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dataSource.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dataSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataSource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDataSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dataSource.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataSourceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(dataSource)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataSource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDataSource() throws Exception {
        // Initialize the database
        dataSourceRepository.saveAndFlush(dataSource);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the dataSource
        restDataSourceMockMvc
            .perform(delete(ENTITY_API_URL_ID, dataSource.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return dataSourceRepository.count();
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

    protected DataSource getPersistedDataSource(DataSource dataSource) {
        return dataSourceRepository.findById(dataSource.getId()).orElseThrow();
    }

    protected void assertPersistedDataSourceToMatchAllProperties(DataSource expectedDataSource) {
        assertDataSourceAllPropertiesEquals(expectedDataSource, getPersistedDataSource(expectedDataSource));
    }

    protected void assertPersistedDataSourceToMatchUpdatableProperties(DataSource expectedDataSource) {
        assertDataSourceAllUpdatablePropertiesEquals(expectedDataSource, getPersistedDataSource(expectedDataSource));
    }
}
