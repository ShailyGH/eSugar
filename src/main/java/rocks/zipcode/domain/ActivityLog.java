package rocks.zipcode.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ActivityLog.
 */
@Entity
@Table(name = "activity_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ActivityLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "start_date_time")
    private ZonedDateTime startDateTime;

    @Column(name = "end_date_time")
    private ZonedDateTime endDateTime;

    @Column(name = "distance_covered")
    private Float distanceCovered;

    @Column(name = "steps_count")
    private Integer stepsCount;

    @Column(name = "calories_burnt")
    private Float caloriesBurnt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "activityLog")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "activityLog" }, allowSetters = true)
    private Set<Activity> activities = new HashSet<>();

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

    public ActivityLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartDateTime() {
        return this.startDateTime;
    }

    public ActivityLog startDateTime(ZonedDateTime startDateTime) {
        this.setStartDateTime(startDateTime);
        return this;
    }

    public void setStartDateTime(ZonedDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public ZonedDateTime getEndDateTime() {
        return this.endDateTime;
    }

    public ActivityLog endDateTime(ZonedDateTime endDateTime) {
        this.setEndDateTime(endDateTime);
        return this;
    }

    public void setEndDateTime(ZonedDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Float getDistanceCovered() {
        return this.distanceCovered;
    }

    public ActivityLog distanceCovered(Float distanceCovered) {
        this.setDistanceCovered(distanceCovered);
        return this;
    }

    public void setDistanceCovered(Float distanceCovered) {
        this.distanceCovered = distanceCovered;
    }

    public Integer getStepsCount() {
        return this.stepsCount;
    }

    public ActivityLog stepsCount(Integer stepsCount) {
        this.setStepsCount(stepsCount);
        return this;
    }

    public void setStepsCount(Integer stepsCount) {
        this.stepsCount = stepsCount;
    }

    public Float getCaloriesBurnt() {
        return this.caloriesBurnt;
    }

    public ActivityLog caloriesBurnt(Float caloriesBurnt) {
        this.setCaloriesBurnt(caloriesBurnt);
        return this;
    }

    public void setCaloriesBurnt(Float caloriesBurnt) {
        this.caloriesBurnt = caloriesBurnt;
    }

    public Set<Activity> getActivities() {
        return this.activities;
    }

    public void setActivities(Set<Activity> activities) {
        if (this.activities != null) {
            this.activities.forEach(i -> i.setActivityLog(null));
        }
        if (activities != null) {
            activities.forEach(i -> i.setActivityLog(this));
        }
        this.activities = activities;
    }

    public ActivityLog activities(Set<Activity> activities) {
        this.setActivities(activities);
        return this;
    }

    public ActivityLog addActivity(Activity activity) {
        this.activities.add(activity);
        activity.setActivityLog(this);
        return this;
    }

    public ActivityLog removeActivity(Activity activity) {
        this.activities.remove(activity);
        activity.setActivityLog(null);
        return this;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public ActivityLog userProfile(UserProfile userProfile) {
        this.setUserProfile(userProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActivityLog)) {
            return false;
        }
        return getId() != null && getId().equals(((ActivityLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActivityLog{" +
            "id=" + getId() +
            ", startDateTime='" + getStartDateTime() + "'" +
            ", endDateTime='" + getEndDateTime() + "'" +
            ", distanceCovered=" + getDistanceCovered() +
            ", stepsCount=" + getStepsCount() +
            ", caloriesBurnt=" + getCaloriesBurnt() +
            "}";
    }
}
