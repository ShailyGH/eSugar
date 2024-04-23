package rocks.zipcode.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rocks.zipcode.domain.ActivityAsserts.*;
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
import rocks.zipcode.domain.Activity;
import rocks.zipcode.repository.ActivityRepository;

/**
 * Integration tests for the {@link ActivityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ActivityResourceIT {

    private static final String DEFAULT_ACTIVITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVITY_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_ACTIVITY_MULTIPLIER = 1F;
    private static final Float UPDATED_ACTIVITY_MULTIPLIER = 2F;

    private static final String ENTITY_API_URL = "/api/activities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActivityMockMvc;

    private Activity activity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Activity createEntity(EntityManager em) {
        Activity activity = new Activity().activityName(DEFAULT_ACTIVITY_NAME).activityMultiplier(DEFAULT_ACTIVITY_MULTIPLIER);
        return activity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Activity createUpdatedEntity(EntityManager em) {
        Activity activity = new Activity().activityName(UPDATED_ACTIVITY_NAME).activityMultiplier(UPDATED_ACTIVITY_MULTIPLIER);
        return activity;
    }

    @BeforeEach
    public void initTest() {
        activity = createEntity(em);
    }

    @Test
    @Transactional
    void createActivity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Activity
        var returnedActivity = om.readValue(
            restActivityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activity)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Activity.class
        );

        // Validate the Activity in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertActivityUpdatableFieldsEquals(returnedActivity, getPersistedActivity(returnedActivity));
    }

    @Test
    @Transactional
    void createActivityWithExistingId() throws Exception {
        // Create the Activity with an existing ID
        activity.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activity)))
            .andExpect(status().isBadRequest());

        // Validate the Activity in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllActivities() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList
        restActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activity.getId().intValue())))
            .andExpect(jsonPath("$.[*].activityName").value(hasItem(DEFAULT_ACTIVITY_NAME)))
            .andExpect(jsonPath("$.[*].activityMultiplier").value(hasItem(DEFAULT_ACTIVITY_MULTIPLIER.doubleValue())));
    }

    @Test
    @Transactional
    void getActivity() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get the activity
        restActivityMockMvc
            .perform(get(ENTITY_API_URL_ID, activity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(activity.getId().intValue()))
            .andExpect(jsonPath("$.activityName").value(DEFAULT_ACTIVITY_NAME))
            .andExpect(jsonPath("$.activityMultiplier").value(DEFAULT_ACTIVITY_MULTIPLIER.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingActivity() throws Exception {
        // Get the activity
        restActivityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingActivity() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activity
        Activity updatedActivity = activityRepository.findById(activity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedActivity are not directly saved in db
        em.detach(updatedActivity);
        updatedActivity.activityName(UPDATED_ACTIVITY_NAME).activityMultiplier(UPDATED_ACTIVITY_MULTIPLIER);

        restActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedActivity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedActivity))
            )
            .andExpect(status().isOk());

        // Validate the Activity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedActivityToMatchAllProperties(updatedActivity);
    }

    @Test
    @Transactional
    void putNonExistingActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activity.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, activity.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activity))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(activity))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activity)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Activity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateActivityWithPatch() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activity using partial update
        Activity partialUpdatedActivity = new Activity();
        partialUpdatedActivity.setId(activity.getId());

        partialUpdatedActivity.activityMultiplier(UPDATED_ACTIVITY_MULTIPLIER);

        restActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedActivity))
            )
            .andExpect(status().isOk());

        // Validate the Activity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertActivityUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedActivity, activity), getPersistedActivity(activity));
    }

    @Test
    @Transactional
    void fullUpdateActivityWithPatch() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activity using partial update
        Activity partialUpdatedActivity = new Activity();
        partialUpdatedActivity.setId(activity.getId());

        partialUpdatedActivity.activityName(UPDATED_ACTIVITY_NAME).activityMultiplier(UPDATED_ACTIVITY_MULTIPLIER);

        restActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedActivity))
            )
            .andExpect(status().isOk());

        // Validate the Activity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertActivityUpdatableFieldsEquals(partialUpdatedActivity, getPersistedActivity(partialUpdatedActivity));
    }

    @Test
    @Transactional
    void patchNonExistingActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activity.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, activity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(activity))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(activity))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(activity)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Activity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteActivity() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the activity
        restActivityMockMvc
            .perform(delete(ENTITY_API_URL_ID, activity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return activityRepository.count();
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

    protected Activity getPersistedActivity(Activity activity) {
        return activityRepository.findById(activity.getId()).orElseThrow();
    }

    protected void assertPersistedActivityToMatchAllProperties(Activity expectedActivity) {
        assertActivityAllPropertiesEquals(expectedActivity, getPersistedActivity(expectedActivity));
    }

    protected void assertPersistedActivityToMatchUpdatableProperties(Activity expectedActivity) {
        assertActivityAllUpdatablePropertiesEquals(expectedActivity, getPersistedActivity(expectedActivity));
    }
}
