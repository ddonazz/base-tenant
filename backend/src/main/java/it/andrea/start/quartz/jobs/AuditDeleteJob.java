package it.andrea.start.quartz.jobs;

import it.andrea.start.configuration.GlobalConfig;
import it.andrea.start.service.audit.AuditTraceService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@DisallowConcurrentExecution
@AllArgsConstructor
public class AuditDeleteJob extends QuartzJobBean {

    private static final Logger LOG = LoggerFactory.getLogger(AuditDeleteJob.class);

    private final GlobalConfig globalConfig;
    private final AuditTraceService auditTraceService;

    @Override
    public void executeInternal(@NonNull JobExecutionContext context) {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        int retentionDays = jobDataMap.getIntValue("retentionDays");
        if (retentionDays <= 0) {
            retentionDays = globalConfig.getAuditSavedDay();
        }

        LOG.info("AuditDeleteJob start at : {}", LocalDateTime.now());

        LocalDateTime dateCompare = LocalDateTime.now().minusDays(retentionDays);
        LOG.info("AuditDeleteJob delete audits before of : {}", dateCompare);

        int rowDeleted = auditTraceService.deleteAuditTrace(dateCompare);
        LOG.info("AuditDeleteJob deleted audits : {}", rowDeleted);

        LOG.info("AuditDeleteJob ending at : {}", LocalDateTime.now());
    }
}
