package rocks.zipcode.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class BodyHeightTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BodyHeight getBodyHeightSample1() {
        return new BodyHeight().id(1L);
    }

    public static BodyHeight getBodyHeightSample2() {
        return new BodyHeight().id(2L);
    }

    public static BodyHeight getBodyHeightRandomSampleGenerator() {
        return new BodyHeight().id(longCount.incrementAndGet());
    }
}
