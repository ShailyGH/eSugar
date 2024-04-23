package rocks.zipcode.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rocks.zipcode.domain.BodyVitalsLogTestSamples.*;
import static rocks.zipcode.domain.DataSourceTestSamples.*;

import org.junit.jupiter.api.Test;
import rocks.zipcode.web.rest.TestUtil;

class DataSourceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataSource.class);
        DataSource dataSource1 = getDataSourceSample1();
        DataSource dataSource2 = new DataSource();
        assertThat(dataSource1).isNotEqualTo(dataSource2);

        dataSource2.setId(dataSource1.getId());
        assertThat(dataSource1).isEqualTo(dataSource2);

        dataSource2 = getDataSourceSample2();
        assertThat(dataSource1).isNotEqualTo(dataSource2);
    }

    @Test
    void bodyVitalsLogTest() throws Exception {
        DataSource dataSource = getDataSourceRandomSampleGenerator();
        BodyVitalsLog bodyVitalsLogBack = getBodyVitalsLogRandomSampleGenerator();

        dataSource.setBodyVitalsLog(bodyVitalsLogBack);
        assertThat(dataSource.getBodyVitalsLog()).isEqualTo(bodyVitalsLogBack);

        dataSource.bodyVitalsLog(null);
        assertThat(dataSource.getBodyVitalsLog()).isNull();
    }
}
