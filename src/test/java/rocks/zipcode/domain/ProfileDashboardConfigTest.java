package rocks.zipcode.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rocks.zipcode.domain.ProfileDashboardConfigTestSamples.*;
import static rocks.zipcode.domain.UserProfileTestSamples.*;

import org.junit.jupiter.api.Test;
import rocks.zipcode.web.rest.TestUtil;

class ProfileDashboardConfigTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfileDashboardConfig.class);
        ProfileDashboardConfig profileDashboardConfig1 = getProfileDashboardConfigSample1();
        ProfileDashboardConfig profileDashboardConfig2 = new ProfileDashboardConfig();
        assertThat(profileDashboardConfig1).isNotEqualTo(profileDashboardConfig2);

        profileDashboardConfig2.setId(profileDashboardConfig1.getId());
        assertThat(profileDashboardConfig1).isEqualTo(profileDashboardConfig2);

        profileDashboardConfig2 = getProfileDashboardConfigSample2();
        assertThat(profileDashboardConfig1).isNotEqualTo(profileDashboardConfig2);
    }

    @Test
    void userProfileTest() throws Exception {
        ProfileDashboardConfig profileDashboardConfig = getProfileDashboardConfigRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        profileDashboardConfig.setUserProfile(userProfileBack);
        assertThat(profileDashboardConfig.getUserProfile()).isEqualTo(userProfileBack);

        profileDashboardConfig.userProfile(null);
        assertThat(profileDashboardConfig.getUserProfile()).isNull();
    }
}
