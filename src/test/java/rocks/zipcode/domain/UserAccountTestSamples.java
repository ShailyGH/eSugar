package rocks.zipcode.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class UserAccountTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static UserAccount getUserAccountSample1() {
        return new UserAccount()
            .id(1)
            .loginName("loginName1")
            .password("password1")
            .streetAddress("streetAddress1")
            .city("city1")
            .state("state1")
            .country("country1")
            .zipcode("zipcode1")
            .phoneNumber(1)
            .email("email1")
            .isActive("isActive1");
    }

    public static UserAccount getUserAccountSample2() {
        return new UserAccount()
            .id(2)
            .loginName("loginName2")
            .password("password2")
            .streetAddress("streetAddress2")
            .city("city2")
            .state("state2")
            .country("country2")
            .zipcode("zipcode2")
            .phoneNumber(2)
            .email("email2")
            .isActive("isActive2");
    }

    public static UserAccount getUserAccountRandomSampleGenerator() {
        return new UserAccount()
            .id(intCount.incrementAndGet())
            .loginName(UUID.randomUUID().toString())
            .password(UUID.randomUUID().toString())
            .streetAddress(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .state(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString())
            .zipcode(UUID.randomUUID().toString())
            .phoneNumber(intCount.incrementAndGet())
            .email(UUID.randomUUID().toString())
            .isActive(UUID.randomUUID().toString());
    }
}
