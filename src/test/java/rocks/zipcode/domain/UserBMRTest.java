package rocks.zipcode.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rocks.zipcode.domain.UserBMRTestSamples.*;
import static rocks.zipcode.domain.UserProfileTestSamples.*;

import org.junit.jupiter.api.Test;
import rocks.zipcode.web.rest.TestUtil;

class UserBMRTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserBMR.class);
        UserBMR userBMR1 = getUserBMRSample1();
        UserBMR userBMR2 = new UserBMR();
        assertThat(userBMR1).isNotEqualTo(userBMR2);

        userBMR2.setId(userBMR1.getId());
        assertThat(userBMR1).isEqualTo(userBMR2);

        userBMR2 = getUserBMRSample2();
        assertThat(userBMR1).isNotEqualTo(userBMR2);
    }

    @Test
    void userProfileTest() throws Exception {
        UserBMR userBMR = getUserBMRRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userBMR.setUserProfile(userProfileBack);
        assertThat(userBMR.getUserProfile()).isEqualTo(userProfileBack);

        userBMR.userProfile(null);
        assertThat(userBMR.getUserProfile()).isNull();
    }
}
