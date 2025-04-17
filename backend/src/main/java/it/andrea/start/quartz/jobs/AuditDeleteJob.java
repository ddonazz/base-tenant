package it.andrea.start.quartz.jobs;

import it.andrea.start.configuration.GlobalConfig;
import it.andrea.start.service.audit.AuditTraceService;
import jakarta.validation.constraints.NotNull;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;

@ComponentScan
@DisallowConcurrentExecution
public class AuditDeleteJob extends QuartzJobBean {

    private static final Logger LOG = LoggerFactory.getLogger(AuditDeleteJob.class);

    private GlobalConfig globalConfig;
    private AuditTraceService auditTraceService;

    public AuditDeleteJob() {
        super();
    }

    public AuditDeleteJob(GlobalConfig globalConfig, AuditTraceService auditTraceService) {
        super();
        this.globalConfig = globalConfig;
        this.auditTraceService = auditTraceService;
    }

    @Override
    protected void executeInternal(@NonNull JobExecutionContext context) {
        LOG.info("AuditDeleteJob start at : {}", LocalDateTime.now());

        LocalDateTime dateCompare = LocalDateTime.now().minusDays(globalConfig.getAuditSavedDay());
        LOG.info("AuditDeleteJob delete audits before of : {}", dateCompare);

        int rowDeleted = auditTraceService.deleteAuditTrace(dateCompare);
        LOG.info("AuditDeleteJob deleted audits : {}", rowDeleted);

        LOG.info("AuditDeleteJob ending at : {}", LocalDateTime.now());
    }
}
