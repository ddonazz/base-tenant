package it.andrea.start.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import it.andrea.start.models.JobInfo;

public interface JobInfoRepository extends JpaRepository<JobInfo, String>, JpaSpecificationExecutor<JobInfo> {

    public Optional<JobInfo> findByJobName(String jobName);

}
