package it.andrea.start.error.exception.job;

import java.io.Serial;

public class JobSchedulingException extends JobException {

    @Serial
    private static final long serialVersionUID = 7614149978270160538L;

    public JobSchedulingException(Object[] args) {
        super("Errore schedulazione job " + args[0] + "\\" + args[1], args);
    }

}
