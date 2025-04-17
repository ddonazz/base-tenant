package it.andrea.start.service.job;

import it.andrea.start.dto.JobInfoDTO;
import it.andrea.start.error.exception.mapping.MappingToDtoException;

import java.text.ParseException;
import java.util.Collection;

public interface JobInfoService {

    Collection<JobInfoDTO> listJobs() throws MappingToDtoException;

    void startAllSchedulers();

    void scheduleNewJob(String jobName) throws ParseException;

    void updateScheduleJob(String jobName) throws ParseException;

    boolean unScheduleJob(String jobName);

    boolean deleteJob(String jobName);

    boolean pauseJob(String jobName);

    boolean resumeJob(String jobName);

    boolean startJobNow(String jobName);

}
