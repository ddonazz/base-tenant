package it.andrea.start.mappers.audit;

import org.springframework.stereotype.Component;

import it.andrea.start.dto.audit.AuditTraceDTO;
import it.andrea.start.error.exception.mapping.MappingToDtoException;
import it.andrea.start.error.exception.mapping.MappingToEntityException;
import it.andrea.start.mappers.AbstractMapper;
import it.andrea.start.models.audit.AuditTrace;
import it.andrea.start.utils.HelperDate;
import jakarta.persistence.EntityManager;

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

        dto.setId(entity.getId());
        dto.setDateEvent(HelperDate.dateToString(entity.getDateEvent()));
        dto.setActivity(entity.getActivity());
        dto.setAuditType(entity.getAuditType());

        dto.setUsername(entity.getUsername());

        dto.setClassName(entity.getClassName());
        dto.setMethodName(entity.getMethodName());
        dto.setControllerMethod(entity.getControllerMethod());

        dto.setHttpMethod(entity.getHttpMethod());
        dto.setRequestUri(entity.getRequestUri());
        dto.setClientIpAddress(entity.getClientIpAddress());
        dto.setUserAgent(entity.getUserAgent());
        dto.setRequestParams(entity.getRequestParams());
        dto.setRequestBody(entity.getRequestBody());

        dto.setSuccess(entity.getSuccess());
        dto.setDurationMs(entity.getDurationMs());

        dto.setExceptionType(entity.getExceptionType());
        dto.setExceptionMessage(entity.getExceptionMessage());

        return dto;
    }

    @Override
    public void toEntity(AuditTraceDTO dto, AuditTrace entity) throws MappingToEntityException {
        if (dto == null || entity == null) {
            return;
        }

        entity.setActivity(dto.getActivity());
        entity.setAuditType(dto.getAuditType());

        entity.setUsername(dto.getUsername());

        entity.setClassName(dto.getClassName());
        entity.setMethodName(dto.getMethodName());
        entity.setControllerMethod(dto.getControllerMethod());

        entity.setHttpMethod(dto.getHttpMethod());
        entity.setRequestUri(dto.getRequestUri());
        entity.setClientIpAddress(dto.getClientIpAddress());
        entity.setUserAgent(dto.getUserAgent());
        entity.setRequestParams(dto.getRequestParams());
        entity.setRequestBody(dto.getRequestBody());

        entity.setSuccess(dto.getSuccess());
        entity.setDurationMs(dto.getDurationMs());

        entity.setExceptionType(dto.getExceptionType());
        entity.setExceptionMessage(dto.getExceptionMessage());
    }
}