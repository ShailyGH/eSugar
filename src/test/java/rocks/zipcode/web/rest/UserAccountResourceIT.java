package rocks.zipcode.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rocks.zipcode.domain.UserAccountAsserts.*;
import static rocks.zipcode.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import rocks.zipcode.IntegrationTest;
import rocks.zipcode.domain.UserAccount;
import rocks.zipcode.repository.UserAccountRepository;

/**
 * Integration tests for the {@link UserAccountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserAccountResourceIT {

    private static final String DEFAULT_LOGIN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_STREET_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_ZIPCODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIPCODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_PHONE_NUMBER = 1;
    private static final Integer UPDATED_PHONE_NUMBER = 2;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_IS_ACTIVE = "AAAAAAAAAA";
    private static final String UPDATED_IS_ACTIVE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserAccountMockMvc;

    private UserAccount userAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAccount createEntity(EntityManager em) {
        UserAccount userAccount = new UserAccount()
            .loginName(DEFAULT_LOGIN_NAME)
            .password(DEFAULT_PASSWORD)
            .streetAddress(DEFAULT_STREET_ADDRESS)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .country(DEFAULT_COUNTRY)
            .zipcode(DEFAULT_ZIPCODE)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .email(DEFAULT_EMAIL)
            .isActive(DEFAULT_IS_ACTIVE);
        return userAccount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAccount createUpdatedEntity(EntityManager em) {
        UserAccount userAccount = new UserAccount()
            .loginName(UPDATED_LOGIN_NAME)
            .password(UPDATED_PASSWORD)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .country(UPDATED_COUNTRY)
            .zipcode(UPDATED_ZIPCODE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE);
        return userAccount;
    }

    @BeforeEach
    public void initTest() {
        userAccount = createEntity(em);
    }

    @Test
    @Transactional
    void createUserAccount() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserAccount
        var returnedUserAccount = om.readValue(
            restUserAccountMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAccount)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserAccount.class
        );

        // Validate the UserAccount in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUserAccountUpdatableFieldsEquals(returnedUserAccount, getPersistedUserAccount(returnedUserAccount));
    }

    @Test
    @Transactional
    void createUserAccountWithExistingId() throws Exception {
        // Create the UserAccount with an existing ID
        userAccount.setId(1);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAccount)))
            .andExpect(status().isBadRequest());

        // Validate the UserAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserAccounts() throws Exception {
        // Initialize the database
        userAccountRepository.saveAndFlush(userAccount);

        // Get all the userAccountList
        restUserAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].loginName").value(hasItem(DEFAULT_LOGIN_NAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].zipcode").value(hasItem(DEFAULT_ZIPCODE)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    @Test
    @Transactional
    void getUserAccount() throws Exception {
        // Initialize the database
        userAccountRepository.saveAndFlush(userAccount);

        // Get the userAccount
        restUserAccountMockMvc
            .perform(get(ENTITY_API_URL_ID, userAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userAccount.getId().intValue()))
            .andExpect(jsonPath("$.loginName").value(DEFAULT_LOGIN_NAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.zipcode").value(DEFAULT_ZIPCODE))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE));
    }

    @Test
    @Transactional
    void getNonExistingUserAccount() throws Exception {
        // Get the userAccount
        restUserAccountMockMvc.perform(get(ENTITY_API_URL_ID, Integer.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserAccount() throws Exception {
        // Initialize the database
        userAccountRepository.saveAndFlush(userAccount);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAccount
        UserAccount updatedUserAccount = userAccountRepository.findById(userAccount.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserAccount are not directly saved in db
        em.detach(updatedUserAccount);
        updatedUserAccount
            .loginName(UPDATED_LOGIN_NAME)
            .password(UPDATED_PASSWORD)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .country(UPDATED_COUNTRY)
            .zipcode(UPDATED_ZIPCODE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE);

        restUserAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserAccount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUserAccount))
            )
            .andExpect(status().isOk());

        // Validate the UserAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserAccountToMatchAllProperties(updatedUserAccount);
    }

    @Test
    @Transactional
    void putNonExistingUserAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAccount.setId(intCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAccount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAccount.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAccount.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAccountMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAccount)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserAccountWithPatch() throws Exception {
        // Initialize the database
        userAccountRepository.saveAndFlush(userAccount);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAccount using partial update
        UserAccount partialUpdatedUserAccount = new UserAccount();
        partialUpdatedUserAccount.setId(userAccount.getId());

        partialUpdatedUserAccount
            .loginName(UPDATED_LOGIN_NAME)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipcode(UPDATED_ZIPCODE)
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE);

        restUserAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserAccount))
            )
            .andExpect(status().isOk());

        // Validate the UserAccount in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserAccountUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserAccount, userAccount),
            getPersistedUserAccount(userAccount)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserAccountWithPatch() throws Exception {
        // Initialize the database
        userAccountRepository.saveAndFlush(userAccount);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAccount using partial update
        UserAccount partialUpdatedUserAccount = new UserAccount();
        partialUpdatedUserAccount.setId(userAccount.getId());

        partialUpdatedUserAccount
            .loginName(UPDATED_LOGIN_NAME)
            .password(UPDATED_PASSWORD)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .country(UPDATED_COUNTRY)
            .zipcode(UPDATED_ZIPCODE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE);

        restUserAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserAccount))
            )
            .andExpect(status().isOk());

        // Validate the UserAccount in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserAccountUpdatableFieldsEquals(partialUpdatedUserAccount, getPersistedUserAccount(partialUpdatedUserAccount));
    }

    @Test
    @Transactional
    void patchNonExistingUserAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAccount.setId(intCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAccount.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAccount.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAccountMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userAccount)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserAccount() throws Exception {
        // Initialize the database
        userAccountRepository.saveAndFlush(userAccount);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userAccount
        restUserAccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, userAccount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userAccountRepository.count();
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

    protected UserAccount getPersistedUserAccount(UserAccount userAccount) {
        return userAccountRepository.findById(userAccount.getId()).orElseThrow();
    }

    protected void assertPersistedUserAccountToMatchAllProperties(UserAccount expectedUserAccount) {
        assertUserAccountAllPropertiesEquals(expectedUserAccount, getPersistedUserAccount(expectedUserAccount));
    }

    protected void assertPersistedUserAccountToMatchUpdatableProperties(UserAccount expectedUserAccount) {
        assertUserAccountAllUpdatablePropertiesEquals(expectedUserAccount, getPersistedUserAccount(expectedUserAccount));
    }
}
