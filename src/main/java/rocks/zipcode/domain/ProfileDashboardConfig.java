package rocks.zipcode.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProfileDashboardConfig.
 */
@Entity
@Table(name = "profile_dashboard_config")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfileDashboardConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "is_blood_glucose_shown")
    private String isBloodGlucoseShown;

    @Column(name = "is_blood_pressure_shown")
    private String isBloodPressureShown;

    @Column(name = "is_body_composition_shown")
    private String isBodyCompositionShown;

    @Column(name = "is_blood_cholesterol_shown")
    private String isBloodCholesterolShown;

    @Column(name = "is_body_height_shown")
    private String isBodyHeightShown;

    @Column(name = "is_body_weight_shown")
    private String isBodyWeightShown;

    @Column(name = "is_calories_burnt_shown")
    private String isCaloriesBurntShown;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "characteristicData",
            "profileDashboardConfigs",
            "bloodGlucoses",
            "bodyVitalsLogs",
            "userBMRS",
            "activityLogs",
            "bodyWeights",
            "bodyHeights",
            "userAccount",
        },
        allowSetters = true
    )
    private UserProfile userProfile;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProfileDashboardConfig id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsBloodGlucoseShown() {
        return this.isBloodGlucoseShown;
    }

    public ProfileDashboardConfig isBloodGlucoseShown(String isBloodGlucoseShown) {
        this.setIsBloodGlucoseShown(isBloodGlucoseShown);
        return this;
    }

    public void setIsBloodGlucoseShown(String isBloodGlucoseShown) {
        this.isBloodGlucoseShown = isBloodGlucoseShown;
    }

    public String getIsBloodPressureShown() {
        return this.isBloodPressureShown;
    }

    public ProfileDashboardConfig isBloodPressureShown(String isBloodPressureShown) {
        this.setIsBloodPressureShown(isBloodPressureShown);
        return this;
    }

    public void setIsBloodPressureShown(String isBloodPressureShown) {
        this.isBloodPressureShown = isBloodPressureShown;
    }

    public String getIsBodyCompositionShown() {
        return this.isBodyCompositionShown;
    }

    public ProfileDashboardConfig isBodyCompositionShown(String isBodyCompositionShown) {
        this.setIsBodyCompositionShown(isBodyCompositionShown);
        return this;
    }

    public void setIsBodyCompositionShown(String isBodyCompositionShown) {
        this.isBodyCompositionShown = isBodyCompositionShown;
    }

    public String getIsBloodCholesterolShown() {
        return this.isBloodCholesterolShown;
    }

    public ProfileDashboardConfig isBloodCholesterolShown(String isBloodCholesterolShown) {
        this.setIsBloodCholesterolShown(isBloodCholesterolShown);
        return this;
    }

    public void setIsBloodCholesterolShown(String isBloodCholesterolShown) {
        this.isBloodCholesterolShown = isBloodCholesterolShown;
    }

    public String getIsBodyHeightShown() {
        return this.isBodyHeightShown;
    }

    public ProfileDashboardConfig isBodyHeightShown(String isBodyHeightShown) {
        this.setIsBodyHeightShown(isBodyHeightShown);
        return this;
    }

    public void setIsBodyHeightShown(String isBodyHeightShown) {
        this.isBodyHeightShown = isBodyHeightShown;
    }

    public String getIsBodyWeightShown() {
        return this.isBodyWeightShown;
    }

    public ProfileDashboardConfig isBodyWeightShown(String isBodyWeightShown) {
        this.setIsBodyWeightShown(isBodyWeightShown);
        return this;
    }

    public void setIsBodyWeightShown(String isBodyWeightShown) {
        this.isBodyWeightShown = isBodyWeightShown;
    }

    public String getIsCaloriesBurntShown() {
        return this.isCaloriesBurntShown;
    }

    public ProfileDashboardConfig isCaloriesBurntShown(String isCaloriesBurntShown) {
        this.setIsCaloriesBurntShown(isCaloriesBurntShown);
        return this;
    }

    public void setIsCaloriesBurntShown(String isCaloriesBurntShown) {
        this.isCaloriesBurntShown = isCaloriesBurntShown;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public ProfileDashboardConfig userProfile(UserProfile userProfile) {
        this.setUserProfile(userProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfileDashboardConfig)) {
            return false;
        }
        return getId() != null && getId().equals(((ProfileDashboardConfig) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfileDashboardConfig{" +
            "id=" + getId() +
            ", isBloodGlucoseShown='" + getIsBloodGlucoseShown() + "'" +
            ", isBloodPressureShown='" + getIsBloodPressureShown() + "'" +
            ", isBodyCompositionShown='" + getIsBodyCompositionShown() + "'" +
            ", isBloodCholesterolShown='" + getIsBloodCholesterolShown() + "'" +
            ", isBodyHeightShown='" + getIsBodyHeightShown() + "'" +
            ", isBodyWeightShown='" + getIsBodyWeightShown() + "'" +
            ", isCaloriesBurntShown='" + getIsCaloriesBurntShown() + "'" +
            "}";
    }
}
