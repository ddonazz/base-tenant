package it.andrea.start.configuration;

import it.andrea.start.constants.AuditLevel;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class GlobalConfig {

    private final AuditLevel auditLevel;
    private final int auditSavedDay;

    public GlobalConfig(Environment environment) {
        auditLevel = AuditLevel.valueOf(environment.getProperty("app.audit.level"));
        auditSavedDay = Integer.valueOf(environment.getProperty("app.audit.day"));
    }

    public AuditLevel getAuditLevel() {
        return auditLevel;
    }

    public int getAuditSavedDay() {
        return auditSavedDay;
    }

}
