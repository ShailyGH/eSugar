package rocks.zipcode.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rocks.zipcode.domain.CharacteristicDataTestSamples.*;
import static rocks.zipcode.domain.UserProfileTestSamples.*;

import org.junit.jupiter.api.Test;
import rocks.zipcode.web.rest.TestUtil;

class CharacteristicDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CharacteristicData.class);
        CharacteristicData characteristicData1 = getCharacteristicDataSample1();
        CharacteristicData characteristicData2 = new CharacteristicData();
        assertThat(characteristicData1).isNotEqualTo(characteristicData2);

        characteristicData2.setId(characteristicData1.getId());
        assertThat(characteristicData1).isEqualTo(characteristicData2);

        characteristicData2 = getCharacteristicDataSample2();
        assertThat(characteristicData1).isNotEqualTo(characteristicData2);
    }

    @Test
    void userProfileTest() throws Exception {
        CharacteristicData characteristicData = getCharacteristicDataRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        characteristicData.setUserProfile(userProfileBack);
        assertThat(characteristicData.getUserProfile()).isEqualTo(userProfileBack);

        characteristicData.userProfile(null);
        assertThat(characteristicData.getUserProfile()).isNull();
    }
}
