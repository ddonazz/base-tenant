package it.andrea.start.quartz;

import java.util.UUID;

import org.quartz.SchedulerException;
import org.quartz.spi.InstanceIdGenerator;

public class QuartzInstanceIdGenerator implements InstanceIdGenerator {

    @Override
    public String generateInstanceId() throws SchedulerException {
        try {
            return UUID.randomUUID().toString();
        } catch (RuntimeException ex) {
            throw new SchedulerException("Unable to generate instance ID.", ex);
        }
    }

}
