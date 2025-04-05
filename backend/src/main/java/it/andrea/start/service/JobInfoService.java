package it.andrea.start.service;

import java.text.ParseException;
import java.util.Collection;

import it.andrea.start.dto.JobInfoDTO;
import it.andrea.start.exception.MappingToDtoException;

public interface JobInfoService {

    Collection<JobInfoDTO> listJobs() throws MappingToDtoException;

    void startAllSchedulers();

    void scheduleNewJob(String jobName) throws Exception;

    void updateScheduleJob(String jobName) throws ParseException;

    boolean unScheduleJob(String jobName);

    boolean deleteJob(String jobName);

    boolean pauseJob(String jobName);

    boolean resumeJob(String jobName);

    boolean startJobNow(String jobName);

}
