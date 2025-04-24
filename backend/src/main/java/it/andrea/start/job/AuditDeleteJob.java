package it.andrea.start.job;

import java.time.LocalDateTime;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import it.andrea.start.configuration.GlobalConfig;
import it.andrea.start.service.audit.AuditTraceService;
import lombok.AllArgsConstructor;

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

        LOG.info("Start at : {}", LocalDateTime.now());

        LocalDateTime dateCompare = LocalDateTime.now().minusDays(retentionDays);
        LOG.info("Delete audits before of : {}", dateCompare);

        int rowDeleted = auditTraceService.deleteAuditTrace(dateCompare);
        LOG.info("Deleted audits : {}", rowDeleted);

        LOG.info("Ending at : {}", LocalDateTime.now());
    }
}
