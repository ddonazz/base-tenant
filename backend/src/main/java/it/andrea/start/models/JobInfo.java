package it.andrea.start.models;

import jakarta.persistence.*; // Usa jakarta per Spring Boot 3+
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "job_info")
public class JobInfo {

    @Id
    @Column
    private String jobName;

    @Column(nullable = false)
    private String jobGroup;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String jobClass;

    @Column
    private String cronExpression;

    @Column
    private Long repeatIntervalMillis;

    @Column
    private Long initialDelayMillis;

    @Column()
    private Integer repeatCount;

    @Column(nullable = false)
    private Boolean cronJob;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(columnDefinition = "TEXT")
    private String jobDataMapJson;

}