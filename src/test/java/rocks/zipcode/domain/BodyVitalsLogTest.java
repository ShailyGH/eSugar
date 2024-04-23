package rocks.zipcode.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rocks.zipcode.domain.BodyVitalsLogTestSamples.*;
import static rocks.zipcode.domain.DataSourceTestSamples.*;
import static rocks.zipcode.domain.UserProfileTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import rocks.zipcode.web.rest.TestUtil;

class BodyVitalsLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BodyVitalsLog.class);
        BodyVitalsLog bodyVitalsLog1 = getBodyVitalsLogSample1();
        BodyVitalsLog bodyVitalsLog2 = new BodyVitalsLog();
        assertThat(bodyVitalsLog1).isNotEqualTo(bodyVitalsLog2);

        bodyVitalsLog2.setId(bodyVitalsLog1.getId());
        assertThat(bodyVitalsLog1).isEqualTo(bodyVitalsLog2);

        bodyVitalsLog2 = getBodyVitalsLogSample2();
        assertThat(bodyVitalsLog1).isNotEqualTo(bodyVitalsLog2);
    }

    @Test
    void dataSourceTest() throws Exception {
        BodyVitalsLog bodyVitalsLog = getBodyVitalsLogRandomSampleGenerator();
        DataSource dataSourceBack = getDataSourceRandomSampleGenerator();

        bodyVitalsLog.addDataSource(dataSourceBack);
        assertThat(bodyVitalsLog.getDataSources()).containsOnly(dataSourceBack);
        assertThat(dataSourceBack.getBodyVitalsLog()).isEqualTo(bodyVitalsLog);

        bodyVitalsLog.removeDataSource(dataSourceBack);
        assertThat(bodyVitalsLog.getDataSources()).doesNotContain(dataSourceBack);
        assertThat(dataSourceBack.getBodyVitalsLog()).isNull();

        bodyVitalsLog.dataSources(new HashSet<>(Set.of(dataSourceBack)));
        assertThat(bodyVitalsLog.getDataSources()).containsOnly(dataSourceBack);
        assertThat(dataSourceBack.getBodyVitalsLog()).isEqualTo(bodyVitalsLog);

        bodyVitalsLog.setDataSources(new HashSet<>());
        assertThat(bodyVitalsLog.getDataSources()).doesNotContain(dataSourceBack);
        assertThat(dataSourceBack.getBodyVitalsLog()).isNull();
    }

    @Test
    void userProfileTest() throws Exception {
        BodyVitalsLog bodyVitalsLog = getBodyVitalsLogRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        bodyVitalsLog.setUserProfile(userProfileBack);
        assertThat(bodyVitalsLog.getUserProfile()).isEqualTo(userProfileBack);

        bodyVitalsLog.userProfile(null);
        assertThat(bodyVitalsLog.getUserProfile()).isNull();
    }
}
