package rocks.zipcode.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserBMR.
 */
@Entity
@Table(name = "user_bmr")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserBMR implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_version")
    private Integer idVersion;

    @Column(name = "bmr")
    private Float bmr;

    @Column(name = "dt_created")
    private ZonedDateTime dtCreated;

    @Column(name = "dt_modified")
    private ZonedDateTime dtModified;

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

    public UserBMR id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdVersion() {
        return this.idVersion;
    }

    public UserBMR idVersion(Integer idVersion) {
        this.setIdVersion(idVersion);
        return this;
    }

    public void setIdVersion(Integer idVersion) {
        this.idVersion = idVersion;
    }

    public Float getBmr() {
        return this.bmr;
    }

    public UserBMR bmr(Float bmr) {
        this.setBmr(bmr);
        return this;
    }

    public void setBmr(Float bmr) {
        this.bmr = bmr;
    }

    public ZonedDateTime getDtCreated() {
        return this.dtCreated;
    }

    public UserBMR dtCreated(ZonedDateTime dtCreated) {
        this.setDtCreated(dtCreated);
        return this;
    }

    public void setDtCreated(ZonedDateTime dtCreated) {
        this.dtCreated = dtCreated;
    }

    public ZonedDateTime getDtModified() {
        return this.dtModified;
    }

    public UserBMR dtModified(ZonedDateTime dtModified) {
        this.setDtModified(dtModified);
        return this;
    }

    public void setDtModified(ZonedDateTime dtModified) {
        this.dtModified = dtModified;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public UserBMR userProfile(UserProfile userProfile) {
        this.setUserProfile(userProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserBMR)) {
            return false;
        }
        return getId() != null && getId().equals(((UserBMR) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserBMR{" +
            "id=" + getId() +
            ", idVersion=" + getIdVersion() +
            ", bmr=" + getBmr() +
            ", dtCreated='" + getDtCreated() + "'" +
            ", dtModified='" + getDtModified() + "'" +
            "}";
    }
}
