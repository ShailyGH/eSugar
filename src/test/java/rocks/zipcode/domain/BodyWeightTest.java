package rocks.zipcode.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rocks.zipcode.domain.BodyWeightTestSamples.*;
import static rocks.zipcode.domain.UserProfileTestSamples.*;

import org.junit.jupiter.api.Test;
import rocks.zipcode.web.rest.TestUtil;

class BodyWeightTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BodyWeight.class);
        BodyWeight bodyWeight1 = getBodyWeightSample1();
        BodyWeight bodyWeight2 = new BodyWeight();
        assertThat(bodyWeight1).isNotEqualTo(bodyWeight2);

        bodyWeight2.setId(bodyWeight1.getId());
        assertThat(bodyWeight1).isEqualTo(bodyWeight2);

        bodyWeight2 = getBodyWeightSample2();
        assertThat(bodyWeight1).isNotEqualTo(bodyWeight2);
    }

    @Test
    void userProfileTest() throws Exception {
        BodyWeight bodyWeight = getBodyWeightRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        bodyWeight.setUserProfile(userProfileBack);
        assertThat(bodyWeight.getUserProfile()).isEqualTo(userProfileBack);

        bodyWeight.userProfile(null);
        assertThat(bodyWeight.getUserProfile()).isNull();
    }
}
