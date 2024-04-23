package rocks.zipcode.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UserBMRTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static UserBMR getUserBMRSample1() {
        return new UserBMR().id(1L).idVersion(1);
    }

    public static UserBMR getUserBMRSample2() {
        return new UserBMR().id(2L).idVersion(2);
    }

    public static UserBMR getUserBMRRandomSampleGenerator() {
        return new UserBMR().id(longCount.incrementAndGet()).idVersion(intCount.incrementAndGet());
    }
}
