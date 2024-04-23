package rocks.zipcode.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Activity.
 */
@Entity
@Table(name = "activity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "activity_name")
    private String activityName;

    @Column(name = "activity_multiplier")
    private Float activityMultiplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "activities", "userProfile" }, allowSetters = true)
    private ActivityLog activityLog;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Activity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivityName() {
        return this.activityName;
    }

    public Activity activityName(String activityName) {
        this.setActivityName(activityName);
        return this;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Float getActivityMultiplier() {
        return this.activityMultiplier;
    }

    public Activity activityMultiplier(Float activityMultiplier) {
        this.setActivityMultiplier(activityMultiplier);
        return this;
    }

    public void setActivityMultiplier(Float activityMultiplier) {
        this.activityMultiplier = activityMultiplier;
    }

    public ActivityLog getActivityLog() {
        return this.activityLog;
    }

    public void setActivityLog(ActivityLog activityLog) {
        this.activityLog = activityLog;
    }

    public Activity activityLog(ActivityLog activityLog) {
        this.setActivityLog(activityLog);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Activity)) {
            return false;
        }
        return getId() != null && getId().equals(((Activity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Activity{" +
            "id=" + getId() +
            ", activityName='" + getActivityName() + "'" +
            ", activityMultiplier=" + getActivityMultiplier() +
            "}";
    }
}
