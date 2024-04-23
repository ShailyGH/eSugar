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
 * A BodyVitalsLog.
 */
@Entity
@Table(name = "body_vitals_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BodyVitalsLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "dt_created")
    private ZonedDateTime dtCreated;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bodyVitalsLog")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "bodyVitalsLog" }, allowSetters = true)
    private Set<DataSource> dataSources = new HashSet<>();

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

    public BodyVitalsLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDtCreated() {
        return this.dtCreated;
    }

    public BodyVitalsLog dtCreated(ZonedDateTime dtCreated) {
        this.setDtCreated(dtCreated);
        return this;
    }

    public void setDtCreated(ZonedDateTime dtCreated) {
        this.dtCreated = dtCreated;
    }

    public Set<DataSource> getDataSources() {
        return this.dataSources;
    }

    public void setDataSources(Set<DataSource> dataSources) {
        if (this.dataSources != null) {
            this.dataSources.forEach(i -> i.setBodyVitalsLog(null));
        }
        if (dataSources != null) {
            dataSources.forEach(i -> i.setBodyVitalsLog(this));
        }
        this.dataSources = dataSources;
    }

    public BodyVitalsLog dataSources(Set<DataSource> dataSources) {
        this.setDataSources(dataSources);
        return this;
    }

    public BodyVitalsLog addDataSource(DataSource dataSource) {
        this.dataSources.add(dataSource);
        dataSource.setBodyVitalsLog(this);
        return this;
    }

    public BodyVitalsLog removeDataSource(DataSource dataSource) {
        this.dataSources.remove(dataSource);
        dataSource.setBodyVitalsLog(null);
        return this;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public BodyVitalsLog userProfile(UserProfile userProfile) {
        this.setUserProfile(userProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BodyVitalsLog)) {
            return false;
        }
        return getId() != null && getId().equals(((BodyVitalsLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BodyVitalsLog{" +
            "id=" + getId() +
            ", dtCreated='" + getDtCreated() + "'" +
            "}";
    }
}
