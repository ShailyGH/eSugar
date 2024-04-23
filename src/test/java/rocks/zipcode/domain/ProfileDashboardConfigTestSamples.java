package rocks.zipcode.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProfileDashboardConfigTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ProfileDashboardConfig getProfileDashboardConfigSample1() {
        return new ProfileDashboardConfig()
            .id(1L)
            .isBloodGlucoseShown("isBloodGlucoseShown1")
            .isBloodPressureShown("isBloodPressureShown1")
            .isBodyCompositionShown("isBodyCompositionShown1")
            .isBloodCholesterolShown("isBloodCholesterolShown1")
            .isBodyHeightShown("isBodyHeightShown1")
            .isBodyWeightShown("isBodyWeightShown1")
            .isCaloriesBurntShown("isCaloriesBurntShown1");
    }

    public static ProfileDashboardConfig getProfileDashboardConfigSample2() {
        return new ProfileDashboardConfig()
            .id(2L)
            .isBloodGlucoseShown("isBloodGlucoseShown2")
            .isBloodPressureShown("isBloodPressureShown2")
            .isBodyCompositionShown("isBodyCompositionShown2")
            .isBloodCholesterolShown("isBloodCholesterolShown2")
            .isBodyHeightShown("isBodyHeightShown2")
            .isBodyWeightShown("isBodyWeightShown2")
            .isCaloriesBurntShown("isCaloriesBurntShown2");
    }

    public static ProfileDashboardConfig getProfileDashboardConfigRandomSampleGenerator() {
        return new ProfileDashboardConfig()
            .id(longCount.incrementAndGet())
            .isBloodGlucoseShown(UUID.randomUUID().toString())
            .isBloodPressureShown(UUID.randomUUID().toString())
            .isBodyCompositionShown(UUID.randomUUID().toString())
            .isBloodCholesterolShown(UUID.randomUUID().toString())
            .isBodyHeightShown(UUID.randomUUID().toString())
            .isBodyWeightShown(UUID.randomUUID().toString())
            .isCaloriesBurntShown(UUID.randomUUID().toString());
    }
}
