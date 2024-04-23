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
import rocks.zipcode.domain.UserAccount;
import rocks.zipcode.repository.UserAccountRepository;
import rocks.zipcode.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link rocks.zipcode.domain.UserAccount}.
 */
@RestController
@RequestMapping("/api/user-accounts")
@Transactional
public class UserAccountResource {

    private final Logger log = LoggerFactory.getLogger(UserAccountResource.class);

    private static final String ENTITY_NAME = "userAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserAccountRepository userAccountRepository;

    public UserAccountResource(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    /**
     * {@code POST  /user-accounts} : Create a new userAccount.
     *
     * @param userAccount the userAccount to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userAccount, or with status {@code 400 (Bad Request)} if the userAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserAccount> createUserAccount(@RequestBody UserAccount userAccount) throws URISyntaxException {
        log.debug("REST request to save UserAccount : {}", userAccount);
        if (userAccount.getId() != null) {
            throw new BadRequestAlertException("A new userAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userAccount = userAccountRepository.save(userAccount);
        return ResponseEntity.created(new URI("/api/user-accounts/" + userAccount.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, userAccount.getId().toString()))
            .body(userAccount);
    }

    /**
     * {@code PUT  /user-accounts/:id} : Updates an existing userAccount.
     *
     * @param id the id of the userAccount to save.
     * @param userAccount the userAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAccount,
     * or with status {@code 400 (Bad Request)} if the userAccount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserAccount> updateUserAccount(
        @PathVariable(value = "id", required = false) final Integer id,
        @RequestBody UserAccount userAccount
    ) throws URISyntaxException {
        log.debug("REST request to update UserAccount : {}, {}", id, userAccount);
        if (userAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAccount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userAccount = userAccountRepository.save(userAccount);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userAccount.getId().toString()))
            .body(userAccount);
    }

    /**
     * {@code PATCH  /user-accounts/:id} : Partial updates given fields of an existing userAccount, field will ignore if it is null
     *
     * @param id the id of the userAccount to save.
     * @param userAccount the userAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAccount,
     * or with status {@code 400 (Bad Request)} if the userAccount is not valid,
     * or with status {@code 404 (Not Found)} if the userAccount is not found,
     * or with status {@code 500 (Internal Server Error)} if the userAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserAccount> partialUpdateUserAccount(
        @PathVariable(value = "id", required = false) final Integer id,
        @RequestBody UserAccount userAccount
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserAccount partially : {}, {}", id, userAccount);
        if (userAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAccount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserAccount> result = userAccountRepository
            .findById(userAccount.getId())
            .map(existingUserAccount -> {
                if (userAccount.getLoginName() != null) {
                    existingUserAccount.setLoginName(userAccount.getLoginName());
                }
                if (userAccount.getPassword() != null) {
                    existingUserAccount.setPassword(userAccount.getPassword());
                }
                if (userAccount.getStreetAddress() != null) {
                    existingUserAccount.setStreetAddress(userAccount.getStreetAddress());
                }
                if (userAccount.getCity() != null) {
                    existingUserAccount.setCity(userAccount.getCity());
                }
                if (userAccount.getState() != null) {
                    existingUserAccount.setState(userAccount.getState());
                }
                if (userAccount.getCountry() != null) {
                    existingUserAccount.setCountry(userAccount.getCountry());
                }
                if (userAccount.getZipcode() != null) {
                    existingUserAccount.setZipcode(userAccount.getZipcode());
                }
                if (userAccount.getPhoneNumber() != null) {
                    existingUserAccount.setPhoneNumber(userAccount.getPhoneNumber());
                }
                if (userAccount.getEmail() != null) {
                    existingUserAccount.setEmail(userAccount.getEmail());
                }
                if (userAccount.getIsActive() != null) {
                    existingUserAccount.setIsActive(userAccount.getIsActive());
                }

                return existingUserAccount;
            })
            .map(userAccountRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userAccount.getId().toString())
        );
    }

    /**
     * {@code GET  /user-accounts} : get all the userAccounts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userAccounts in body.
     */
    @GetMapping("")
    public List<UserAccount> getAllUserAccounts() {
        log.debug("REST request to get all UserAccounts");
        return userAccountRepository.findAll();
    }

    /**
     * {@code GET  /user-accounts/:id} : get the "id" userAccount.
     *
     * @param id the id of the userAccount to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userAccount, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserAccount> getUserAccount(@PathVariable("id") Integer id) {
        log.debug("REST request to get UserAccount : {}", id);
        Optional<UserAccount> userAccount = userAccountRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userAccount);
    }

    /**
     * {@code DELETE  /user-accounts/:id} : delete the "id" userAccount.
     *
     * @param id the id of the userAccount to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserAccount(@PathVariable("id") Integer id) {
        log.debug("REST request to delete UserAccount : {}", id);
        userAccountRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
