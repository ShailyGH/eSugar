package rocks.zipcode.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rocks.zipcode.domain.ActivityLogAsserts.*;
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
import rocks.zipcode.domain.ActivityLog;
import rocks.zipcode.repository.ActivityLogRepository;

/**
 * Integration tests for the {@link ActivityLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ActivityLogResourceIT {

    private static final ZonedDateTime DEFAULT_START_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Float DEFAULT_DISTANCE_COVERED = 1F;
    private static final Float UPDATED_DISTANCE_COVERED = 2F;

    private static final Integer DEFAULT_STEPS_COUNT = 1;
    private static final Integer UPDATED_STEPS_COUNT = 2;

    private static final Float DEFAULT_CALORIES_BURNT = 1F;
    private static final Float UPDATED_CALORIES_BURNT = 2F;

    private static final String ENTITY_API_URL = "/api/activity-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActivityLogMockMvc;

    private ActivityLog activityLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActivityLog createEntity(EntityManager em) {
        ActivityLog activityLog = new ActivityLog()
            .startDateTime(DEFAULT_START_DATE_TIME)
            .endDateTime(DEFAULT_END_DATE_TIME)
            .distanceCovered(DEFAULT_DISTANCE_COVERED)
            .stepsCount(DEFAULT_STEPS_COUNT)
            .caloriesBurnt(DEFAULT_CALORIES_BURNT);
        return activityLog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActivityLog createUpdatedEntity(EntityManager em) {
        ActivityLog activityLog = new ActivityLog()
            .startDateTime(UPDATED_START_DATE_TIME)
            .endDateTime(UPDATED_END_DATE_TIME)
            .distanceCovered(UPDATED_DISTANCE_COVERED)
            .stepsCount(UPDATED_STEPS_COUNT)
            .caloriesBurnt(UPDATED_CALORIES_BURNT);
        return activityLog;
    }

    @BeforeEach
    public void initTest() {
        activityLog = createEntity(em);
    }

    @Test
    @Transactional
    void createActivityLog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ActivityLog
        var returnedActivityLog = om.readValue(
            restActivityLogMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activityLog)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ActivityLog.class
        );

        // Validate the ActivityLog in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertActivityLogUpdatableFieldsEquals(returnedActivityLog, getPersistedActivityLog(returnedActivityLog));
    }

    @Test
    @Transactional
    void createActivityLogWithExistingId() throws Exception {
        // Create the ActivityLog with an existing ID
        activityLog.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restActivityLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activityLog)))
            .andExpect(status().isBadRequest());

        // Validate the ActivityLog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllActivityLogs() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList
        restActivityLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activityLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDateTime").value(hasItem(sameInstant(DEFAULT_START_DATE_TIME))))
            .andExpect(jsonPath("$.[*].endDateTime").value(hasItem(sameInstant(DEFAULT_END_DATE_TIME))))
            .andExpect(jsonPath("$.[*].distanceCovered").value(hasItem(DEFAULT_DISTANCE_COVERED.doubleValue())))
            .andExpect(jsonPath("$.[*].stepsCount").value(hasItem(DEFAULT_STEPS_COUNT)))
            .andExpect(jsonPath("$.[*].caloriesBurnt").value(hasItem(DEFAULT_CALORIES_BURNT.doubleValue())));
    }

    @Test
    @Transactional
    void getActivityLog() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get the activityLog
        restActivityLogMockMvc
            .perform(get(ENTITY_API_URL_ID, activityLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(activityLog.getId().intValue()))
            .andExpect(jsonPath("$.startDateTime").value(sameInstant(DEFAULT_START_DATE_TIME)))
            .andExpect(jsonPath("$.endDateTime").value(sameInstant(DEFAULT_END_DATE_TIME)))
            .andExpect(jsonPath("$.distanceCovered").value(DEFAULT_DISTANCE_COVERED.doubleValue()))
            .andExpect(jsonPath("$.stepsCount").value(DEFAULT_STEPS_COUNT))
            .andExpect(jsonPath("$.caloriesBurnt").value(DEFAULT_CALORIES_BURNT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingActivityLog() throws Exception {
        // Get the activityLog
        restActivityLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingActivityLog() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activityLog
        ActivityLog updatedActivityLog = activityLogRepository.findById(activityLog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedActivityLog are not directly saved in db
        em.detach(updatedActivityLog);
        updatedActivityLog
            .startDateTime(UPDATED_START_DATE_TIME)
            .endDateTime(UPDATED_END_DATE_TIME)
            .distanceCovered(UPDATED_DISTANCE_COVERED)
            .stepsCount(UPDATED_STEPS_COUNT)
            .caloriesBurnt(UPDATED_CALORIES_BURNT);

        restActivityLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedActivityLog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedActivityLog))
            )
            .andExpect(status().isOk());

        // Validate the ActivityLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedActivityLogToMatchAllProperties(updatedActivityLog);
    }

    @Test
    @Transactional
    void putNonExistingActivityLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activityLog.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActivityLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, activityLog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(activityLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActivityLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchActivityLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activityLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(activityLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActivityLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamActivityLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activityLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activityLog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ActivityLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateActivityLogWithPatch() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activityLog using partial update
        ActivityLog partialUpdatedActivityLog = new ActivityLog();
        partialUpdatedActivityLog.setId(activityLog.getId());

        partialUpdatedActivityLog
            .startDateTime(UPDATED_START_DATE_TIME)
            .distanceCovered(UPDATED_DISTANCE_COVERED)
            .caloriesBurnt(UPDATED_CALORIES_BURNT);

        restActivityLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActivityLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedActivityLog))
            )
            .andExpect(status().isOk());

        // Validate the ActivityLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertActivityLogUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedActivityLog, activityLog),
            getPersistedActivityLog(activityLog)
        );
    }

    @Test
    @Transactional
    void fullUpdateActivityLogWithPatch() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activityLog using partial update
        ActivityLog partialUpdatedActivityLog = new ActivityLog();
        partialUpdatedActivityLog.setId(activityLog.getId());

        partialUpdatedActivityLog
            .startDateTime(UPDATED_START_DATE_TIME)
            .endDateTime(UPDATED_END_DATE_TIME)
            .distanceCovered(UPDATED_DISTANCE_COVERED)
            .stepsCount(UPDATED_STEPS_COUNT)
            .caloriesBurnt(UPDATED_CALORIES_BURNT);

        restActivityLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActivityLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedActivityLog))
            )
            .andExpect(status().isOk());

        // Validate the ActivityLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertActivityLogUpdatableFieldsEquals(partialUpdatedActivityLog, getPersistedActivityLog(partialUpdatedActivityLog));
    }

    @Test
    @Transactional
    void patchNonExistingActivityLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activityLog.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActivityLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, activityLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(activityLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActivityLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchActivityLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activityLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(activityLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActivityLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamActivityLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activityLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityLogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(activityLog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ActivityLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteActivityLog() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the activityLog
        restActivityLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, activityLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return activityLogRepository.count();
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

    protected ActivityLog getPersistedActivityLog(ActivityLog activityLog) {
        return activityLogRepository.findById(activityLog.getId()).orElseThrow();
    }

    protected void assertPersistedActivityLogToMatchAllProperties(ActivityLog expectedActivityLog) {
        assertActivityLogAllPropertiesEquals(expectedActivityLog, getPersistedActivityLog(expectedActivityLog));
    }

    protected void assertPersistedActivityLogToMatchUpdatableProperties(ActivityLog expectedActivityLog) {
        assertActivityLogAllUpdatablePropertiesEquals(expectedActivityLog, getPersistedActivityLog(expectedActivityLog));
    }
}
