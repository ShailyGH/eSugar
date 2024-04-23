package rocks.zipcode.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CharacteristicDataTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CharacteristicData getCharacteristicDataSample1() {
        return new CharacteristicData().id(1L).gender("gender1").bloodType("bloodType1");
    }

    public static CharacteristicData getCharacteristicDataSample2() {
        return new CharacteristicData().id(2L).gender("gender2").bloodType("bloodType2");
    }

    public static CharacteristicData getCharacteristicDataRandomSampleGenerator() {
        return new CharacteristicData()
            .id(longCount.incrementAndGet())
            .gender(UUID.randomUUID().toString())
            .bloodType(UUID.randomUUID().toString());
    }
}
