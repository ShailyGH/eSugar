package rocks.zipcode.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rocks.zipcode.domain.BloodGlucoseTestSamples.*;
import static rocks.zipcode.domain.UserProfileTestSamples.*;

import org.junit.jupiter.api.Test;
import rocks.zipcode.web.rest.TestUtil;

class BloodGlucoseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BloodGlucose.class);
        BloodGlucose bloodGlucose1 = getBloodGlucoseSample1();
        BloodGlucose bloodGlucose2 = new BloodGlucose();
        assertThat(bloodGlucose1).isNotEqualTo(bloodGlucose2);

        bloodGlucose2.setId(bloodGlucose1.getId());
        assertThat(bloodGlucose1).isEqualTo(bloodGlucose2);

        bloodGlucose2 = getBloodGlucoseSample2();
        assertThat(bloodGlucose1).isNotEqualTo(bloodGlucose2);
    }

    @Test
    void userProfileTest() throws Exception {
        BloodGlucose bloodGlucose = getBloodGlucoseRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        bloodGlucose.setUserProfile(userProfileBack);
        assertThat(bloodGlucose.getUserProfile()).isEqualTo(userProfileBack);

        bloodGlucose.userProfile(null);
        assertThat(bloodGlucose.getUserProfile()).isNull();
    }
}
