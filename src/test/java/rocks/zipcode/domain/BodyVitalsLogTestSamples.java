package rocks.zipcode.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class BodyVitalsLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BodyVitalsLog getBodyVitalsLogSample1() {
        return new BodyVitalsLog().id(1L);
    }

    public static BodyVitalsLog getBodyVitalsLogSample2() {
        return new BodyVitalsLog().id(2L);
    }

    public static BodyVitalsLog getBodyVitalsLogRandomSampleGenerator() {
        return new BodyVitalsLog().id(longCount.incrementAndGet());
    }
}
