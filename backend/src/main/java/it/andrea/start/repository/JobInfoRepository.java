package it.andrea.start.repository;

import it.andrea.start.models.JobInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface JobInfoRepository extends JpaRepository<JobInfo, String>, JpaSpecificationExecutor<JobInfo> {

    Optional<JobInfo> findByJobName(String jobName);

}
