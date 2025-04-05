package it.andrea.start.models;

import org.quartz.SimpleTrigger;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "job_info")
public class JobInfo {

    @Id
    private String jobName;

    @Column(nullable = false, name = "description")
    private String description;

    @Column(nullable = false, name = "job_group")
    private String jobGroup;

    @Column(nullable = false, name = "job_class")
    private String jobClass;

    @Column(nullable = true, name = "cron_expression")
    private String cronExpression;

    @Column(nullable = true, name = "repeat_time")
    private Long repeatTime;

    @Column(nullable = false, name = "repeat_count")
    private int repeatCount = SimpleTrigger.REPEAT_INDEFINITELY;

    @Column(nullable = false, name = "cron_job")
    private Boolean cronJob;

    @Column(nullable = false, name = "is_active")
    private Boolean isActive;

    public String getJobName() {
	return jobName;
    }

    public void setJobName(String jobName) {
	this.jobName = jobName;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getJobGroup() {
	return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
	this.jobGroup = jobGroup;
    }

    public String getJobClass() {
	return jobClass;
    }

    public void setJobClass(String jobClass) {
	this.jobClass = jobClass;
    }

    public String getCronExpression() {
	return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
	this.cronExpression = cronExpression;
    }

    public Long getRepeatTime() {
	return repeatTime;
    }

    public void setRepeatTime(Long repeatTime) {
	this.repeatTime = repeatTime;
    }

    public Boolean getCronJob() {
	return cronJob;
    }

    public void setCronJob(Boolean cronJob) {
	this.cronJob = cronJob;
    }

    public Boolean getIsActive() {
	return isActive;
    }

    public void setIsActive(Boolean isActive) {
	this.isActive = isActive;
    }

    public int getRepeatCount() {
	return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
	this.repeatCount = repeatCount;
    }

}