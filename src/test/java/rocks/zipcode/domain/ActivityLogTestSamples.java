package rocks.zipcode.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ActivityLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ActivityLog getActivityLogSample1() {
        return new ActivityLog().id(1L).stepsCount(1);
    }

    public static ActivityLog getActivityLogSample2() {
        return new ActivityLog().id(2L).stepsCount(2);
    }

    public static ActivityLog getActivityLogRandomSampleGenerator() {
        return new ActivityLog().id(longCount.incrementAndGet()).stepsCount(intCount.incrementAndGet());
    }
}
