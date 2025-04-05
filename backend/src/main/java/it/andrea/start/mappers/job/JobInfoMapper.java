package it.andrea.start.mappers.job;

import org.springframework.stereotype.Service;

import it.andrea.start.dto.JobInfoDTO;
import it.andrea.start.exception.MappingToDtoException;
import it.andrea.start.exception.MappingToEntityException;
import it.andrea.start.mappers.AbstractMapper;
import it.andrea.start.models.JobInfo;
import jakarta.persistence.EntityManager;

@Service
public class JobInfoMapper extends AbstractMapper<JobInfoDTO, JobInfo> {

    public JobInfoMapper(EntityManager entityManager) {
	super(entityManager);
    }

    @Override
    public JobInfoDTO toDto(JobInfo entity) throws MappingToDtoException {
	JobInfoDTO dto = new JobInfoDTO();
	dto.setJobName(entity.getJobName());
	dto.setDescription(entity.getDescription());
	dto.setJobGroup(entity.getJobGroup());
	dto.setCronExpression(entity.getCronExpression());
	dto.setRepeatTime(entity.getRepeatTime());
	dto.setCronJob(entity.getCronJob());
	dto.setIsActive(entity.getIsActive());

	return dto;
    }

    @Override
    public void toEntity(JobInfoDTO dto, JobInfo entity) throws MappingToEntityException {
	entity.setJobName(dto.getJobName());
	entity.setDescription(dto.getDescription());
	entity.setJobGroup(dto.getJobGroup());
	entity.setCronExpression(dto.getCronExpression());
	entity.setRepeatTime(dto.getRepeatTime());
	entity.setCronJob(dto.getCronJob());
	entity.setIsActive(dto.getIsActive());
    }

}
