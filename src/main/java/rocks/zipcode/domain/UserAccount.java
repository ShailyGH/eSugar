package rocks.zipcode.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserAccount.
 */
@Entity
@Table(name = "user_account")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "login_name")
    private String loginName;

    @Column(name = "password")
    private String password;

    @Column(name = "street_address")
    private String streetAddress;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "zipcode")
    private String zipcode;

    @Column(name = "phone_number")
    private Integer phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "is_active")
    private String isActive;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userAccount")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Set<UserProfile> userProfiles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getId() {
        return this.id;
    }

    public UserAccount id(Integer id) {
        this.setId(id);
        return this;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLoginName() {
        return this.loginName;
    }

    public UserAccount loginName(String loginName) {
        this.setLoginName(loginName);
        return this;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return this.password;
    }

    public UserAccount password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStreetAddress() {
        return this.streetAddress;
    }

    public UserAccount streetAddress(String streetAddress) {
        this.setStreetAddress(streetAddress);
        return this;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return this.city;
    }

    public UserAccount city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public UserAccount state(String state) {
        this.setState(state);
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return this.country;
    }

    public UserAccount country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipcode() {
        return this.zipcode;
    }

    public UserAccount zipcode(String zipcode) {
        this.setZipcode(zipcode);
        return this;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Integer getPhoneNumber() {
        return this.phoneNumber;
    }

    public UserAccount phoneNumber(Integer phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public UserAccount email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsActive() {
        return this.isActive;
    }

    public UserAccount isActive(String isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public Set<UserProfile> getUserProfiles() {
        return this.userProfiles;
    }

    public void setUserProfiles(Set<UserProfile> userProfiles) {
        if (this.userProfiles != null) {
            this.userProfiles.forEach(i -> i.setUserAccount(null));
        }
        if (userProfiles != null) {
            userProfiles.forEach(i -> i.setUserAccount(this));
        }
        this.userProfiles = userProfiles;
    }

    public UserAccount userProfiles(Set<UserProfile> userProfiles) {
        this.setUserProfiles(userProfiles);
        return this;
    }

    public UserAccount addUserProfile(UserProfile userProfile) {
        this.userProfiles.add(userProfile);
        userProfile.setUserAccount(this);
        return this;
    }

    public UserAccount removeUserProfile(UserProfile userProfile) {
        this.userProfiles.remove(userProfile);
        userProfile.setUserAccount(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAccount)) {
            return false;
        }
        return getId() != null && getId().equals(((UserAccount) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAccount{" +
            "id=" + getId() +
            ", loginName='" + getLoginName() + "'" +
            ", password='" + getPassword() + "'" +
            ", streetAddress='" + getStreetAddress() + "'" +
            ", city='" + getCity() + "'" +
            ", state='" + getState() + "'" +
            ", country='" + getCountry() + "'" +
            ", zipcode='" + getZipcode() + "'" +
            ", phoneNumber=" + getPhoneNumber() +
            ", email='" + getEmail() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
