package it.andrea.start.dto;

import lombok.Data;

@Data
public class JobInfoDTO {

    private String jobName;
    private String jobGroup;
    private String description;
    private String jobClass;

    // Stato
    private Boolean isActive;
    private Boolean cronJob;

    private String cronExpression;

    private Long repeatIntervalMillis;
    private Long initialDelayMillis;
    private Integer repeatCount;

    private String jobDataMapJson;

}