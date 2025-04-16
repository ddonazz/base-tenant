package it.andrea.start.service.audit;

import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;

import it.andrea.start.dto.audit.AuditTraceDTO;
import it.andrea.start.models.audit.AuditTrace;
import it.andrea.start.searchcriteria.audit.AuditTraceSearchCriteria;
import it.andrea.start.utils.PagedResult;

public interface AuditTraceService {

    PagedResult<AuditTraceDTO> searchAuditTrace(AuditTraceSearchCriteria criteria, Pageable pageable);

    void saveLog(AuditTrace auditTrace);

    AuditTraceDTO getAuditTrace(Long id);

    int deleteAuditTrace(LocalDateTime dateCompare);

}
