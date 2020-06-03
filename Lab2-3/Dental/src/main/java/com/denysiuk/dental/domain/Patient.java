package com.denysiuk.dental.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Patient.
 */
@Entity
@Table(name = "patient")
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "tel", nullable = false)
    private Long tel;

    @Column(name = "address")
    private String address;

    @Column(name = "age")
    private Long age;

    @Column(name = "sex")
    private String sex;

    @Column(name = "doctor_id")
    private Long doctorID;

    @OneToMany(mappedBy = "patients")
    private Set<Treatment> treatments = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "pacients", allowSetters = true)
    private Doctor doctors;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Patient name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTel() {
        return tel;
    }

    public Patient tel(Long tel) {
        this.tel = tel;
        return this;
    }

    public void setTel(Long tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public Patient address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getAge() {
        return age;
    }

    public Patient age(Long age) {
        this.age = age;
        return this;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public Patient sex(String sex) {
        this.sex = sex;
        return this;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Long getDoctorID() {
        return doctorID;
    }

    public Patient doctorID(Long doctorID) {
        this.doctorID = doctorID;
        return this;
    }

    public void setDoctorID(Long doctorID) {
        this.doctorID = doctorID;
    }

    public Set<Treatment> getTreatments() {
        return treatments;
    }

    public Patient treatments(Set<Treatment> treatments) {
        this.treatments = treatments;
        return this;
    }

    public Patient addTreatments(Treatment treatment) {
        this.treatments.add(treatment);
        treatment.setPatients(this);
        return this;
    }

    public Patient removeTreatments(Treatment treatment) {
        this.treatments.remove(treatment);
        treatment.setPatients(null);
        return this;
    }

    public void setTreatments(Set<Treatment> treatments) {
        this.treatments = treatments;
    }

    public Doctor getDoctors() {
        return doctors;
    }

    public Patient doctors(Doctor doctor) {
        this.doctors = doctor;
        return this;
    }

    public void setDoctors(Doctor doctor) {
        this.doctors = doctor;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Patient)) {
            return false;
        }
        return id != null && id.equals(((Patient) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Patient{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", tel=" + getTel() +
            ", address='" + getAddress() + "'" +
            ", age=" + getAge() +
            ", sex='" + getSex() + "'" +
            ", doctorID=" + getDoctorID() +
            "}";
    }
}
