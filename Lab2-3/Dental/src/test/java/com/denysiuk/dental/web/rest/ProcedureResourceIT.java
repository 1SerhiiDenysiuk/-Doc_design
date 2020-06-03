package com.denysiuk.dental.web.rest;

import com.denysiuk.dental.DentalApp;
import com.denysiuk.dental.domain.Procedure;
import com.denysiuk.dental.domain.Treatment;
import com.denysiuk.dental.repository.ProcedureRepository;
import com.denysiuk.dental.service.ProcedureService;
import com.denysiuk.dental.service.dto.ProcedureCriteria;
import com.denysiuk.dental.service.ProcedureQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ProcedureResource} REST controller.
 */
@SpringBootTest(classes = DentalApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProcedureResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_PRICE = 1L;
    private static final Long UPDATED_PRICE = 2L;
    private static final Long SMALLER_PRICE = 1L - 1L;

    @Autowired
    private ProcedureRepository procedureRepository;

    @Autowired
    private ProcedureService procedureService;

    @Autowired
    private ProcedureQueryService procedureQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProcedureMockMvc;

    private Procedure procedure;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Procedure createEntity(EntityManager em) {
        Procedure procedure = new Procedure()
            .name(DEFAULT_NAME)
            .price(DEFAULT_PRICE);
        return procedure;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Procedure createUpdatedEntity(EntityManager em) {
        Procedure procedure = new Procedure()
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE);
        return procedure;
    }

    @BeforeEach
    public void initTest() {
        procedure = createEntity(em);
    }

    @Test
    @Transactional
    public void createProcedure() throws Exception {
        int databaseSizeBeforeCreate = procedureRepository.findAll().size();
        // Create the Procedure
        restProcedureMockMvc.perform(post("/api/procedures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(procedure)))
            .andExpect(status().isCreated());

        // Validate the Procedure in the database
        List<Procedure> procedureList = procedureRepository.findAll();
        assertThat(procedureList).hasSize(databaseSizeBeforeCreate + 1);
        Procedure testProcedure = procedureList.get(procedureList.size() - 1);
        assertThat(testProcedure.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProcedure.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    public void createProcedureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = procedureRepository.findAll().size();

        // Create the Procedure with an existing ID
        procedure.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcedureMockMvc.perform(post("/api/procedures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(procedure)))
            .andExpect(status().isBadRequest());

        // Validate the Procedure in the database
        List<Procedure> procedureList = procedureRepository.findAll();
        assertThat(procedureList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = procedureRepository.findAll().size();
        // set the field null
        procedure.setName(null);

        // Create the Procedure, which fails.


        restProcedureMockMvc.perform(post("/api/procedures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(procedure)))
            .andExpect(status().isBadRequest());

        List<Procedure> procedureList = procedureRepository.findAll();
        assertThat(procedureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProcedures() throws Exception {
        // Initialize the database
        procedureRepository.saveAndFlush(procedure);

        // Get all the procedureList
        restProcedureMockMvc.perform(get("/api/procedures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(procedure.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));
    }
    
    @Test
    @Transactional
    public void getProcedure() throws Exception {
        // Initialize the database
        procedureRepository.saveAndFlush(procedure);

        // Get the procedure
        restProcedureMockMvc.perform(get("/api/procedures/{id}", procedure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(procedure.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()));
    }


    @Test
    @Transactional
    public void getProceduresByIdFiltering() throws Exception {
        // Initialize the database
        procedureRepository.saveAndFlush(procedure);

        Long id = procedure.getId();

        defaultProcedureShouldBeFound("id.equals=" + id);
        defaultProcedureShouldNotBeFound("id.notEquals=" + id);

        defaultProcedureShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProcedureShouldNotBeFound("id.greaterThan=" + id);

        defaultProcedureShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProcedureShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllProceduresByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        procedureRepository.saveAndFlush(procedure);

        // Get all the procedureList where name equals to DEFAULT_NAME
        defaultProcedureShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the procedureList where name equals to UPDATED_NAME
        defaultProcedureShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProceduresByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        procedureRepository.saveAndFlush(procedure);

        // Get all the procedureList where name not equals to DEFAULT_NAME
        defaultProcedureShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the procedureList where name not equals to UPDATED_NAME
        defaultProcedureShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProceduresByNameIsInShouldWork() throws Exception {
        // Initialize the database
        procedureRepository.saveAndFlush(procedure);

        // Get all the procedureList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProcedureShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the procedureList where name equals to UPDATED_NAME
        defaultProcedureShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProceduresByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        procedureRepository.saveAndFlush(procedure);

        // Get all the procedureList where name is not null
        defaultProcedureShouldBeFound("name.specified=true");

        // Get all the procedureList where name is null
        defaultProcedureShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllProceduresByNameContainsSomething() throws Exception {
        // Initialize the database
        procedureRepository.saveAndFlush(procedure);

        // Get all the procedureList where name contains DEFAULT_NAME
        defaultProcedureShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the procedureList where name contains UPDATED_NAME
        defaultProcedureShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProceduresByNameNotContainsSomething() throws Exception {
        // Initialize the database
        procedureRepository.saveAndFlush(procedure);

        // Get all the procedureList where name does not contain DEFAULT_NAME
        defaultProcedureShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the procedureList where name does not contain UPDATED_NAME
        defaultProcedureShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllProceduresByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        procedureRepository.saveAndFlush(procedure);

        // Get all the procedureList where price equals to DEFAULT_PRICE
        defaultProcedureShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the procedureList where price equals to UPDATED_PRICE
        defaultProcedureShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllProceduresByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        procedureRepository.saveAndFlush(procedure);

        // Get all the procedureList where price not equals to DEFAULT_PRICE
        defaultProcedureShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the procedureList where price not equals to UPDATED_PRICE
        defaultProcedureShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllProceduresByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        procedureRepository.saveAndFlush(procedure);

        // Get all the procedureList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultProcedureShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the procedureList where price equals to UPDATED_PRICE
        defaultProcedureShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllProceduresByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        procedureRepository.saveAndFlush(procedure);

        // Get all the procedureList where price is not null
        defaultProcedureShouldBeFound("price.specified=true");

        // Get all the procedureList where price is null
        defaultProcedureShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllProceduresByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        procedureRepository.saveAndFlush(procedure);

        // Get all the procedureList where price is greater than or equal to DEFAULT_PRICE
        defaultProcedureShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the procedureList where price is greater than or equal to UPDATED_PRICE
        defaultProcedureShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllProceduresByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        procedureRepository.saveAndFlush(procedure);

        // Get all the procedureList where price is less than or equal to DEFAULT_PRICE
        defaultProcedureShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the procedureList where price is less than or equal to SMALLER_PRICE
        defaultProcedureShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    public void getAllProceduresByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        procedureRepository.saveAndFlush(procedure);

        // Get all the procedureList where price is less than DEFAULT_PRICE
        defaultProcedureShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the procedureList where price is less than UPDATED_PRICE
        defaultProcedureShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllProceduresByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        procedureRepository.saveAndFlush(procedure);

        // Get all the procedureList where price is greater than DEFAULT_PRICE
        defaultProcedureShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the procedureList where price is greater than SMALLER_PRICE
        defaultProcedureShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }


    @Test
    @Transactional
    public void getAllProceduresByTreatmentsIsEqualToSomething() throws Exception {
        // Initialize the database
        procedureRepository.saveAndFlush(procedure);
        Treatment treatments = TreatmentResourceIT.createEntity(em);
        em.persist(treatments);
        em.flush();
        procedure.setTreatments(treatments);
        procedureRepository.saveAndFlush(procedure);
        Long treatmentsId = treatments.getId();

        // Get all the procedureList where treatments equals to treatmentsId
        defaultProcedureShouldBeFound("treatmentsId.equals=" + treatmentsId);

        // Get all the procedureList where treatments equals to treatmentsId + 1
        defaultProcedureShouldNotBeFound("treatmentsId.equals=" + (treatmentsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProcedureShouldBeFound(String filter) throws Exception {
        restProcedureMockMvc.perform(get("/api/procedures?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(procedure.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));

        // Check, that the count call also returns 1
        restProcedureMockMvc.perform(get("/api/procedures/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProcedureShouldNotBeFound(String filter) throws Exception {
        restProcedureMockMvc.perform(get("/api/procedures?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProcedureMockMvc.perform(get("/api/procedures/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingProcedure() throws Exception {
        // Get the procedure
        restProcedureMockMvc.perform(get("/api/procedures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProcedure() throws Exception {
        // Initialize the database
        procedureService.save(procedure);

        int databaseSizeBeforeUpdate = procedureRepository.findAll().size();

        // Update the procedure
        Procedure updatedProcedure = procedureRepository.findById(procedure.getId()).get();
        // Disconnect from session so that the updates on updatedProcedure are not directly saved in db
        em.detach(updatedProcedure);
        updatedProcedure
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE);

        restProcedureMockMvc.perform(put("/api/procedures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedProcedure)))
            .andExpect(status().isOk());

        // Validate the Procedure in the database
        List<Procedure> procedureList = procedureRepository.findAll();
        assertThat(procedureList).hasSize(databaseSizeBeforeUpdate);
        Procedure testProcedure = procedureList.get(procedureList.size() - 1);
        assertThat(testProcedure.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProcedure.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void updateNonExistingProcedure() throws Exception {
        int databaseSizeBeforeUpdate = procedureRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcedureMockMvc.perform(put("/api/procedures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(procedure)))
            .andExpect(status().isBadRequest());

        // Validate the Procedure in the database
        List<Procedure> procedureList = procedureRepository.findAll();
        assertThat(procedureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProcedure() throws Exception {
        // Initialize the database
        procedureService.save(procedure);

        int databaseSizeBeforeDelete = procedureRepository.findAll().size();

        // Delete the procedure
        restProcedureMockMvc.perform(delete("/api/procedures/{id}", procedure.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Procedure> procedureList = procedureRepository.findAll();
        assertThat(procedureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
