package com.denysiuk.dental.service;

import com.denysiuk.dental.domain.Procedure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Procedure}.
 */
public interface ProcedureService {

    /**
     * Save a procedure.
     *
     * @param procedure the entity to save.
     * @return the persisted entity.
     */
    Procedure save(Procedure procedure);

    /**
     * Get all the procedures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Procedure> findAll(Pageable pageable);


    /**
     * Get the "id" procedure.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Procedure> findOne(Long id);

    /**
     * Delete the "id" procedure.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
