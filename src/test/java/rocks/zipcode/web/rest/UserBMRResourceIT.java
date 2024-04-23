package rocks.zipcode.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rocks.zipcode.domain.UserBMRAsserts.*;
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
import rocks.zipcode.domain.UserBMR;
import rocks.zipcode.repository.UserBMRRepository;

/**
 * Integration tests for the {@link UserBMRResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserBMRResourceIT {

    private static final Integer DEFAULT_ID_VERSION = 1;
    private static final Integer UPDATED_ID_VERSION = 2;

    private static final Float DEFAULT_BMR = 1F;
    private static final Float UPDATED_BMR = 2F;

    private static final ZonedDateTime DEFAULT_DT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DT_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DT_MODIFIED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DT_MODIFIED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/user-bmrs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserBMRRepository userBMRRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserBMRMockMvc;

    private UserBMR userBMR;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserBMR createEntity(EntityManager em) {
        UserBMR userBMR = new UserBMR()
            .idVersion(DEFAULT_ID_VERSION)
            .bmr(DEFAULT_BMR)
            .dtCreated(DEFAULT_DT_CREATED)
            .dtModified(DEFAULT_DT_MODIFIED);
        return userBMR;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserBMR createUpdatedEntity(EntityManager em) {
        UserBMR userBMR = new UserBMR()
            .idVersion(UPDATED_ID_VERSION)
            .bmr(UPDATED_BMR)
            .dtCreated(UPDATED_DT_CREATED)
            .dtModified(UPDATED_DT_MODIFIED);
        return userBMR;
    }

    @BeforeEach
    public void initTest() {
        userBMR = createEntity(em);
    }

    @Test
    @Transactional
    void createUserBMR() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserBMR
        var returnedUserBMR = om.readValue(
            restUserBMRMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userBMR)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserBMR.class
        );

        // Validate the UserBMR in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUserBMRUpdatableFieldsEquals(returnedUserBMR, getPersistedUserBMR(returnedUserBMR));
    }

    @Test
    @Transactional
    void createUserBMRWithExistingId() throws Exception {
        // Create the UserBMR with an existing ID
        userBMR.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserBMRMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userBMR)))
            .andExpect(status().isBadRequest());

        // Validate the UserBMR in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserBMRS() throws Exception {
        // Initialize the database
        userBMRRepository.saveAndFlush(userBMR);

        // Get all the userBMRList
        restUserBMRMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userBMR.getId().intValue())))
            .andExpect(jsonPath("$.[*].idVersion").value(hasItem(DEFAULT_ID_VERSION)))
            .andExpect(jsonPath("$.[*].bmr").value(hasItem(DEFAULT_BMR.doubleValue())))
            .andExpect(jsonPath("$.[*].dtCreated").value(hasItem(sameInstant(DEFAULT_DT_CREATED))))
            .andExpect(jsonPath("$.[*].dtModified").value(hasItem(sameInstant(DEFAULT_DT_MODIFIED))));
    }

    @Test
    @Transactional
    void getUserBMR() throws Exception {
        // Initialize the database
        userBMRRepository.saveAndFlush(userBMR);

        // Get the userBMR
        restUserBMRMockMvc
            .perform(get(ENTITY_API_URL_ID, userBMR.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userBMR.getId().intValue()))
            .andExpect(jsonPath("$.idVersion").value(DEFAULT_ID_VERSION))
            .andExpect(jsonPath("$.bmr").value(DEFAULT_BMR.doubleValue()))
            .andExpect(jsonPath("$.dtCreated").value(sameInstant(DEFAULT_DT_CREATED)))
            .andExpect(jsonPath("$.dtModified").value(sameInstant(DEFAULT_DT_MODIFIED)));
    }

    @Test
    @Transactional
    void getNonExistingUserBMR() throws Exception {
        // Get the userBMR
        restUserBMRMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserBMR() throws Exception {
        // Initialize the database
        userBMRRepository.saveAndFlush(userBMR);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userBMR
        UserBMR updatedUserBMR = userBMRRepository.findById(userBMR.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserBMR are not directly saved in db
        em.detach(updatedUserBMR);
        updatedUserBMR.idVersion(UPDATED_ID_VERSION).bmr(UPDATED_BMR).dtCreated(UPDATED_DT_CREATED).dtModified(UPDATED_DT_MODIFIED);

        restUserBMRMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserBMR.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUserBMR))
            )
            .andExpect(status().isOk());

        // Validate the UserBMR in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserBMRToMatchAllProperties(updatedUserBMR);
    }

    @Test
    @Transactional
    void putNonExistingUserBMR() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userBMR.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserBMRMockMvc
            .perform(put(ENTITY_API_URL_ID, userBMR.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userBMR)))
            .andExpect(status().isBadRequest());

        // Validate the UserBMR in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserBMR() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userBMR.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserBMRMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userBMR))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserBMR in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserBMR() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userBMR.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserBMRMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userBMR)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserBMR in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserBMRWithPatch() throws Exception {
        // Initialize the database
        userBMRRepository.saveAndFlush(userBMR);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userBMR using partial update
        UserBMR partialUpdatedUserBMR = new UserBMR();
        partialUpdatedUserBMR.setId(userBMR.getId());

        partialUpdatedUserBMR.bmr(UPDATED_BMR);

        restUserBMRMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserBMR.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserBMR))
            )
            .andExpect(status().isOk());

        // Validate the UserBMR in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserBMRUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedUserBMR, userBMR), getPersistedUserBMR(userBMR));
    }

    @Test
    @Transactional
    void fullUpdateUserBMRWithPatch() throws Exception {
        // Initialize the database
        userBMRRepository.saveAndFlush(userBMR);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userBMR using partial update
        UserBMR partialUpdatedUserBMR = new UserBMR();
        partialUpdatedUserBMR.setId(userBMR.getId());

        partialUpdatedUserBMR.idVersion(UPDATED_ID_VERSION).bmr(UPDATED_BMR).dtCreated(UPDATED_DT_CREATED).dtModified(UPDATED_DT_MODIFIED);

        restUserBMRMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserBMR.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserBMR))
            )
            .andExpect(status().isOk());

        // Validate the UserBMR in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserBMRUpdatableFieldsEquals(partialUpdatedUserBMR, getPersistedUserBMR(partialUpdatedUserBMR));
    }

    @Test
    @Transactional
    void patchNonExistingUserBMR() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userBMR.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserBMRMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userBMR.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userBMR))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserBMR in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserBMR() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userBMR.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserBMRMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userBMR))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserBMR in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserBMR() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userBMR.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserBMRMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userBMR)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserBMR in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserBMR() throws Exception {
        // Initialize the database
        userBMRRepository.saveAndFlush(userBMR);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userBMR
        restUserBMRMockMvc
            .perform(delete(ENTITY_API_URL_ID, userBMR.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userBMRRepository.count();
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

    protected UserBMR getPersistedUserBMR(UserBMR userBMR) {
        return userBMRRepository.findById(userBMR.getId()).orElseThrow();
    }

    protected void assertPersistedUserBMRToMatchAllProperties(UserBMR expectedUserBMR) {
        assertUserBMRAllPropertiesEquals(expectedUserBMR, getPersistedUserBMR(expectedUserBMR));
    }

    protected void assertPersistedUserBMRToMatchUpdatableProperties(UserBMR expectedUserBMR) {
        assertUserBMRAllUpdatablePropertiesEquals(expectedUserBMR, getPersistedUserBMR(expectedUserBMR));
    }
}
