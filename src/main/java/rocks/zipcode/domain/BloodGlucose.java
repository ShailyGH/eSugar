package rocks.zipcode.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BloodGlucose.
 */
@Entity
@Table(name = "blood_glucose")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BloodGlucose implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "measurement")
    private Float measurement;

    @Column(name = "measurement_content")
    private String measurementContent;

    @Column(name = "measurement_type")
    private String measurementType;

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

    public BloodGlucose id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getMeasurement() {
        return this.measurement;
    }

    public BloodGlucose measurement(Float measurement) {
        this.setMeasurement(measurement);
        return this;
    }

    public void setMeasurement(Float measurement) {
        this.measurement = measurement;
    }

    public String getMeasurementContent() {
        return this.measurementContent;
    }

    public BloodGlucose measurementContent(String measurementContent) {
        this.setMeasurementContent(measurementContent);
        return this;
    }

    public void setMeasurementContent(String measurementContent) {
        this.measurementContent = measurementContent;
    }

    public String getMeasurementType() {
        return this.measurementType;
    }

    public BloodGlucose measurementType(String measurementType) {
        this.setMeasurementType(measurementType);
        return this;
    }

    public void setMeasurementType(String measurementType) {
        this.measurementType = measurementType;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public BloodGlucose userProfile(UserProfile userProfile) {
        this.setUserProfile(userProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BloodGlucose)) {
            return false;
        }
        return getId() != null && getId().equals(((BloodGlucose) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BloodGlucose{" +
            "id=" + getId() +
            ", measurement=" + getMeasurement() +
            ", measurementContent='" + getMeasurementContent() + "'" +
            ", measurementType='" + getMeasurementType() + "'" +
            "}";
    }
}
