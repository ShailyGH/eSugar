package rocks.zipcode.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rocks.zipcode.domain.CharacteristicDataAsserts.*;
import static rocks.zipcode.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
import rocks.zipcode.domain.CharacteristicData;
import rocks.zipcode.repository.CharacteristicDataRepository;

/**
 * Integration tests for the {@link CharacteristicDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CharacteristicDataResourceIT {

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final String DEFAULT_BLOOD_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_BLOOD_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/characteristic-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CharacteristicDataRepository characteristicDataRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCharacteristicDataMockMvc;

    private CharacteristicData characteristicData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CharacteristicData createEntity(EntityManager em) {
        CharacteristicData characteristicData = new CharacteristicData()
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .gender(DEFAULT_GENDER)
            .bloodType(DEFAULT_BLOOD_TYPE);
        return characteristicData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CharacteristicData createUpdatedEntity(EntityManager em) {
        CharacteristicData characteristicData = new CharacteristicData()
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .gender(UPDATED_GENDER)
            .bloodType(UPDATED_BLOOD_TYPE);
        return characteristicData;
    }

    @BeforeEach
    public void initTest() {
        characteristicData = createEntity(em);
    }

    @Test
    @Transactional
    void createCharacteristicData() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CharacteristicData
        var returnedCharacteristicData = om.readValue(
            restCharacteristicDataMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(characteristicData)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CharacteristicData.class
        );

        // Validate the CharacteristicData in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCharacteristicDataUpdatableFieldsEquals(
            returnedCharacteristicData,
            getPersistedCharacteristicData(returnedCharacteristicData)
        );
    }

    @Test
    @Transactional
    void createCharacteristicDataWithExistingId() throws Exception {
        // Create the CharacteristicData with an existing ID
        characteristicData.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCharacteristicDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(characteristicData)))
            .andExpect(status().isBadRequest());

        // Validate the CharacteristicData in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCharacteristicData() throws Exception {
        // Initialize the database
        characteristicDataRepository.saveAndFlush(characteristicData);

        // Get all the characteristicDataList
        restCharacteristicDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(characteristicData.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].bloodType").value(hasItem(DEFAULT_BLOOD_TYPE)));
    }

    @Test
    @Transactional
    void getCharacteristicData() throws Exception {
        // Initialize the database
        characteristicDataRepository.saveAndFlush(characteristicData);

        // Get the characteristicData
        restCharacteristicDataMockMvc
            .perform(get(ENTITY_API_URL_ID, characteristicData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(characteristicData.getId().intValue()))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.bloodType").value(DEFAULT_BLOOD_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingCharacteristicData() throws Exception {
        // Get the characteristicData
        restCharacteristicDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCharacteristicData() throws Exception {
        // Initialize the database
        characteristicDataRepository.saveAndFlush(characteristicData);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the characteristicData
        CharacteristicData updatedCharacteristicData = characteristicDataRepository.findById(characteristicData.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCharacteristicData are not directly saved in db
        em.detach(updatedCharacteristicData);
        updatedCharacteristicData.dateOfBirth(UPDATED_DATE_OF_BIRTH).gender(UPDATED_GENDER).bloodType(UPDATED_BLOOD_TYPE);

        restCharacteristicDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCharacteristicData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCharacteristicData))
            )
            .andExpect(status().isOk());

        // Validate the CharacteristicData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCharacteristicDataToMatchAllProperties(updatedCharacteristicData);
    }

    @Test
    @Transactional
    void putNonExistingCharacteristicData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        characteristicData.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCharacteristicDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, characteristicData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(characteristicData))
            )
            .andExpect(status().isBadRequest());

        // Validate the CharacteristicData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCharacteristicData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        characteristicData.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacteristicDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(characteristicData))
            )
            .andExpect(status().isBadRequest());

        // Validate the CharacteristicData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCharacteristicData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        characteristicData.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacteristicDataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(characteristicData)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CharacteristicData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCharacteristicDataWithPatch() throws Exception {
        // Initialize the database
        characteristicDataRepository.saveAndFlush(characteristicData);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the characteristicData using partial update
        CharacteristicData partialUpdatedCharacteristicData = new CharacteristicData();
        partialUpdatedCharacteristicData.setId(characteristicData.getId());

        partialUpdatedCharacteristicData.gender(UPDATED_GENDER);

        restCharacteristicDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCharacteristicData.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCharacteristicData))
            )
            .andExpect(status().isOk());

        // Validate the CharacteristicData in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCharacteristicDataUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCharacteristicData, characteristicData),
            getPersistedCharacteristicData(characteristicData)
        );
    }

    @Test
    @Transactional
    void fullUpdateCharacteristicDataWithPatch() throws Exception {
        // Initialize the database
        characteristicDataRepository.saveAndFlush(characteristicData);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the characteristicData using partial update
        CharacteristicData partialUpdatedCharacteristicData = new CharacteristicData();
        partialUpdatedCharacteristicData.setId(characteristicData.getId());

        partialUpdatedCharacteristicData.dateOfBirth(UPDATED_DATE_OF_BIRTH).gender(UPDATED_GENDER).bloodType(UPDATED_BLOOD_TYPE);

        restCharacteristicDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCharacteristicData.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCharacteristicData))
            )
            .andExpect(status().isOk());

        // Validate the CharacteristicData in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCharacteristicDataUpdatableFieldsEquals(
            partialUpdatedCharacteristicData,
            getPersistedCharacteristicData(partialUpdatedCharacteristicData)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCharacteristicData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        characteristicData.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCharacteristicDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, characteristicData.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(characteristicData))
            )
            .andExpect(status().isBadRequest());

        // Validate the CharacteristicData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCharacteristicData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        characteristicData.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacteristicDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(characteristicData))
            )
            .andExpect(status().isBadRequest());

        // Validate the CharacteristicData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCharacteristicData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        characteristicData.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacteristicDataMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(characteristicData)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CharacteristicData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCharacteristicData() throws Exception {
        // Initialize the database
        characteristicDataRepository.saveAndFlush(characteristicData);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the characteristicData
        restCharacteristicDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, characteristicData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return characteristicDataRepository.count();
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

    protected CharacteristicData getPersistedCharacteristicData(CharacteristicData characteristicData) {
        return characteristicDataRepository.findById(characteristicData.getId()).orElseThrow();
    }

    protected void assertPersistedCharacteristicDataToMatchAllProperties(CharacteristicData expectedCharacteristicData) {
        assertCharacteristicDataAllPropertiesEquals(expectedCharacteristicData, getPersistedCharacteristicData(expectedCharacteristicData));
    }

    protected void assertPersistedCharacteristicDataToMatchUpdatableProperties(CharacteristicData expectedCharacteristicData) {
        assertCharacteristicDataAllUpdatablePropertiesEquals(
            expectedCharacteristicData,
            getPersistedCharacteristicData(expectedCharacteristicData)
        );
    }
}
