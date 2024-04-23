package rocks.zipcode.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import rocks.zipcode.domain.DataSource;
import rocks.zipcode.repository.DataSourceRepository;
import rocks.zipcode.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link rocks.zipcode.domain.DataSource}.
 */
@RestController
@RequestMapping("/api/data-sources")
@Transactional
public class DataSourceResource {

    private final Logger log = LoggerFactory.getLogger(DataSourceResource.class);

    private static final String ENTITY_NAME = "dataSource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DataSourceRepository dataSourceRepository;

    public DataSourceResource(DataSourceRepository dataSourceRepository) {
        this.dataSourceRepository = dataSourceRepository;
    }

    /**
     * {@code POST  /data-sources} : Create a new dataSource.
     *
     * @param dataSource the dataSource to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dataSource, or with status {@code 400 (Bad Request)} if the dataSource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DataSource> createDataSource(@RequestBody DataSource dataSource) throws URISyntaxException {
        log.debug("REST request to save DataSource : {}", dataSource);
        if (dataSource.getId() != null) {
            throw new BadRequestAlertException("A new dataSource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        dataSource = dataSourceRepository.save(dataSource);
        return ResponseEntity.created(new URI("/api/data-sources/" + dataSource.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, dataSource.getId().toString()))
            .body(dataSource);
    }

    /**
     * {@code PUT  /data-sources/:id} : Updates an existing dataSource.
     *
     * @param id the id of the dataSource to save.
     * @param dataSource the dataSource to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataSource,
     * or with status {@code 400 (Bad Request)} if the dataSource is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dataSource couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DataSource> updateDataSource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DataSource dataSource
    ) throws URISyntaxException {
        log.debug("REST request to update DataSource : {}, {}", id, dataSource);
        if (dataSource.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dataSource.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dataSourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        dataSource = dataSourceRepository.save(dataSource);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dataSource.getId().toString()))
            .body(dataSource);
    }

    /**
     * {@code PATCH  /data-sources/:id} : Partial updates given fields of an existing dataSource, field will ignore if it is null
     *
     * @param id the id of the dataSource to save.
     * @param dataSource the dataSource to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataSource,
     * or with status {@code 400 (Bad Request)} if the dataSource is not valid,
     * or with status {@code 404 (Not Found)} if the dataSource is not found,
     * or with status {@code 500 (Internal Server Error)} if the dataSource couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DataSource> partialUpdateDataSource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DataSource dataSource
    ) throws URISyntaxException {
        log.debug("REST request to partial update DataSource partially : {}, {}", id, dataSource);
        if (dataSource.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dataSource.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dataSourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DataSource> result = dataSourceRepository
            .findById(dataSource.getId())
            .map(existingDataSource -> {
                if (dataSource.getSourceName() != null) {
                    existingDataSource.setSourceName(dataSource.getSourceName());
                }

                return existingDataSource;
            })
            .map(dataSourceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dataSource.getId().toString())
        );
    }

    /**
     * {@code GET  /data-sources} : get all the dataSources.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dataSources in body.
     */
    @GetMapping("")
    public List<DataSource> getAllDataSources() {
        log.debug("REST request to get all DataSources");
        return dataSourceRepository.findAll();
    }

    /**
     * {@code GET  /data-sources/:id} : get the "id" dataSource.
     *
     * @param id the id of the dataSource to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dataSource, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DataSource> getDataSource(@PathVariable("id") Long id) {
        log.debug("REST request to get DataSource : {}", id);
        Optional<DataSource> dataSource = dataSourceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dataSource);
    }

    /**
     * {@code DELETE  /data-sources/:id} : delete the "id" dataSource.
     *
     * @param id the id of the dataSource to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDataSource(@PathVariable("id") Long id) {
        log.debug("REST request to delete DataSource : {}", id);
        dataSourceRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
