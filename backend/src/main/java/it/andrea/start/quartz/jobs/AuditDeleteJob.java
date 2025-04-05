package it.andrea.start.quartz.jobs;

import java.time.LocalDateTime;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.quartz.QuartzJobBean;

import it.andrea.start.configuration.GlobalConfig;
import it.andrea.start.service.audit.AuditTraceService;

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
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
	LOG.info("AuditDeleteJob inizio alle : {}", LocalDateTime.now());

	LocalDateTime dateCompare = LocalDateTime.now().minusDays(globalConfig.getAuditSavedDay());
	LOG.info("AuditDeleteJob eliminazione audit prima di : {}", dateCompare);

	int rowDeleted = auditTraceService.deleteAuditTrace(dateCompare);
	LOG.info("AuditDeleteJob numero audit eliminati : {}", rowDeleted);

	LOG.info("AuditDeleteJob finito alle : {}", LocalDateTime.now());
    }
}
