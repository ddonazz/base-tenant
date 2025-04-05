package it.andrea.start.configuration.context.tenant;

import org.springframework.lang.Nullable;

public interface TenantContext {
    
    @Nullable
    String getAgency();
    
    @Nullable
    String getDatabaseName();

}
