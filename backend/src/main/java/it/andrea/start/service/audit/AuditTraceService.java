package it.andrea.start.service.audit;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.data.domain.Pageable;

import it.andrea.start.dto.audit.AuditTraceDTO;
import it.andrea.start.searchcriteria.audit.AuditTraceSearchCriteria;
import it.andrea.start.utils.PagedResult;

public interface AuditTraceService {

    PagedResult<AuditTraceDTO> searchAuditTrace(AuditTraceSearchCriteria criteria, Pageable pageable);

    void saveAuditTrace(Collection<AuditTraceDTO> audits);

    AuditTraceDTO getAuditTrace(Long id);

    int deleteAuditTrace(LocalDateTime dateCompare);

}
