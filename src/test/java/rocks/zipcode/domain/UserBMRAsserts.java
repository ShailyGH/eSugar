package rocks.zipcode.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rocks.zipcode.domain.AssertUtils.zonedDataTimeSameInstant;

public class UserBMRAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserBMRAllPropertiesEquals(UserBMR expected, UserBMR actual) {
        assertUserBMRAutoGeneratedPropertiesEquals(expected, actual);
        assertUserBMRAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserBMRAllUpdatablePropertiesEquals(UserBMR expected, UserBMR actual) {
        assertUserBMRUpdatableFieldsEquals(expected, actual);
        assertUserBMRUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserBMRAutoGeneratedPropertiesEquals(UserBMR expected, UserBMR actual) {
        assertThat(expected)
            .as("Verify UserBMR auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserBMRUpdatableFieldsEquals(UserBMR expected, UserBMR actual) {
        assertThat(expected)
            .as("Verify UserBMR relevant properties")
            .satisfies(e -> assertThat(e.getIdVersion()).as("check idVersion").isEqualTo(actual.getIdVersion()))
            .satisfies(e -> assertThat(e.getBmr()).as("check bmr").isEqualTo(actual.getBmr()))
            .satisfies(
                e ->
                    assertThat(e.getDtCreated())
                        .as("check dtCreated")
                        .usingComparator(zonedDataTimeSameInstant)
                        .isEqualTo(actual.getDtCreated())
            )
            .satisfies(
                e ->
                    assertThat(e.getDtModified())
                        .as("check dtModified")
                        .usingComparator(zonedDataTimeSameInstant)
                        .isEqualTo(actual.getDtModified())
            );
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserBMRUpdatableRelationshipsEquals(UserBMR expected, UserBMR actual) {
        assertThat(expected)
            .as("Verify UserBMR relationships")
            .satisfies(e -> assertThat(e.getUserProfile()).as("check userProfile").isEqualTo(actual.getUserProfile()));
    }
}
