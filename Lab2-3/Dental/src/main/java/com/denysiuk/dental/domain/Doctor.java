package com.denysiuk.dental.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Doctor.
 */
@Entity
@Table(name = "doctor")
public class Doctor implements Serializable {

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

    @NotNull
    @Column(name = "specialization", nullable = false)
    private String specialization;

    @NotNull
    @Column(name = "department_id", nullable = false)
    private Long departmentID;

    @OneToMany(mappedBy = "doctors")
    private Set<Patient> pacients = new HashSet<>();

    @OneToMany(mappedBy = "docrors")
    private Set<Department> departments = new HashSet<>();

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

    public Doctor name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTel() {
        return tel;
    }

    public Doctor tel(Long tel) {
        this.tel = tel;
        return this;
    }

    public void setTel(Long tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public Doctor address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSpecialization() {
        return specialization;
    }

    public Doctor specialization(String specialization) {
        this.specialization = specialization;
        return this;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Long getDepartmentID() {
        return departmentID;
    }

    public Doctor departmentID(Long departmentID) {
        this.departmentID = departmentID;
        return this;
    }

    public void setDepartmentID(Long departmentID) {
        this.departmentID = departmentID;
    }

    public Set<Patient> getPacients() {
        return pacients;
    }

    public Doctor pacients(Set<Patient> patients) {
        this.pacients = patients;
        return this;
    }

    public Doctor addPacients(Patient patient) {
        this.pacients.add(patient);
        patient.setDoctors(this);
        return this;
    }

    public Doctor removePacients(Patient patient) {
        this.pacients.remove(patient);
        patient.setDoctors(null);
        return this;
    }

    public void setPacients(Set<Patient> patients) {
        this.pacients = patients;
    }

    public Set<Department> getDepartments() {
        return departments;
    }

    public Doctor departments(Set<Department> departments) {
        this.departments = departments;
        return this;
    }

    public Doctor addDepartments(Department department) {
        this.departments.add(department);
        department.setDocrors(this);
        return this;
    }

    public Doctor removeDepartments(Department department) {
        this.departments.remove(department);
        department.setDocrors(null);
        return this;
    }

    public void setDepartments(Set<Department> departments) {
        this.departments = departments;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Doctor)) {
            return false;
        }
        return id != null && id.equals(((Doctor) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Doctor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", tel=" + getTel() +
            ", address='" + getAddress() + "'" +
            ", specialization='" + getSpecialization() + "'" +
            ", departmentID=" + getDepartmentID() +
            "}";
    }
}
