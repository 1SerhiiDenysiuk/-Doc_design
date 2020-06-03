package com.denysiuk.dental.service.impl;

import com.denysiuk.dental.service.ProcedureService;
import com.denysiuk.dental.domain.Procedure;
import com.denysiuk.dental.repository.ProcedureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Procedure}.
 */
@Service
@Transactional
public class ProcedureServiceImpl implements ProcedureService {

    private final Logger log = LoggerFactory.getLogger(ProcedureServiceImpl.class);

    private final ProcedureRepository procedureRepository;

    public ProcedureServiceImpl(ProcedureRepository procedureRepository) {
        this.procedureRepository = procedureRepository;
    }

    /**
     * Save a procedure.
     *
     * @param procedure the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Procedure save(Procedure procedure) {
        log.debug("Request to save Procedure : {}", procedure);
        return procedureRepository.save(procedure);
    }

    /**
     * Get all the procedures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Procedure> findAll(Pageable pageable) {
        log.debug("Request to get all Procedures");
        return procedureRepository.findAll(pageable);
    }


    /**
     * Get one procedure by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Procedure> findOne(Long id) {
        log.debug("Request to get Procedure : {}", id);
        return procedureRepository.findById(id);
    }

    /**
     * Delete the procedure by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Procedure : {}", id);

        procedureRepository.deleteById(id);
    }
}
