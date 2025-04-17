package it.andrea.start.quartz;

import org.quartz.SchedulerException;
import org.quartz.spi.InstanceIdGenerator;

import java.util.UUID;

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
