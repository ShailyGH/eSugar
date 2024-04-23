package rocks.zipcode.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DataSourceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DataSource getDataSourceSample1() {
        return new DataSource().id(1L).sourceName("sourceName1");
    }

    public static DataSource getDataSourceSample2() {
        return new DataSource().id(2L).sourceName("sourceName2");
    }

    public static DataSource getDataSourceRandomSampleGenerator() {
        return new DataSource().id(longCount.incrementAndGet()).sourceName(UUID.randomUUID().toString());
    }
}
