package it.andrea.start.error.exception.job;

import java.io.Serial;

import it.andrea.start.error.exception.ApplicationException;
import it.andrea.start.error.exception.ErrorCode;

public class JobControlException extends ApplicationException {

    @Serial
    private static final long serialVersionUID = -4106296616521977141L;

    public JobControlException(String message, Object... messageArguments) {
        super(ErrorCode.JOB_CONTROL_EXCEPTION, message + " " + messageArguments[0] + "\\" + messageArguments[1], messageArguments);
    }

}
