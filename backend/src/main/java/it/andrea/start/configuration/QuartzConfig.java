package it.andrea.start.configuration;

import javax.sql.DataSource;

import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class QuartzConfig {

    @Bean
    SchedulerFactoryBeanCustomizer schedulerFactoryBeanCustomizer(DataSource dataSource, PlatformTransactionManager transactionManager) {
        return schedulerFactoryBean -> {
            schedulerFactoryBean.setDataSource(dataSource);
            schedulerFactoryBean.setTransactionManager(transactionManager);
        };
    }

    @Bean
    JobFactory springBeanJobFactory(ApplicationContext applicationContext) {
        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    public static class AutoWiringSpringBeanJobFactory extends SpringBeanJobFactory {

        private ApplicationContext applicationContext;

        @Override
        public void setApplicationContext(@NonNull final ApplicationContext context) {
            this.applicationContext = context;
            super.setApplicationContext(context);
        }

        @NonNull
        @Override
        protected Object createJobInstance(@NonNull final TriggerFiredBundle bundle) throws Exception {
            final Object job = super.createJobInstance(bundle);
            if (applicationContext != null) {
                applicationContext.getAutowireCapableBeanFactory().autowireBean(job);
            }

            return job;
        }
    }

}