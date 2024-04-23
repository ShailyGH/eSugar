package rocks.zipcode.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DataSource.
 */
@Entity
@Table(name = "data_source")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DataSource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "source_name")
    private String sourceName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "dataSources", "userProfile" }, allowSetters = true)
    private BodyVitalsLog bodyVitalsLog;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DataSource id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceName() {
        return this.sourceName;
    }

    public DataSource sourceName(String sourceName) {
        this.setSourceName(sourceName);
        return this;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public BodyVitalsLog getBodyVitalsLog() {
        return this.bodyVitalsLog;
    }

    public void setBodyVitalsLog(BodyVitalsLog bodyVitalsLog) {
        this.bodyVitalsLog = bodyVitalsLog;
    }

    public DataSource bodyVitalsLog(BodyVitalsLog bodyVitalsLog) {
        this.setBodyVitalsLog(bodyVitalsLog);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataSource)) {
            return false;
        }
        return getId() != null && getId().equals(((DataSource) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DataSource{" +
            "id=" + getId() +
            ", sourceName='" + getSourceName() + "'" +
            "}";
    }
}
