package it.andrea.start.dto;

import lombok.Data;

@Data
public class JobInfoDTO {

    private String jobName;
    private String description;
    private String jobGroup;
    private String cronExpression;
    private Long repeatTime;
    private Boolean cronJob;
    private Boolean isActive;

}
