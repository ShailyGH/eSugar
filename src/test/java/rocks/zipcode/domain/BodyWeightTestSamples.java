package rocks.zipcode.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class BodyWeightTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BodyWeight getBodyWeightSample1() {
        return new BodyWeight().id(1L);
    }

    public static BodyWeight getBodyWeightSample2() {
        return new BodyWeight().id(2L);
    }

    public static BodyWeight getBodyWeightRandomSampleGenerator() {
        return new BodyWeight().id(longCount.incrementAndGet());
    }
}
