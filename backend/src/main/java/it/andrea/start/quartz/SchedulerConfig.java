package it.andrea.start.quartz;

import java.util.Properties;

import javax.sql.DataSource;

import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
@ConditionalOnProperty(name = "quartz.enabled")
public class SchedulerConfig {

    private DataSource dataSource;
    private ApplicationContext applicationContext;
    private QuartzProperties quartzProperties;

    public SchedulerConfig(DataSource dataSource, ApplicationContext applicationContext, QuartzProperties quartzProperties) {
        super();
        this.dataSource = dataSource;
        this.applicationContext = applicationContext;
        this.quartzProperties = quartzProperties;
    }

    @Bean
    JobFactory jobFactory() {
        SchedulerJobFactory jobFactory = new SchedulerJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory) throws Exception {
        Properties properties = new Properties();
        properties.putAll(quartzProperties.getProperties());

        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setOverwriteExistingJobs(true);
        factory.setDataSource(dataSource);
        factory.setJobFactory(jobFactory);
        factory.setQuartzProperties(properties);
        factory.afterPropertiesSet();
        clearScheduler(factory);

        return factory;
    }

    private void clearScheduler(SchedulerFactoryBean factory) {
        try {
            factory.getScheduler().clear();
        } catch (SchedulerException e) {
            throw new IllegalStateException("Error while clearing scheduler", e);
        }
    }

}
