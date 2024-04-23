package rocks.zipcode.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rocks.zipcode.domain.BodyHeightTestSamples.*;
import static rocks.zipcode.domain.UserProfileTestSamples.*;

import org.junit.jupiter.api.Test;
import rocks.zipcode.web.rest.TestUtil;

class BodyHeightTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BodyHeight.class);
        BodyHeight bodyHeight1 = getBodyHeightSample1();
        BodyHeight bodyHeight2 = new BodyHeight();
        assertThat(bodyHeight1).isNotEqualTo(bodyHeight2);

        bodyHeight2.setId(bodyHeight1.getId());
        assertThat(bodyHeight1).isEqualTo(bodyHeight2);

        bodyHeight2 = getBodyHeightSample2();
        assertThat(bodyHeight1).isNotEqualTo(bodyHeight2);
    }

    @Test
    void userProfileTest() throws Exception {
        BodyHeight bodyHeight = getBodyHeightRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        bodyHeight.setUserProfile(userProfileBack);
        assertThat(bodyHeight.getUserProfile()).isEqualTo(userProfileBack);

        bodyHeight.userProfile(null);
        assertThat(bodyHeight.getUserProfile()).isNull();
    }
}
