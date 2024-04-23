package rocks.zipcode.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rocks.zipcode.domain.ActivityLogTestSamples.*;
import static rocks.zipcode.domain.ActivityTestSamples.*;
import static rocks.zipcode.domain.UserProfileTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import rocks.zipcode.web.rest.TestUtil;

class ActivityLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivityLog.class);
        ActivityLog activityLog1 = getActivityLogSample1();
        ActivityLog activityLog2 = new ActivityLog();
        assertThat(activityLog1).isNotEqualTo(activityLog2);

        activityLog2.setId(activityLog1.getId());
        assertThat(activityLog1).isEqualTo(activityLog2);

        activityLog2 = getActivityLogSample2();
        assertThat(activityLog1).isNotEqualTo(activityLog2);
    }

    @Test
    void activityTest() throws Exception {
        ActivityLog activityLog = getActivityLogRandomSampleGenerator();
        Activity activityBack = getActivityRandomSampleGenerator();

        activityLog.addActivity(activityBack);
        assertThat(activityLog.getActivities()).containsOnly(activityBack);
        assertThat(activityBack.getActivityLog()).isEqualTo(activityLog);

        activityLog.removeActivity(activityBack);
        assertThat(activityLog.getActivities()).doesNotContain(activityBack);
        assertThat(activityBack.getActivityLog()).isNull();

        activityLog.activities(new HashSet<>(Set.of(activityBack)));
        assertThat(activityLog.getActivities()).containsOnly(activityBack);
        assertThat(activityBack.getActivityLog()).isEqualTo(activityLog);

        activityLog.setActivities(new HashSet<>());
        assertThat(activityLog.getActivities()).doesNotContain(activityBack);
        assertThat(activityBack.getActivityLog()).isNull();
    }

    @Test
    void userProfileTest() throws Exception {
        ActivityLog activityLog = getActivityLogRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        activityLog.setUserProfile(userProfileBack);
        assertThat(activityLog.getUserProfile()).isEqualTo(userProfileBack);

        activityLog.userProfile(null);
        assertThat(activityLog.getUserProfile()).isNull();
    }
}
