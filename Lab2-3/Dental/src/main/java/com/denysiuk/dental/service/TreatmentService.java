package com.denysiuk.dental.service;

import com.denysiuk.dental.domain.Treatment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Treatment}.
 */
public interface TreatmentService {

    /**
     * Save a treatment.
     *
     * @param treatment the entity to save.
     * @return the persisted entity.
     */
    Treatment save(Treatment treatment);

    /**
     * Get all the treatments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Treatment> findAll(Pageable pageable);


    /**
     * Get the "id" treatment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Treatment> findOne(Long id);

    /**
     * Delete the "id" treatment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
