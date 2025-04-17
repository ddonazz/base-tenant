package it.andrea.start.quartz;

import jakarta.validation.constraints.NotNull;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

@Component
public class SchedulerJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull final ApplicationContext context) {
        applicationContext = context;
    }

    @Override
    protected Object createJobInstance(@NonNull final TriggerFiredBundle bundle) throws Exception {
        final Object job = super.createJobInstance(bundle);
        applicationContext.getAutowireCapableBeanFactory().autowireBean(job);

        return job;
    }

}
