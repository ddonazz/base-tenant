package it.andrea.start.mappers.audit;

import org.springframework.stereotype.Service;

import it.andrea.start.dto.audit.AuditTraceDTO;
import it.andrea.start.exception.MappingToDtoException;
import it.andrea.start.exception.MappingToEntityException;
import it.andrea.start.mappers.AbstractMapper;
import it.andrea.start.models.audit.AuditTrace;
import it.andrea.start.utils.HelperDate;
import jakarta.persistence.EntityManager;

@Service
public class AuditMapper extends AbstractMapper<AuditTraceDTO, AuditTrace> {

    public AuditMapper(EntityManager entityManager) {
	super(entityManager);
    }

    @Override
    public AuditTraceDTO toDto(AuditTrace entity) throws MappingToDtoException {
	AuditTraceDTO dto = new AuditTraceDTO();
	dto.setId(entity.getId());
	dto.setActivity(entity.getActivity());
	dto.setUserId(entity.getUserId());
	dto.setUserName(entity.getUserName());
	dto.setAuditType(entity.getAuditType());
	dto.setDateEvent(entity.getDateEvent());
	dto.setDateEventString(HelperDate.dateToString(entity.getDateEvent(), HelperDate.TIMESTAMP_WITH_TIMEZONE_FORMAT));
	dto.setControllerMethod(entity.getControllerMethod());
	dto.setEntityName(entity.getEntityName());
	dto.setEntityKeyValue(entity.getEntityKeyValue());
	dto.setEntityOldValue(entity.getEntityOldValue());
	dto.setEntityNewValue(entity.getEntityNewValue());
	dto.setMethod(entity.getMethod());
	dto.setUrl(entity.getUrl());
	dto.setHttpContextRequest(entity.getHttpContextRequest());
	dto.setHttpContextResponse(entity.getHttpContextResponse());
	dto.setExceptionTrace(entity.getExceptionTrace());

	return dto;
    }

    @Override
    public void toEntity(AuditTraceDTO dto, AuditTrace entity) throws MappingToEntityException {
	entity.setActivity(dto.getActivity());
	entity.setUserId(dto.getUserId());
	entity.setUserName(dto.getUserName());
	entity.setAuditType(dto.getAuditType());
	entity.setDateEvent(dto.getDateEvent());
	entity.setControllerMethod(dto.getControllerMethod());
	entity.setEntityName(dto.getEntityName());
	entity.setEntityKeyValue(dto.getEntityKeyValue());
	entity.setEntityOldValue(dto.getEntityOldValue());
	entity.setEntityNewValue(dto.getEntityNewValue());
	entity.setMethod(dto.getMethod());
	entity.setUrl(dto.getUrl());
	entity.setHttpContextRequest(dto.getHttpContextRequest());
	entity.setHttpContextResponse(dto.getHttpContextResponse());
	entity.setExceptionTrace(dto.getExceptionTrace());
    }

}
