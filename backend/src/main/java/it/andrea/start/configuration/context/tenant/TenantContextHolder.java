package it.andrea.start.configuration.context.tenant;

import org.springframework.core.NamedInheritableThreadLocal;
import org.springframework.core.NamedThreadLocal;

public class TenantContextHolder {

    private static final ThreadLocal<TenantContext> tenantHolder = new NamedThreadLocal<>("TenantContext");
    private static final ThreadLocal<TenantContext> inheritableTenantHolder = new NamedInheritableThreadLocal<>("TenantContext");

    private TenantContextHolder() {
    }

    public static void setTenant(TenantContext tenantContext) {
        setTenant(tenantContext, false);
    }

    public static void setTenant(TenantContext tenantContext, boolean inheritable) {
        if (tenantContext == null) {
            resetTenant();
        } else {
            if (inheritable) {
                inheritableTenantHolder.set(tenantContext);
                tenantHolder.remove();
            } else {
                tenantHolder.set(tenantContext);
                inheritableTenantHolder.remove();
            }
        }
    }

    public static TenantContext getTenant() {
        TenantContext tenant = tenantHolder.get();
        return (tenant != null) ? tenant : inheritableTenantHolder.get();
    }

    public static void resetTenant() {
        tenantHolder.remove();
        inheritableTenantHolder.remove();
    }

}
