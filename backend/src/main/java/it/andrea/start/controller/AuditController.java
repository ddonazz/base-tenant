package it.andrea.start.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.andrea.start.constants.AuditActivity;
import it.andrea.start.constants.AuditTypeOperation;
import it.andrea.start.dto.audit.AuditTraceDTO;
import it.andrea.start.error.exception.mapping.MappingToDtoException;
import it.andrea.start.searchcriteria.audit.AuditTraceSearchCriteria;
import it.andrea.start.service.audit.AuditTraceService;
import it.andrea.start.utils.HelperDate;
import it.andrea.start.utils.PagedResult;

@Tag(name = "Audit API")
@RestController
@RequestMapping("/api/audit")
@PreAuthorize("hasRole('ADMIN')")
public class AuditController {

    private final AuditTraceService auditTraceService;

    public AuditController(AuditTraceService auditTraceService) {
        super();
        this.auditTraceService = auditTraceService;
    }

    @Operation(
        method = "GET",
        description = "List audits by search criteria with timezone date",
        summary = "List audits by search criteria with timezone date"
    )
    @GetMapping("/list")
    public ResponseEntity<PagedResult<AuditTraceDTO>> listAudits(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String sessionId,
            @RequestParam(required = false) AuditActivity activity,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) AuditTypeOperation auditType,
            @RequestParam(required = false) String textSearch,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo,
            Pageable pageable) throws MappingToDtoException {
        AuditTraceSearchCriteria auditTraceSearchCriteria = new AuditTraceSearchCriteria();
        auditTraceSearchCriteria.setId(id);
        auditTraceSearchCriteria.setSessionId(sessionId);
        auditTraceSearchCriteria.setActivity(activity);
        auditTraceSearchCriteria.setUserId(userId);
        auditTraceSearchCriteria.setUserName(userName);
        auditTraceSearchCriteria.setAuditType(auditType);
        auditTraceSearchCriteria.setTextSearch(textSearch);
        auditTraceSearchCriteria.setDateFrom(HelperDate.parseDate(dateFrom, HelperDate.TIMESTAMP_WITH_TIMEZONE_FORMAT, true));
        auditTraceSearchCriteria.setDateTo(HelperDate.parseDate(dateTo, HelperDate.TIMESTAMP_WITH_TIMEZONE_FORMAT, true));

        PagedResult<AuditTraceDTO> audits = auditTraceService.searchAuditTrace(auditTraceSearchCriteria, pageable);
        return ResponseEntity.ok(audits);
    }

    @Operation(
        method = "GET",
        description = "Return information of a specific audit",
        summary = "Return information of a specific audit"
    )
    @GetMapping("/{id}")
    public ResponseEntity<AuditTraceDTO> getAudit(@PathVariable Long id) throws MappingToDtoException {
        AuditTraceDTO audits = auditTraceService.getAuditTrace(id);
        return ResponseEntity.ok(audits);
    }

}
