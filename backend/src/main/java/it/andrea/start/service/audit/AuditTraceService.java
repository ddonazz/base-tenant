package it.andrea.start.service.audit;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.data.domain.Pageable;

import it.andrea.start.dto.audit.AuditTraceDTO;
import it.andrea.start.error.exception.mapping.MappingToDtoException;
import it.andrea.start.error.exception.mapping.MappingToEntityException;
import it.andrea.start.searchcriteria.audit.AuditTraceSearchCriteria;
import it.andrea.start.utils.PagedResult;

public interface AuditTraceService {

    PagedResult<AuditTraceDTO> searchAuditTrace(AuditTraceSearchCriteria criteria, Pageable pageable) throws MappingToDtoException;

    void saveAuditTrace(Collection<AuditTraceDTO> audits) throws MappingToEntityException;

    AuditTraceDTO getAuditTrace(Long id) throws MappingToDtoException;

    int deleteAuditTrace(LocalDateTime dateCompare);

}
