package it.andrea.start.service.job;

import java.util.Collection;

import it.andrea.start.dto.JobInfoDTO;
import it.andrea.start.error.exception.mapping.MappingToDtoException; // Assicurati che questa esista o rimuovi 'throws'

public interface JobInfoService {

    Collection<JobInfoDTO> listJobs() throws MappingToDtoException;

    void scheduleNewJob(String jobName, String jobGroup);

    void updateScheduleJob(String jobName, String jobGroup);

    void unScheduleJob(String jobName, String jobGroup);

    void deleteJob(String jobName, String jobGroup);

    void pauseJob(String jobName, String jobGroup);

    void resumeJob(String jobName, String jobGroup);

    void startJobNow(String jobName, String jobGroup);

}