package it.andrea.start.service.audit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.andrea.start.dto.audit.AuditTraceDTO;
import it.andrea.start.error.exception.mapping.MappingToDtoException;
import it.andrea.start.error.exception.mapping.MappingToEntityException;
import it.andrea.start.mappers.audit.AuditMapper;
import it.andrea.start.models.audit.AuditTrace;
import it.andrea.start.repository.audit.AuditTraceRepository;
import it.andrea.start.searchcriteria.audit.AuditTraceSearchCriteria;
import it.andrea.start.searchcriteria.audit.AuditTraceSearchSpecification;
import it.andrea.start.utils.PagedResult;

@Service
@Transactional
public class AuditTraceServiceImpl implements AuditTraceService {

    private final AuditTraceRepository auditTraceRepository;

    private final AuditMapper auditMapper;

    public AuditTraceServiceImpl(AuditTraceRepository auditTraceRepository, AuditMapper auditMapper) {
	super();
	this.auditTraceRepository = auditTraceRepository;
	this.auditMapper = auditMapper;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void saveAuditTrace(Collection<AuditTraceDTO> audits) throws MappingToEntityException {
        if (audits == null || audits.isEmpty()) {
            return;
        }

        List<AuditTrace> entities = new ArrayList<>();
        for (AuditTraceDTO auditTraceDTO : audits) {
            if (auditTraceDTO == null) {
                continue;
            }
            AuditTrace auditTrace = new AuditTrace();
            auditMapper.toEntity(auditTraceDTO, auditTrace);

            entities.add(auditTrace);
        }

        if (!entities.isEmpty()) {
            auditTraceRepository.saveAll(entities);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public PagedResult<AuditTraceDTO> searchAuditTrace(AuditTraceSearchCriteria criteria, Pageable pageable) throws MappingToDtoException {
        Page<AuditTrace> auditPage = auditTraceRepository.findAll(new AuditTraceSearchSpecification(criteria), pageable);

        final Page<AuditTraceDTO> dtoPage = auditPage.map(audit -> {
            try {
                return auditMapper.toDto(audit);
            } catch (MappingToDtoException e) {
                throw new RuntimeException("Errore durante il mapping dell'utente", e);
            }
        });

        final PagedResult<AuditTraceDTO> result = new PagedResult<>();
        result.setItems(dtoPage.getContent());
        result.setPageNumber(dtoPage.getNumber() + 1);
        result.setPageSize(dtoPage.getSize());
        result.setTotalElements((int) dtoPage.getTotalElements());

        return result;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public AuditTraceDTO getAuditTrace(Long id) throws MappingToDtoException {
        Optional<AuditTrace> auditOpt = auditTraceRepository.findById(id);
        if (auditOpt.isEmpty()) {
            return null;
        }

        AuditTrace auditTrace = auditOpt.get();

        return auditMapper.toDto(auditTrace);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public int deleteAuditTrace(LocalDateTime dateCompare) {
        return auditTraceRepository.deleteRows(dateCompare);
    }

}
