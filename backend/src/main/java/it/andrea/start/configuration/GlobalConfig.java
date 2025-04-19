package it.andrea.start.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.andrea.start.constants.AuditLevel;
import lombok.Getter;

@Getter
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class GlobalConfig {

    @Value("${app.audit.level}")
    private AuditLevel auditLevel;

    @Value("${app.audit.day}")
    private int auditSavedDay;

}
