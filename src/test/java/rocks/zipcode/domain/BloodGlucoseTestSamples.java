package rocks.zipcode.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BloodGlucoseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BloodGlucose getBloodGlucoseSample1() {
        return new BloodGlucose().id(1L).measurementContent("measurementContent1").measurementType("measurementType1");
    }

    public static BloodGlucose getBloodGlucoseSample2() {
        return new BloodGlucose().id(2L).measurementContent("measurementContent2").measurementType("measurementType2");
    }

    public static BloodGlucose getBloodGlucoseRandomSampleGenerator() {
        return new BloodGlucose()
            .id(longCount.incrementAndGet())
            .measurementContent(UUID.randomUUID().toString())
            .measurementType(UUID.randomUUID().toString());
    }
}
