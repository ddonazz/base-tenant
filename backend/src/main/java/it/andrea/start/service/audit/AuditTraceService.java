package it.andrea.start.service.audit;

import java.time.LocalDateTime;
import java.util.Collection;

import it.andrea.start.dto.audit.AuditTraceDTO;
import it.andrea.start.exception.MappingToDtoException;
import it.andrea.start.exception.MappingToEntityException;
import it.andrea.start.searchcriteria.audit.AuditTraceSearchCriteria;
import it.andrea.start.utils.PagedResult;

public interface AuditTraceService {

    void saveAuditTrace(Collection<AuditTraceDTO> audits) throws MappingToEntityException;

    PagedResult<AuditTraceDTO> searchAuditTrace(AuditTraceSearchCriteria criteria, int pageNum, int pageSize) throws MappingToDtoException;

    AuditTraceDTO getAuditTrace(Long id) throws MappingToDtoException;

    int deleteAuditTrace(LocalDateTime dateCompare);

}
