package it.andrea.start.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import it.andrea.start.models.JobInfo;

public interface JobInfoRepository extends JpaRepository<JobInfo, String>, JpaSpecificationExecutor<JobInfo> {

    Optional<JobInfo> findByJobName(String jobName);

    Optional<JobInfo> findByJobNameAndJobGroup(String jobName, String jobGroup);

    List<JobInfo> findByIsActiveTrue();

}
