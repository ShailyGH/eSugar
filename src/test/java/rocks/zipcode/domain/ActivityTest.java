package rocks.zipcode.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rocks.zipcode.domain.ActivityLogTestSamples.*;
import static rocks.zipcode.domain.ActivityTestSamples.*;

import org.junit.jupiter.api.Test;
import rocks.zipcode.web.rest.TestUtil;

class ActivityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Activity.class);
        Activity activity1 = getActivitySample1();
        Activity activity2 = new Activity();
        assertThat(activity1).isNotEqualTo(activity2);

        activity2.setId(activity1.getId());
        assertThat(activity1).isEqualTo(activity2);

        activity2 = getActivitySample2();
        assertThat(activity1).isNotEqualTo(activity2);
    }

    @Test
    void activityLogTest() throws Exception {
        Activity activity = getActivityRandomSampleGenerator();
        ActivityLog activityLogBack = getActivityLogRandomSampleGenerator();

        activity.setActivityLog(activityLogBack);
        assertThat(activity.getActivityLog()).isEqualTo(activityLogBack);

        activity.activityLog(null);
        assertThat(activity.getActivityLog()).isNull();
    }
}
