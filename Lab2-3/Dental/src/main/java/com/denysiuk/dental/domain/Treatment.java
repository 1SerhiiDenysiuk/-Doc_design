package com.denysiuk.dental.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Treatment.
 */
@Entity
@Table(name = "treatment")
public class Treatment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id")
    private Long patientID;

    @Column(name = "date")
    private ZonedDateTime date;

    @OneToMany(mappedBy = "treatments")
    private Set<Procedure> procedures = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "treatments", allowSetters = true)
    private Patient patients;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPatientID() {
        return patientID;
    }

    public Treatment patientID(Long patientID) {
        this.patientID = patientID;
        return this;
    }

    public void setPatientID(Long patientID) {
        this.patientID = patientID;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Treatment date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Set<Procedure> getProcedures() {
        return procedures;
    }

    public Treatment procedures(Set<Procedure> procedures) {
        this.procedures = procedures;
        return this;
    }

    public Treatment addProcedures(Procedure procedure) {
        this.procedures.add(procedure);
        procedure.setTreatments(this);
        return this;
    }

    public Treatment removeProcedures(Procedure procedure) {
        this.procedures.remove(procedure);
        procedure.setTreatments(null);
        return this;
    }

    public void setProcedures(Set<Procedure> procedures) {
        this.procedures = procedures;
    }

    public Patient getPatients() {
        return patients;
    }

    public Treatment patients(Patient patient) {
        this.patients = patient;
        return this;
    }

    public void setPatients(Patient patient) {
        this.patients = patient;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Treatment)) {
            return false;
        }
        return id != null && id.equals(((Treatment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Treatment{" +
            "id=" + getId() +
            ", patientID=" + getPatientID() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
