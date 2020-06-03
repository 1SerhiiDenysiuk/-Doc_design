package com.denysiuk.dental.web.rest;

import com.denysiuk.dental.DentalApp;
import com.denysiuk.dental.domain.Treatment;
import com.denysiuk.dental.domain.Procedure;
import com.denysiuk.dental.domain.Patient;
import com.denysiuk.dental.repository.TreatmentRepository;
import com.denysiuk.dental.service.TreatmentService;
import com.denysiuk.dental.service.dto.TreatmentCriteria;
import com.denysiuk.dental.service.TreatmentQueryService;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.denysiuk.dental.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TreatmentResource} REST controller.
 */
@SpringBootTest(classes = DentalApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TreatmentResourceIT {

    private static final Long DEFAULT_PATIENT_ID = 1L;
    private static final Long UPDATED_PATIENT_ID = 2L;
    private static final Long SMALLER_PATIENT_ID = 1L - 1L;

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private TreatmentRepository treatmentRepository;

    @Autowired
    private TreatmentService treatmentService;

    @Autowired
    private TreatmentQueryService treatmentQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTreatmentMockMvc;

    private Treatment treatment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Treatment createEntity(EntityManager em) {
        Treatment treatment = new Treatment()
            .patientID(DEFAULT_PATIENT_ID)
            .date(DEFAULT_DATE);
        return treatment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Treatment createUpdatedEntity(EntityManager em) {
        Treatment treatment = new Treatment()
            .patientID(UPDATED_PATIENT_ID)
            .date(UPDATED_DATE);
        return treatment;
    }

    @BeforeEach
    public void initTest() {
        treatment = createEntity(em);
    }

    @Test
    @Transactional
    public void createTreatment() throws Exception {
        int databaseSizeBeforeCreate = treatmentRepository.findAll().size();
        // Create the Treatment
        restTreatmentMockMvc.perform(post("/api/treatments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(treatment)))
            .andExpect(status().isCreated());

        // Validate the Treatment in the database
        List<Treatment> treatmentList = treatmentRepository.findAll();
        assertThat(treatmentList).hasSize(databaseSizeBeforeCreate + 1);
        Treatment testTreatment = treatmentList.get(treatmentList.size() - 1);
        assertThat(testTreatment.getPatientID()).isEqualTo(DEFAULT_PATIENT_ID);
        assertThat(testTreatment.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createTreatmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = treatmentRepository.findAll().size();

        // Create the Treatment with an existing ID
        treatment.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTreatmentMockMvc.perform(post("/api/treatments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(treatment)))
            .andExpect(status().isBadRequest());

        // Validate the Treatment in the database
        List<Treatment> treatmentList = treatmentRepository.findAll();
        assertThat(treatmentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTreatments() throws Exception {
        // Initialize the database
        treatmentRepository.saveAndFlush(treatment);

        // Get all the treatmentList
        restTreatmentMockMvc.perform(get("/api/treatments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(treatment.getId().intValue())))
            .andExpect(jsonPath("$.[*].patientID").value(hasItem(DEFAULT_PATIENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }
    
    @Test
    @Transactional
    public void getTreatment() throws Exception {
        // Initialize the database
        treatmentRepository.saveAndFlush(treatment);

        // Get the treatment
        restTreatmentMockMvc.perform(get("/api/treatments/{id}", treatment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(treatment.getId().intValue()))
            .andExpect(jsonPath("$.patientID").value(DEFAULT_PATIENT_ID.intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)));
    }


    @Test
    @Transactional
    public void getTreatmentsByIdFiltering() throws Exception {
        // Initialize the database
        treatmentRepository.saveAndFlush(treatment);

        Long id = treatment.getId();

        defaultTreatmentShouldBeFound("id.equals=" + id);
        defaultTreatmentShouldNotBeFound("id.notEquals=" + id);

        defaultTreatmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTreatmentShouldNotBeFound("id.greaterThan=" + id);

        defaultTreatmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTreatmentShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTreatmentsByPatientIDIsEqualToSomething() throws Exception {
        // Initialize the database
        treatmentRepository.saveAndFlush(treatment);

        // Get all the treatmentList where patientID equals to DEFAULT_PATIENT_ID
        defaultTreatmentShouldBeFound("patientID.equals=" + DEFAULT_PATIENT_ID);

        // Get all the treatmentList where patientID equals to UPDATED_PATIENT_ID
        defaultTreatmentShouldNotBeFound("patientID.equals=" + UPDATED_PATIENT_ID);
    }

    @Test
    @Transactional
    public void getAllTreatmentsByPatientIDIsNotEqualToSomething() throws Exception {
        // Initialize the database
        treatmentRepository.saveAndFlush(treatment);

        // Get all the treatmentList where patientID not equals to DEFAULT_PATIENT_ID
        defaultTreatmentShouldNotBeFound("patientID.notEquals=" + DEFAULT_PATIENT_ID);

        // Get all the treatmentList where patientID not equals to UPDATED_PATIENT_ID
        defaultTreatmentShouldBeFound("patientID.notEquals=" + UPDATED_PATIENT_ID);
    }

    @Test
    @Transactional
    public void getAllTreatmentsByPatientIDIsInShouldWork() throws Exception {
        // Initialize the database
        treatmentRepository.saveAndFlush(treatment);

        // Get all the treatmentList where patientID in DEFAULT_PATIENT_ID or UPDATED_PATIENT_ID
        defaultTreatmentShouldBeFound("patientID.in=" + DEFAULT_PATIENT_ID + "," + UPDATED_PATIENT_ID);

        // Get all the treatmentList where patientID equals to UPDATED_PATIENT_ID
        defaultTreatmentShouldNotBeFound("patientID.in=" + UPDATED_PATIENT_ID);
    }

    @Test
    @Transactional
    public void getAllTreatmentsByPatientIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        treatmentRepository.saveAndFlush(treatment);

        // Get all the treatmentList where patientID is not null
        defaultTreatmentShouldBeFound("patientID.specified=true");

        // Get all the treatmentList where patientID is null
        defaultTreatmentShouldNotBeFound("patientID.specified=false");
    }

    @Test
    @Transactional
    public void getAllTreatmentsByPatientIDIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        treatmentRepository.saveAndFlush(treatment);

        // Get all the treatmentList where patientID is greater than or equal to DEFAULT_PATIENT_ID
        defaultTreatmentShouldBeFound("patientID.greaterThanOrEqual=" + DEFAULT_PATIENT_ID);

        // Get all the treatmentList where patientID is greater than or equal to UPDATED_PATIENT_ID
        defaultTreatmentShouldNotBeFound("patientID.greaterThanOrEqual=" + UPDATED_PATIENT_ID);
    }

    @Test
    @Transactional
    public void getAllTreatmentsByPatientIDIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        treatmentRepository.saveAndFlush(treatment);

        // Get all the treatmentList where patientID is less than or equal to DEFAULT_PATIENT_ID
        defaultTreatmentShouldBeFound("patientID.lessThanOrEqual=" + DEFAULT_PATIENT_ID);

        // Get all the treatmentList where patientID is less than or equal to SMALLER_PATIENT_ID
        defaultTreatmentShouldNotBeFound("patientID.lessThanOrEqual=" + SMALLER_PATIENT_ID);
    }

    @Test
    @Transactional
    public void getAllTreatmentsByPatientIDIsLessThanSomething() throws Exception {
        // Initialize the database
        treatmentRepository.saveAndFlush(treatment);

        // Get all the treatmentList where patientID is less than DEFAULT_PATIENT_ID
        defaultTreatmentShouldNotBeFound("patientID.lessThan=" + DEFAULT_PATIENT_ID);

        // Get all the treatmentList where patientID is less than UPDATED_PATIENT_ID
        defaultTreatmentShouldBeFound("patientID.lessThan=" + UPDATED_PATIENT_ID);
    }

    @Test
    @Transactional
    public void getAllTreatmentsByPatientIDIsGreaterThanSomething() throws Exception {
        // Initialize the database
        treatmentRepository.saveAndFlush(treatment);

        // Get all the treatmentList where patientID is greater than DEFAULT_PATIENT_ID
        defaultTreatmentShouldNotBeFound("patientID.greaterThan=" + DEFAULT_PATIENT_ID);

        // Get all the treatmentList where patientID is greater than SMALLER_PATIENT_ID
        defaultTreatmentShouldBeFound("patientID.greaterThan=" + SMALLER_PATIENT_ID);
    }


    @Test
    @Transactional
    public void getAllTreatmentsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        treatmentRepository.saveAndFlush(treatment);

        // Get all the treatmentList where date equals to DEFAULT_DATE
        defaultTreatmentShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the treatmentList where date equals to UPDATED_DATE
        defaultTreatmentShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllTreatmentsByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        treatmentRepository.saveAndFlush(treatment);

        // Get all the treatmentList where date not equals to DEFAULT_DATE
        defaultTreatmentShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the treatmentList where date not equals to UPDATED_DATE
        defaultTreatmentShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllTreatmentsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        treatmentRepository.saveAndFlush(treatment);

        // Get all the treatmentList where date in DEFAULT_DATE or UPDATED_DATE
        defaultTreatmentShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the treatmentList where date equals to UPDATED_DATE
        defaultTreatmentShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllTreatmentsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        treatmentRepository.saveAndFlush(treatment);

        // Get all the treatmentList where date is not null
        defaultTreatmentShouldBeFound("date.specified=true");

        // Get all the treatmentList where date is null
        defaultTreatmentShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllTreatmentsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        treatmentRepository.saveAndFlush(treatment);

        // Get all the treatmentList where date is greater than or equal to DEFAULT_DATE
        defaultTreatmentShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the treatmentList where date is greater than or equal to UPDATED_DATE
        defaultTreatmentShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllTreatmentsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        treatmentRepository.saveAndFlush(treatment);

        // Get all the treatmentList where date is less than or equal to DEFAULT_DATE
        defaultTreatmentShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the treatmentList where date is less than or equal to SMALLER_DATE
        defaultTreatmentShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    public void getAllTreatmentsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        treatmentRepository.saveAndFlush(treatment);

        // Get all the treatmentList where date is less than DEFAULT_DATE
        defaultTreatmentShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the treatmentList where date is less than UPDATED_DATE
        defaultTreatmentShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllTreatmentsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        treatmentRepository.saveAndFlush(treatment);

        // Get all the treatmentList where date is greater than DEFAULT_DATE
        defaultTreatmentShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the treatmentList where date is greater than SMALLER_DATE
        defaultTreatmentShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }


    @Test
    @Transactional
    public void getAllTreatmentsByProceduresIsEqualToSomething() throws Exception {
        // Initialize the database
        treatmentRepository.saveAndFlush(treatment);
        Procedure procedures = ProcedureResourceIT.createEntity(em);
        em.persist(procedures);
        em.flush();
        treatment.addProcedures(procedures);
        treatmentRepository.saveAndFlush(treatment);
        Long proceduresId = procedures.getId();

        // Get all the treatmentList where procedures equals to proceduresId
        defaultTreatmentShouldBeFound("proceduresId.equals=" + proceduresId);

        // Get all the treatmentList where procedures equals to proceduresId + 1
        defaultTreatmentShouldNotBeFound("proceduresId.equals=" + (proceduresId + 1));
    }


    @Test
    @Transactional
    public void getAllTreatmentsByPatientsIsEqualToSomething() throws Exception {
        // Initialize the database
        treatmentRepository.saveAndFlush(treatment);
        Patient patients = PatientResourceIT.createEntity(em);
        em.persist(patients);
        em.flush();
        treatment.setPatients(patients);
        treatmentRepository.saveAndFlush(treatment);
        Long patientsId = patients.getId();

        // Get all the treatmentList where patients equals to patientsId
        defaultTreatmentShouldBeFound("patientsId.equals=" + patientsId);

        // Get all the treatmentList where patients equals to patientsId + 1
        defaultTreatmentShouldNotBeFound("patientsId.equals=" + (patientsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTreatmentShouldBeFound(String filter) throws Exception {
        restTreatmentMockMvc.perform(get("/api/treatments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(treatment.getId().intValue())))
            .andExpect(jsonPath("$.[*].patientID").value(hasItem(DEFAULT_PATIENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));

        // Check, that the count call also returns 1
        restTreatmentMockMvc.perform(get("/api/treatments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTreatmentShouldNotBeFound(String filter) throws Exception {
        restTreatmentMockMvc.perform(get("/api/treatments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTreatmentMockMvc.perform(get("/api/treatments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingTreatment() throws Exception {
        // Get the treatment
        restTreatmentMockMvc.perform(get("/api/treatments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTreatment() throws Exception {
        // Initialize the database
        treatmentService.save(treatment);

        int databaseSizeBeforeUpdate = treatmentRepository.findAll().size();

        // Update the treatment
        Treatment updatedTreatment = treatmentRepository.findById(treatment.getId()).get();
        // Disconnect from session so that the updates on updatedTreatment are not directly saved in db
        em.detach(updatedTreatment);
        updatedTreatment
            .patientID(UPDATED_PATIENT_ID)
            .date(UPDATED_DATE);

        restTreatmentMockMvc.perform(put("/api/treatments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTreatment)))
            .andExpect(status().isOk());

        // Validate the Treatment in the database
        List<Treatment> treatmentList = treatmentRepository.findAll();
        assertThat(treatmentList).hasSize(databaseSizeBeforeUpdate);
        Treatment testTreatment = treatmentList.get(treatmentList.size() - 1);
        assertThat(testTreatment.getPatientID()).isEqualTo(UPDATED_PATIENT_ID);
        assertThat(testTreatment.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingTreatment() throws Exception {
        int databaseSizeBeforeUpdate = treatmentRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTreatmentMockMvc.perform(put("/api/treatments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(treatment)))
            .andExpect(status().isBadRequest());

        // Validate the Treatment in the database
        List<Treatment> treatmentList = treatmentRepository.findAll();
        assertThat(treatmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTreatment() throws Exception {
        // Initialize the database
        treatmentService.save(treatment);

        int databaseSizeBeforeDelete = treatmentRepository.findAll().size();

        // Delete the treatment
        restTreatmentMockMvc.perform(delete("/api/treatments/{id}", treatment.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Treatment> treatmentList = treatmentRepository.findAll();
        assertThat(treatmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
