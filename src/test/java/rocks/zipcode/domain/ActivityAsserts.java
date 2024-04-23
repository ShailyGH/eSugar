package rocks.zipcode.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ActivityAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertActivityAllPropertiesEquals(Activity expected, Activity actual) {
        assertActivityAutoGeneratedPropertiesEquals(expected, actual);
        assertActivityAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertActivityAllUpdatablePropertiesEquals(Activity expected, Activity actual) {
        assertActivityUpdatableFieldsEquals(expected, actual);
        assertActivityUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertActivityAutoGeneratedPropertiesEquals(Activity expected, Activity actual) {
        assertThat(expected)
            .as("Verify Activity auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertActivityUpdatableFieldsEquals(Activity expected, Activity actual) {
        assertThat(expected)
            .as("Verify Activity relevant properties")
            .satisfies(e -> assertThat(e.getActivityName()).as("check activityName").isEqualTo(actual.getActivityName()))
            .satisfies(e -> assertThat(e.getActivityMultiplier()).as("check activityMultiplier").isEqualTo(actual.getActivityMultiplier()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertActivityUpdatableRelationshipsEquals(Activity expected, Activity actual) {
        assertThat(expected)
            .as("Verify Activity relationships")
            .satisfies(e -> assertThat(e.getActivityLog()).as("check activityLog").isEqualTo(actual.getActivityLog()));
    }
}