package com.denysiuk.dental.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.denysiuk.dental.domain.Treatment} entity. This class is used
 * in {@link com.denysiuk.dental.web.rest.TreatmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /treatments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TreatmentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter patientID;

    private ZonedDateTimeFilter date;

    private LongFilter proceduresId;

    private LongFilter patientsId;

    public TreatmentCriteria() {
    }

    public TreatmentCriteria(TreatmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.patientID = other.patientID == null ? null : other.patientID.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.proceduresId = other.proceduresId == null ? null : other.proceduresId.copy();
        this.patientsId = other.patientsId == null ? null : other.patientsId.copy();
    }

    @Override
    public TreatmentCriteria copy() {
        return new TreatmentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getPatientID() {
        return patientID;
    }

    public void setPatientID(LongFilter patientID) {
        this.patientID = patientID;
    }

    public ZonedDateTimeFilter getDate() {
        return date;
    }

    public void setDate(ZonedDateTimeFilter date) {
        this.date = date;
    }

    public LongFilter getProceduresId() {
        return proceduresId;
    }

    public void setProceduresId(LongFilter proceduresId) {
        this.proceduresId = proceduresId;
    }

    public LongFilter getPatientsId() {
        return patientsId;
    }

    public void setPatientsId(LongFilter patientsId) {
        this.patientsId = patientsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TreatmentCriteria that = (TreatmentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(patientID, that.patientID) &&
            Objects.equals(date, that.date) &&
            Objects.equals(proceduresId, that.proceduresId) &&
            Objects.equals(patientsId, that.patientsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        patientID,
        date,
        proceduresId,
        patientsId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TreatmentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (patientID != null ? "patientID=" + patientID + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (proceduresId != null ? "proceduresId=" + proceduresId + ", " : "") +
                (patientsId != null ? "patientsId=" + patientsId + ", " : "") +
            "}";
    }

}
