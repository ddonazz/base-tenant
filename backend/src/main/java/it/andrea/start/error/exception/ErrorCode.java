package it.andrea.start.error.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // --- Internal Generic Errors ---
    ERROR_INTERNAL_SERVER_ERROR("error.internal.server.error", HttpStatus.INTERNAL_SERVER_ERROR, null),
    ERROR_TRANSACTION_ROLLED_BACK("error.transaction.rollback", HttpStatus.INTERNAL_SERVER_ERROR, null),
    ERROR_PERSISTENCE_EXCEPTION("error.persistence.exception", HttpStatus.INTERNAL_SERVER_ERROR, null),
    ERROR_CONSTRAINT_VIOLATION("error.constraint.violation", HttpStatus.CONFLICT, null),
    ERROR_SQL_GENERIC("error.sql.generic", HttpStatus.INTERNAL_SERVER_ERROR, null),
    ERROR_NULL_POINTER("error.null.pointer", HttpStatus.INTERNAL_SERVER_ERROR, null),

    // --- Authorization Login Errors ---
    USER_AUTHORIZE_LOGIN_ACCOUNT_PENDING("user.authorize.login.account.pending", HttpStatus.UNAUTHORIZED, "User"),
    USER_AUTHORIZE_LOGIN_ACCOUNT_SUSPENDED("user.authorize.login.account.suspended", HttpStatus.UNAUTHORIZED, "User"),
    USER_AUTHORIZE_LOGIN_ACCOUNT_DEACTIVE("user.authorize.login.account.deactivate", HttpStatus.UNAUTHORIZED, "User"),
    USER_AUTHORIZE_LOGIN_ACCOUNT_BLACKLIST("user.authorize.login.account.blacklist", HttpStatus.UNAUTHORIZED, "User"),

    // --- Authorization User/Password Errors (Logica di Auth) ---
    AUTHENTICATION_FAILED("authentication.failed", HttpStatus.UNAUTHORIZED, "User"),
    AUTHORIZEUSER_USERNAME_NULL("authorize.user.username.null", HttpStatus.BAD_REQUEST, "User"),
    AUTHORIZEUSER_PASSWORD_NULL("authorize.user.password.null", HttpStatus.BAD_REQUEST, "User"),
    AUTHORIZEUSER_USER_NOT_FOUND("authorize.user.user.not.found", HttpStatus.UNAUTHORIZED, "User"),
    AUTHORIZEUSER_PASSWORD_WRONG("authorize.user.password.wrong", HttpStatus.UNAUTHORIZED, "User"),

    // --- User Management Errors (Logica Applicativa) ---
    USER_ID_NULL("error.user.id.null", HttpStatus.BAD_REQUEST, "User"),
    USER_NOT_FOUND("error.user.id.not.found", HttpStatus.NOT_FOUND, "User"),
    USER_NOT_ACTIVE("error.user.id.not.active", HttpStatus.FORBIDDEN, "User"),
    USER_ALREADY_EXISTS("error.user.id.already.exists", HttpStatus.CONFLICT, "User"),
    USER_USERNAME_ALREADY_USED("error.user.username.already.used", HttpStatus.CONFLICT, "User"),
    USER_EMAIL_ALREADY_USED("error.user.email.already.used", HttpStatus.CONFLICT, "User"),
    USER_ROLE_NULL("error.user.role.null", HttpStatus.BAD_REQUEST, "User"),

    // --- User Role Business Logic Errors ---
    USER_ROLE_NOT_FOUND("error.user.role.not.found", HttpStatus.NOT_FOUND, "User"),
    USER_ROLE_ADMIN_NOT_USABLE("error.user.role.admin.not.usable", HttpStatus.BAD_REQUEST, "User"),
    USER_ROLE_MANAGER_NOT_USABLE("error.user.role.manager.not.usable", HttpStatus.BAD_REQUEST, "User"),
    USER_ROLE_ADMIN_NOT_DELETE("error.user.role.admin.not.delete", HttpStatus.FORBIDDEN, "User"),
    USER_ROLE_MANAGER_NOT_DELETE("error.user.role.manager.not.delete", HttpStatus.FORBIDDEN, "User"),
    USER_ROLE_ADMIN_NOT_CHANGE_PASSWORD("error.user.role.admin.not.change.password", HttpStatus.FORBIDDEN, "User"),
    USER_ROLE_MANAGER_NOT_CHANGE_PASSWORD("error.user.role.manager.not.change.password", HttpStatus.FORBIDDEN, "User"),
    USER_REPEAT_PASSWORD_NOT_EQUAL("error.user.repeat.password.not.equal", HttpStatus.BAD_REQUEST, "User"),

    // --- Job Logic Errors ---
    JOB_NOT_FOUND_EXCEPTION("job.not.found.exception", HttpStatus.NOT_FOUND, "Job"),
    JOB_SCHEDULING_EXCEPTION("job.scheduling.exception", HttpStatus.INTERNAL_SERVER_ERROR, "Job"),
    JOB_CONTROL_EXCEPTION("job.control.exception", HttpStatus.INTERNAL_SERVER_ERROR, "Job");

    private final String code;
    private final HttpStatus httpStatus;
    private final String entity;

    ErrorCode(String code, HttpStatus httpStatus, String entity) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.entity = entity;
    }

    public static ErrorCode getDefault() {
        return ERROR_INTERNAL_SERVER_ERROR;
    }

}