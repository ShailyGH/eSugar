package rocks.zipcode.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class BodyWeightAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBodyWeightAllPropertiesEquals(BodyWeight expected, BodyWeight actual) {
        assertBodyWeightAutoGeneratedPropertiesEquals(expected, actual);
        assertBodyWeightAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBodyWeightAllUpdatablePropertiesEquals(BodyWeight expected, BodyWeight actual) {
        assertBodyWeightUpdatableFieldsEquals(expected, actual);
        assertBodyWeightUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBodyWeightAutoGeneratedPropertiesEquals(BodyWeight expected, BodyWeight actual) {
        assertThat(expected)
            .as("Verify BodyWeight auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBodyWeightUpdatableFieldsEquals(BodyWeight expected, BodyWeight actual) {
        assertThat(expected)
            .as("Verify BodyWeight relevant properties")
            .satisfies(e -> assertThat(e.getWeight()).as("check weight").isEqualTo(actual.getWeight()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBodyWeightUpdatableRelationshipsEquals(BodyWeight expected, BodyWeight actual) {
        assertThat(expected)
            .as("Verify BodyWeight relationships")
            .satisfies(e -> assertThat(e.getUserProfile()).as("check userProfile").isEqualTo(actual.getUserProfile()));
    }
}
