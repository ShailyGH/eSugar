package rocks.zipcode.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class UserProfileTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static UserProfile getUserProfileSample1() {
        return new UserProfile()
            .id(1)
            .userProfileName("userProfileName1")
            .email("email1")
            .isReportSharingEnabled("isReportSharingEnabled1")
            .isActive("isActive1");
    }

    public static UserProfile getUserProfileSample2() {
        return new UserProfile()
            .id(2)
            .userProfileName("userProfileName2")
            .email("email2")
            .isReportSharingEnabled("isReportSharingEnabled2")
            .isActive("isActive2");
    }

    public static UserProfile getUserProfileRandomSampleGenerator() {
        return new UserProfile()
            .id(intCount.incrementAndGet())
            .userProfileName(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .isReportSharingEnabled(UUID.randomUUID().toString())
            .isActive(UUID.randomUUID().toString());
    }
}
