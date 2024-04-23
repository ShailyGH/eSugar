package rocks.zipcode.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserProfile.
 */
@Entity
@Table(name = "user_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_profile_name")
    private String userProfileName;

    @Column(name = "email")
    private String email;

    @Column(name = "is_report_sharing_enabled")
    private String isReportSharingEnabled;

    @Column(name = "is_active")
    private String isActive;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userProfile")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "userProfile" }, allowSetters = true)
    private Set<CharacteristicData> characteristicData = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userProfile")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "userProfile" }, allowSetters = true)
    private Set<ProfileDashboardConfig> profileDashboardConfigs = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userProfile")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "userProfile" }, allowSetters = true)
    private Set<BloodGlucose> bloodGlucoses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userProfile")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "dataSources", "userProfile" }, allowSetters = true)
    private Set<BodyVitalsLog> bodyVitalsLogs = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userProfile")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "userProfile" }, allowSetters = true)
    private Set<UserBMR> userBMRS = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userProfile")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "activities", "userProfile" }, allowSetters = true)
    private Set<ActivityLog> activityLogs = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userProfile")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "userProfile" }, allowSetters = true)
    private Set<BodyWeight> bodyWeights = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userProfile")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "userProfile" }, allowSetters = true)
    private Set<BodyHeight> bodyHeights = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "userProfiles" }, allowSetters = true)
    private UserAccount userAccount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getId() {
        return this.id;
    }

    public UserProfile id(Integer id) {
        this.setId(id);
        return this;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserProfileName() {
        return this.userProfileName;
    }

    public UserProfile userProfileName(String userProfileName) {
        this.setUserProfileName(userProfileName);
        return this;
    }

    public void setUserProfileName(String userProfileName) {
        this.userProfileName = userProfileName;
    }

    public String getEmail() {
        return this.email;
    }

    public UserProfile email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsReportSharingEnabled() {
        return this.isReportSharingEnabled;
    }

    public UserProfile isReportSharingEnabled(String isReportSharingEnabled) {
        this.setIsReportSharingEnabled(isReportSharingEnabled);
        return this;
    }

    public void setIsReportSharingEnabled(String isReportSharingEnabled) {
        this.isReportSharingEnabled = isReportSharingEnabled;
    }

    public String getIsActive() {
        return this.isActive;
    }

    public UserProfile isActive(String isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public Set<CharacteristicData> getCharacteristicData() {
        return this.characteristicData;
    }

    public void setCharacteristicData(Set<CharacteristicData> characteristicData) {
        if (this.characteristicData != null) {
            this.characteristicData.forEach(i -> i.setUserProfile(null));
        }
        if (characteristicData != null) {
            characteristicData.forEach(i -> i.setUserProfile(this));
        }
        this.characteristicData = characteristicData;
    }

    public UserProfile characteristicData(Set<CharacteristicData> characteristicData) {
        this.setCharacteristicData(characteristicData);
        return this;
    }

    public UserProfile addCharacteristicData(CharacteristicData characteristicData) {
        this.characteristicData.add(characteristicData);
        characteristicData.setUserProfile(this);
        return this;
    }

    public UserProfile removeCharacteristicData(CharacteristicData characteristicData) {
        this.characteristicData.remove(characteristicData);
        characteristicData.setUserProfile(null);
        return this;
    }

    public Set<ProfileDashboardConfig> getProfileDashboardConfigs() {
        return this.profileDashboardConfigs;
    }

    public void setProfileDashboardConfigs(Set<ProfileDashboardConfig> profileDashboardConfigs) {
        if (this.profileDashboardConfigs != null) {
            this.profileDashboardConfigs.forEach(i -> i.setUserProfile(null));
        }
        if (profileDashboardConfigs != null) {
            profileDashboardConfigs.forEach(i -> i.setUserProfile(this));
        }
        this.profileDashboardConfigs = profileDashboardConfigs;
    }

    public UserProfile profileDashboardConfigs(Set<ProfileDashboardConfig> profileDashboardConfigs) {
        this.setProfileDashboardConfigs(profileDashboardConfigs);
        return this;
    }

    public UserProfile addProfileDashboardConfig(ProfileDashboardConfig profileDashboardConfig) {
        this.profileDashboardConfigs.add(profileDashboardConfig);
        profileDashboardConfig.setUserProfile(this);
        return this;
    }

    public UserProfile removeProfileDashboardConfig(ProfileDashboardConfig profileDashboardConfig) {
        this.profileDashboardConfigs.remove(profileDashboardConfig);
        profileDashboardConfig.setUserProfile(null);
        return this;
    }

    public Set<BloodGlucose> getBloodGlucoses() {
        return this.bloodGlucoses;
    }

    public void setBloodGlucoses(Set<BloodGlucose> bloodGlucoses) {
        if (this.bloodGlucoses != null) {
            this.bloodGlucoses.forEach(i -> i.setUserProfile(null));
        }
        if (bloodGlucoses != null) {
            bloodGlucoses.forEach(i -> i.setUserProfile(this));
        }
        this.bloodGlucoses = bloodGlucoses;
    }

    public UserProfile bloodGlucoses(Set<BloodGlucose> bloodGlucoses) {
        this.setBloodGlucoses(bloodGlucoses);
        return this;
    }

    public UserProfile addBloodGlucose(BloodGlucose bloodGlucose) {
        this.bloodGlucoses.add(bloodGlucose);
        bloodGlucose.setUserProfile(this);
        return this;
    }

    public UserProfile removeBloodGlucose(BloodGlucose bloodGlucose) {
        this.bloodGlucoses.remove(bloodGlucose);
        bloodGlucose.setUserProfile(null);
        return this;
    }

    public Set<BodyVitalsLog> getBodyVitalsLogs() {
        return this.bodyVitalsLogs;
    }

    public void setBodyVitalsLogs(Set<BodyVitalsLog> bodyVitalsLogs) {
        if (this.bodyVitalsLogs != null) {
            this.bodyVitalsLogs.forEach(i -> i.setUserProfile(null));
        }
        if (bodyVitalsLogs != null) {
            bodyVitalsLogs.forEach(i -> i.setUserProfile(this));
        }
        this.bodyVitalsLogs = bodyVitalsLogs;
    }

    public UserProfile bodyVitalsLogs(Set<BodyVitalsLog> bodyVitalsLogs) {
        this.setBodyVitalsLogs(bodyVitalsLogs);
        return this;
    }

    public UserProfile addBodyVitalsLog(BodyVitalsLog bodyVitalsLog) {
        this.bodyVitalsLogs.add(bodyVitalsLog);
        bodyVitalsLog.setUserProfile(this);
        return this;
    }

    public UserProfile removeBodyVitalsLog(BodyVitalsLog bodyVitalsLog) {
        this.bodyVitalsLogs.remove(bodyVitalsLog);
        bodyVitalsLog.setUserProfile(null);
        return this;
    }

    public Set<UserBMR> getUserBMRS() {
        return this.userBMRS;
    }

    public void setUserBMRS(Set<UserBMR> userBMRS) {
        if (this.userBMRS != null) {
            this.userBMRS.forEach(i -> i.setUserProfile(null));
        }
        if (userBMRS != null) {
            userBMRS.forEach(i -> i.setUserProfile(this));
        }
        this.userBMRS = userBMRS;
    }

    public UserProfile userBMRS(Set<UserBMR> userBMRS) {
        this.setUserBMRS(userBMRS);
        return this;
    }

    public UserProfile addUserBMR(UserBMR userBMR) {
        this.userBMRS.add(userBMR);
        userBMR.setUserProfile(this);
        return this;
    }

    public UserProfile removeUserBMR(UserBMR userBMR) {
        this.userBMRS.remove(userBMR);
        userBMR.setUserProfile(null);
        return this;
    }

    public Set<ActivityLog> getActivityLogs() {
        return this.activityLogs;
    }

    public void setActivityLogs(Set<ActivityLog> activityLogs) {
        if (this.activityLogs != null) {
            this.activityLogs.forEach(i -> i.setUserProfile(null));
        }
        if (activityLogs != null) {
            activityLogs.forEach(i -> i.setUserProfile(this));
        }
        this.activityLogs = activityLogs;
    }

    public UserProfile activityLogs(Set<ActivityLog> activityLogs) {
        this.setActivityLogs(activityLogs);
        return this;
    }

    public UserProfile addActivityLog(ActivityLog activityLog) {
        this.activityLogs.add(activityLog);
        activityLog.setUserProfile(this);
        return this;
    }

    public UserProfile removeActivityLog(ActivityLog activityLog) {
        this.activityLogs.remove(activityLog);
        activityLog.setUserProfile(null);
        return this;
    }

    public Set<BodyWeight> getBodyWeights() {
        return this.bodyWeights;
    }

    public void setBodyWeights(Set<BodyWeight> bodyWeights) {
        if (this.bodyWeights != null) {
            this.bodyWeights.forEach(i -> i.setUserProfile(null));
        }
        if (bodyWeights != null) {
            bodyWeights.forEach(i -> i.setUserProfile(this));
        }
        this.bodyWeights = bodyWeights;
    }

    public UserProfile bodyWeights(Set<BodyWeight> bodyWeights) {
        this.setBodyWeights(bodyWeights);
        return this;
    }

    public UserProfile addBodyWeight(BodyWeight bodyWeight) {
        this.bodyWeights.add(bodyWeight);
        bodyWeight.setUserProfile(this);
        return this;
    }

    public UserProfile removeBodyWeight(BodyWeight bodyWeight) {
        this.bodyWeights.remove(bodyWeight);
        bodyWeight.setUserProfile(null);
        return this;
    }

    public Set<BodyHeight> getBodyHeights() {
        return this.bodyHeights;
    }

    public void setBodyHeights(Set<BodyHeight> bodyHeights) {
        if (this.bodyHeights != null) {
            this.bodyHeights.forEach(i -> i.setUserProfile(null));
        }
        if (bodyHeights != null) {
            bodyHeights.forEach(i -> i.setUserProfile(this));
        }
        this.bodyHeights = bodyHeights;
    }

    public UserProfile bodyHeights(Set<BodyHeight> bodyHeights) {
        this.setBodyHeights(bodyHeights);
        return this;
    }

    public UserProfile addBodyHeight(BodyHeight bodyHeight) {
        this.bodyHeights.add(bodyHeight);
        bodyHeight.setUserProfile(this);
        return this;
    }

    public UserProfile removeBodyHeight(BodyHeight bodyHeight) {
        this.bodyHeights.remove(bodyHeight);
        bodyHeight.setUserProfile(null);
        return this;
    }

    public UserAccount getUserAccount() {
        return this.userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public UserProfile userAccount(UserAccount userAccount) {
        this.setUserAccount(userAccount);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProfile)) {
            return false;
        }
        return getId() != null && getId().equals(((UserProfile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfile{" +
            "id=" + getId() +
            ", userProfileName='" + getUserProfileName() + "'" +
            ", email='" + getEmail() + "'" +
            ", isReportSharingEnabled='" + getIsReportSharingEnabled() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
