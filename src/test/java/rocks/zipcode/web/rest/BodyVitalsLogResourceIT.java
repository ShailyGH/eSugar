package rocks.zipcode.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rocks.zipcode.domain.BodyVitalsLogAsserts.*;
import static rocks.zipcode.web.rest.TestUtil.createUpdateProxyForBean;
import static rocks.zipcode.web.rest.TestUtil.sameInstant;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
import rocks.zipcode.domain.BodyVitalsLog;
import rocks.zipcode.repository.BodyVitalsLogRepository;

/**
 * Integration tests for the {@link BodyVitalsLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BodyVitalsLogResourceIT {

    private static final ZonedDateTime DEFAULT_DT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DT_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/body-vitals-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BodyVitalsLogRepository bodyVitalsLogRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBodyVitalsLogMockMvc;

    private BodyVitalsLog bodyVitalsLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BodyVitalsLog createEntity(EntityManager em) {
        BodyVitalsLog bodyVitalsLog = new BodyVitalsLog().dtCreated(DEFAULT_DT_CREATED);
        return bodyVitalsLog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BodyVitalsLog createUpdatedEntity(EntityManager em) {
        BodyVitalsLog bodyVitalsLog = new BodyVitalsLog().dtCreated(UPDATED_DT_CREATED);
        return bodyVitalsLog;
    }

    @BeforeEach
    public void initTest() {
        bodyVitalsLog = createEntity(em);
    }

    @Test
    @Transactional
    void createBodyVitalsLog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BodyVitalsLog
        var returnedBodyVitalsLog = om.readValue(
            restBodyVitalsLogMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bodyVitalsLog)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BodyVitalsLog.class
        );

        // Validate the BodyVitalsLog in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertBodyVitalsLogUpdatableFieldsEquals(returnedBodyVitalsLog, getPersistedBodyVitalsLog(returnedBodyVitalsLog));
    }

    @Test
    @Transactional
    void createBodyVitalsLogWithExistingId() throws Exception {
        // Create the BodyVitalsLog with an existing ID
        bodyVitalsLog.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBodyVitalsLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bodyVitalsLog)))
            .andExpect(status().isBadRequest());

        // Validate the BodyVitalsLog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBodyVitalsLogs() throws Exception {
        // Initialize the database
        bodyVitalsLogRepository.saveAndFlush(bodyVitalsLog);

        // Get all the bodyVitalsLogList
        restBodyVitalsLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bodyVitalsLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].dtCreated").value(hasItem(sameInstant(DEFAULT_DT_CREATED))));
    }

    @Test
    @Transactional
    void getBodyVitalsLog() throws Exception {
        // Initialize the database
        bodyVitalsLogRepository.saveAndFlush(bodyVitalsLog);

        // Get the bodyVitalsLog
        restBodyVitalsLogMockMvc
            .perform(get(ENTITY_API_URL_ID, bodyVitalsLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bodyVitalsLog.getId().intValue()))
            .andExpect(jsonPath("$.dtCreated").value(sameInstant(DEFAULT_DT_CREATED)));
    }

    @Test
    @Transactional
    void getNonExistingBodyVitalsLog() throws Exception {
        // Get the bodyVitalsLog
        restBodyVitalsLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBodyVitalsLog() throws Exception {
        // Initialize the database
        bodyVitalsLogRepository.saveAndFlush(bodyVitalsLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bodyVitalsLog
        BodyVitalsLog updatedBodyVitalsLog = bodyVitalsLogRepository.findById(bodyVitalsLog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBodyVitalsLog are not directly saved in db
        em.detach(updatedBodyVitalsLog);
        updatedBodyVitalsLog.dtCreated(UPDATED_DT_CREATED);

        restBodyVitalsLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBodyVitalsLog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedBodyVitalsLog))
            )
            .andExpect(status().isOk());

        // Validate the BodyVitalsLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBodyVitalsLogToMatchAllProperties(updatedBodyVitalsLog);
    }

    @Test
    @Transactional
    void putNonExistingBodyVitalsLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bodyVitalsLog.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBodyVitalsLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bodyVitalsLog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bodyVitalsLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the BodyVitalsLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBodyVitalsLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bodyVitalsLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodyVitalsLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bodyVitalsLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the BodyVitalsLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBodyVitalsLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bodyVitalsLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodyVitalsLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bodyVitalsLog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BodyVitalsLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBodyVitalsLogWithPatch() throws Exception {
        // Initialize the database
        bodyVitalsLogRepository.saveAndFlush(bodyVitalsLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bodyVitalsLog using partial update
        BodyVitalsLog partialUpdatedBodyVitalsLog = new BodyVitalsLog();
        partialUpdatedBodyVitalsLog.setId(bodyVitalsLog.getId());

        restBodyVitalsLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBodyVitalsLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBodyVitalsLog))
            )
            .andExpect(status().isOk());

        // Validate the BodyVitalsLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBodyVitalsLogUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBodyVitalsLog, bodyVitalsLog),
            getPersistedBodyVitalsLog(bodyVitalsLog)
        );
    }

    @Test
    @Transactional
    void fullUpdateBodyVitalsLogWithPatch() throws Exception {
        // Initialize the database
        bodyVitalsLogRepository.saveAndFlush(bodyVitalsLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bodyVitalsLog using partial update
        BodyVitalsLog partialUpdatedBodyVitalsLog = new BodyVitalsLog();
        partialUpdatedBodyVitalsLog.setId(bodyVitalsLog.getId());

        partialUpdatedBodyVitalsLog.dtCreated(UPDATED_DT_CREATED);

        restBodyVitalsLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBodyVitalsLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBodyVitalsLog))
            )
            .andExpect(status().isOk());

        // Validate the BodyVitalsLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBodyVitalsLogUpdatableFieldsEquals(partialUpdatedBodyVitalsLog, getPersistedBodyVitalsLog(partialUpdatedBodyVitalsLog));
    }

    @Test
    @Transactional
    void patchNonExistingBodyVitalsLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bodyVitalsLog.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBodyVitalsLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bodyVitalsLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bodyVitalsLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the BodyVitalsLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBodyVitalsLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bodyVitalsLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodyVitalsLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bodyVitalsLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the BodyVitalsLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBodyVitalsLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bodyVitalsLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodyVitalsLogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bodyVitalsLog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BodyVitalsLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBodyVitalsLog() throws Exception {
        // Initialize the database
        bodyVitalsLogRepository.saveAndFlush(bodyVitalsLog);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bodyVitalsLog
        restBodyVitalsLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, bodyVitalsLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bodyVitalsLogRepository.count();
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

    protected BodyVitalsLog getPersistedBodyVitalsLog(BodyVitalsLog bodyVitalsLog) {
        return bodyVitalsLogRepository.findById(bodyVitalsLog.getId()).orElseThrow();
    }

    protected void assertPersistedBodyVitalsLogToMatchAllProperties(BodyVitalsLog expectedBodyVitalsLog) {
        assertBodyVitalsLogAllPropertiesEquals(expectedBodyVitalsLog, getPersistedBodyVitalsLog(expectedBodyVitalsLog));
    }

    protected void assertPersistedBodyVitalsLogToMatchUpdatableProperties(BodyVitalsLog expectedBodyVitalsLog) {
        assertBodyVitalsLogAllUpdatablePropertiesEquals(expectedBodyVitalsLog, getPersistedBodyVitalsLog(expectedBodyVitalsLog));
    }
}
