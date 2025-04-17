package it.andrea.start.mappers.audit;

import it.andrea.start.dto.audit.AuditTraceDTO;
import it.andrea.start.error.exception.mapping.MappingToDtoException;
import it.andrea.start.error.exception.mapping.MappingToEntityException;
import it.andrea.start.mappers.AbstractMapper;
import it.andrea.start.models.audit.AuditTrace;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

@Component
public class AuditMapper extends AbstractMapper<AuditTraceDTO, AuditTrace> {

    public AuditMapper(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public AuditTraceDTO toDto(AuditTrace entity) throws MappingToDtoException {
        if (entity == null) {
            return null;
        }
        AuditTraceDTO dto = new AuditTraceDTO();

        // Mappatura campi base
        dto.setId(entity.getId());
        dto.setDateEvent(entity.getDateEvent());
        dto.setActivity(entity.getActivity());
        dto.setAuditType(entity.getAuditType());

        // Mappatura utente
        // dto.setUserId(entity.getUserId());
        dto.setUsername(entity.getUsername());

        // Mappatura contesto applicativo
        dto.setClassName(entity.getClassName());
        dto.setMethodName(entity.getMethodName());
        dto.setControllerMethod(entity.getControllerMethod());
        dto.setResourceId(entity.getResourceId());

        // Mappatura dettagli HTTP
        dto.setHttpMethod(entity.getHttpMethod());
        dto.setRequestUri(entity.getRequestUri());
        dto.setClientIpAddress(entity.getClientIpAddress());
        dto.setUserAgent(entity.getUserAgent());
        dto.setRequestParams(entity.getRequestParams());
        dto.setRequestBody(entity.getRequestBody());

        // Mappatura esito
        dto.setHttpStatus(entity.getHttpStatus());
        dto.setSuccess(entity.getSuccess());
        dto.setDurationMs(entity.getDurationMs());
        dto.setExceptionTrace(entity.getExceptionTrace());

        return dto;
    }

    @Override
    public void toEntity(AuditTraceDTO dto, AuditTrace entity) throws MappingToEntityException {
        if (dto == null || entity == null) {
            return;
        }

        // Non mappare l'ID (generalmente gestito dalla persistenza)
        // Non mappare dateEvent qui, di solito viene impostato alla creazione
        // dell'entità

        entity.setActivity(dto.getActivity());
        entity.setAuditType(dto.getAuditType());

        // Mappatura utente
        // entity.setUserId(dto.getUserId()); // Decommenta se hai userId nell'entità
        entity.setUsername(dto.getUsername());

        // Mappatura contesto applicativo
        entity.setClassName(dto.getClassName());
        entity.setMethodName(dto.getMethodName());
        entity.setControllerMethod(dto.getControllerMethod());
        entity.setResourceId(dto.getResourceId());

        // Mappatura dettagli HTTP
        entity.setHttpMethod(dto.getHttpMethod());
        entity.setRequestUri(dto.getRequestUri());
        entity.setClientIpAddress(dto.getClientIpAddress());
        entity.setUserAgent(dto.getUserAgent());
        entity.setRequestParams(dto.getRequestParams());
        entity.setRequestBody(dto.getRequestBody()); // **ATTENZIONE AI DATI SENSIBILI**

        // Mappatura esito
        entity.setHttpStatus(dto.getHttpStatus());
        entity.setSuccess(dto.getSuccess());
        entity.setDurationMs(dto.getDurationMs());
        entity.setExceptionTrace(dto.getExceptionTrace());

    }
}