package it.andrea.start.configuration.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import it.andrea.start.configuration.context.tenant.TenantContextHolder;

public class TenantRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return TenantContextHolder.getTenant();
    }

}
