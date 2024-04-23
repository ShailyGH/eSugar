package rocks.zipcode.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rocks.zipcode.domain.ActivityLogTestSamples.*;
import static rocks.zipcode.domain.BloodGlucoseTestSamples.*;
import static rocks.zipcode.domain.BodyHeightTestSamples.*;
import static rocks.zipcode.domain.BodyVitalsLogTestSamples.*;
import static rocks.zipcode.domain.BodyWeightTestSamples.*;
import static rocks.zipcode.domain.CharacteristicDataTestSamples.*;
import static rocks.zipcode.domain.ProfileDashboardConfigTestSamples.*;
import static rocks.zipcode.domain.UserAccountTestSamples.*;
import static rocks.zipcode.domain.UserBMRTestSamples.*;
import static rocks.zipcode.domain.UserProfileTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import rocks.zipcode.web.rest.TestUtil;

class UserProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProfile.class);
        UserProfile userProfile1 = getUserProfileSample1();
        UserProfile userProfile2 = new UserProfile();
        assertThat(userProfile1).isNotEqualTo(userProfile2);

        userProfile2.setId(userProfile1.getId());
        assertThat(userProfile1).isEqualTo(userProfile2);

        userProfile2 = getUserProfileSample2();
        assertThat(userProfile1).isNotEqualTo(userProfile2);
    }

    @Test
    void characteristicDataTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        CharacteristicData characteristicDataBack = getCharacteristicDataRandomSampleGenerator();

        userProfile.addCharacteristicData(characteristicDataBack);
        assertThat(userProfile.getCharacteristicData()).containsOnly(characteristicDataBack);
        assertThat(characteristicDataBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.removeCharacteristicData(characteristicDataBack);
        assertThat(userProfile.getCharacteristicData()).doesNotContain(characteristicDataBack);
        assertThat(characteristicDataBack.getUserProfile()).isNull();

        userProfile.characteristicData(new HashSet<>(Set.of(characteristicDataBack)));
        assertThat(userProfile.getCharacteristicData()).containsOnly(characteristicDataBack);
        assertThat(characteristicDataBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.setCharacteristicData(new HashSet<>());
        assertThat(userProfile.getCharacteristicData()).doesNotContain(characteristicDataBack);
        assertThat(characteristicDataBack.getUserProfile()).isNull();
    }

    @Test
    void profileDashboardConfigTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        ProfileDashboardConfig profileDashboardConfigBack = getProfileDashboardConfigRandomSampleGenerator();

        userProfile.addProfileDashboardConfig(profileDashboardConfigBack);
        assertThat(userProfile.getProfileDashboardConfigs()).containsOnly(profileDashboardConfigBack);
        assertThat(profileDashboardConfigBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.removeProfileDashboardConfig(profileDashboardConfigBack);
        assertThat(userProfile.getProfileDashboardConfigs()).doesNotContain(profileDashboardConfigBack);
        assertThat(profileDashboardConfigBack.getUserProfile()).isNull();

        userProfile.profileDashboardConfigs(new HashSet<>(Set.of(profileDashboardConfigBack)));
        assertThat(userProfile.getProfileDashboardConfigs()).containsOnly(profileDashboardConfigBack);
        assertThat(profileDashboardConfigBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.setProfileDashboardConfigs(new HashSet<>());
        assertThat(userProfile.getProfileDashboardConfigs()).doesNotContain(profileDashboardConfigBack);
        assertThat(profileDashboardConfigBack.getUserProfile()).isNull();
    }

    @Test
    void bloodGlucoseTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        BloodGlucose bloodGlucoseBack = getBloodGlucoseRandomSampleGenerator();

        userProfile.addBloodGlucose(bloodGlucoseBack);
        assertThat(userProfile.getBloodGlucoses()).containsOnly(bloodGlucoseBack);
        assertThat(bloodGlucoseBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.removeBloodGlucose(bloodGlucoseBack);
        assertThat(userProfile.getBloodGlucoses()).doesNotContain(bloodGlucoseBack);
        assertThat(bloodGlucoseBack.getUserProfile()).isNull();

        userProfile.bloodGlucoses(new HashSet<>(Set.of(bloodGlucoseBack)));
        assertThat(userProfile.getBloodGlucoses()).containsOnly(bloodGlucoseBack);
        assertThat(bloodGlucoseBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.setBloodGlucoses(new HashSet<>());
        assertThat(userProfile.getBloodGlucoses()).doesNotContain(bloodGlucoseBack);
        assertThat(bloodGlucoseBack.getUserProfile()).isNull();
    }

    @Test
    void bodyVitalsLogTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        BodyVitalsLog bodyVitalsLogBack = getBodyVitalsLogRandomSampleGenerator();

        userProfile.addBodyVitalsLog(bodyVitalsLogBack);
        assertThat(userProfile.getBodyVitalsLogs()).containsOnly(bodyVitalsLogBack);
        assertThat(bodyVitalsLogBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.removeBodyVitalsLog(bodyVitalsLogBack);
        assertThat(userProfile.getBodyVitalsLogs()).doesNotContain(bodyVitalsLogBack);
        assertThat(bodyVitalsLogBack.getUserProfile()).isNull();

        userProfile.bodyVitalsLogs(new HashSet<>(Set.of(bodyVitalsLogBack)));
        assertThat(userProfile.getBodyVitalsLogs()).containsOnly(bodyVitalsLogBack);
        assertThat(bodyVitalsLogBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.setBodyVitalsLogs(new HashSet<>());
        assertThat(userProfile.getBodyVitalsLogs()).doesNotContain(bodyVitalsLogBack);
        assertThat(bodyVitalsLogBack.getUserProfile()).isNull();
    }

    @Test
    void userBMRTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserBMR userBMRBack = getUserBMRRandomSampleGenerator();

        userProfile.addUserBMR(userBMRBack);
        assertThat(userProfile.getUserBMRS()).containsOnly(userBMRBack);
        assertThat(userBMRBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.removeUserBMR(userBMRBack);
        assertThat(userProfile.getUserBMRS()).doesNotContain(userBMRBack);
        assertThat(userBMRBack.getUserProfile()).isNull();

        userProfile.userBMRS(new HashSet<>(Set.of(userBMRBack)));
        assertThat(userProfile.getUserBMRS()).containsOnly(userBMRBack);
        assertThat(userBMRBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.setUserBMRS(new HashSet<>());
        assertThat(userProfile.getUserBMRS()).doesNotContain(userBMRBack);
        assertThat(userBMRBack.getUserProfile()).isNull();
    }

    @Test
    void activityLogTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        ActivityLog activityLogBack = getActivityLogRandomSampleGenerator();

        userProfile.addActivityLog(activityLogBack);
        assertThat(userProfile.getActivityLogs()).containsOnly(activityLogBack);
        assertThat(activityLogBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.removeActivityLog(activityLogBack);
        assertThat(userProfile.getActivityLogs()).doesNotContain(activityLogBack);
        assertThat(activityLogBack.getUserProfile()).isNull();

        userProfile.activityLogs(new HashSet<>(Set.of(activityLogBack)));
        assertThat(userProfile.getActivityLogs()).containsOnly(activityLogBack);
        assertThat(activityLogBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.setActivityLogs(new HashSet<>());
        assertThat(userProfile.getActivityLogs()).doesNotContain(activityLogBack);
        assertThat(activityLogBack.getUserProfile()).isNull();
    }

    @Test
    void bodyWeightTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        BodyWeight bodyWeightBack = getBodyWeightRandomSampleGenerator();

        userProfile.addBodyWeight(bodyWeightBack);
        assertThat(userProfile.getBodyWeights()).containsOnly(bodyWeightBack);
        assertThat(bodyWeightBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.removeBodyWeight(bodyWeightBack);
        assertThat(userProfile.getBodyWeights()).doesNotContain(bodyWeightBack);
        assertThat(bodyWeightBack.getUserProfile()).isNull();

        userProfile.bodyWeights(new HashSet<>(Set.of(bodyWeightBack)));
        assertThat(userProfile.getBodyWeights()).containsOnly(bodyWeightBack);
        assertThat(bodyWeightBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.setBodyWeights(new HashSet<>());
        assertThat(userProfile.getBodyWeights()).doesNotContain(bodyWeightBack);
        assertThat(bodyWeightBack.getUserProfile()).isNull();
    }

    @Test
    void bodyHeightTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        BodyHeight bodyHeightBack = getBodyHeightRandomSampleGenerator();

        userProfile.addBodyHeight(bodyHeightBack);
        assertThat(userProfile.getBodyHeights()).containsOnly(bodyHeightBack);
        assertThat(bodyHeightBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.removeBodyHeight(bodyHeightBack);
        assertThat(userProfile.getBodyHeights()).doesNotContain(bodyHeightBack);
        assertThat(bodyHeightBack.getUserProfile()).isNull();

        userProfile.bodyHeights(new HashSet<>(Set.of(bodyHeightBack)));
        assertThat(userProfile.getBodyHeights()).containsOnly(bodyHeightBack);
        assertThat(bodyHeightBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.setBodyHeights(new HashSet<>());
        assertThat(userProfile.getBodyHeights()).doesNotContain(bodyHeightBack);
        assertThat(bodyHeightBack.getUserProfile()).isNull();
    }

    @Test
    void userAccountTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserAccount userAccountBack = getUserAccountRandomSampleGenerator();

        userProfile.setUserAccount(userAccountBack);
        assertThat(userProfile.getUserAccount()).isEqualTo(userAccountBack);

        userProfile.userAccount(null);
        assertThat(userProfile.getUserAccount()).isNull();
    }
}
