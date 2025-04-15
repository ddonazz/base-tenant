package it.andrea.start.error.exception;

public enum ErrorCode {

    // --- Internal Generic Errors ---
    ERROR_INTERNAL_SERVER_ERROR("error.internal.server.error"), //
    ERROR_TRANSACTION_ROLLED_BACK("error.transaction.rolledback"), //
    ERROR_PERSISTENCE_EXCEPTION("error.persistence.exception"), //
    ERROR_CONSTRAINT_VIOLATION("error.constraint.violation"), //
    ERROR_SQL_GENERIC("error.sql.generic"), //
    ERROR_NULL_POINTER("error.nullpointer"), //

    // --- Authorization Login Errors ---
    USER_AUTHORIZE_LOGIN_ACCOUNT_PENDING("user.authorize.login.account.pending"), //
    USER_AUTHORIZE_LOGIN_ACCOUNT_SUSPENDED("user.authorize.login.account.suspended"), //
    USER_AUTHORIZE_LOGIN_ACCOUNT_DEACTIVE("user.authorize.login.account.deactive"), //
    USER_AUTHORIZE_LOGIN_ACCOUNT_BLACKLIST("user.authorize.login.account.blacklist"), //

    // --- Authorization User/Password Errors ---
    AUTHORIZEUSER_USERNAME_NULL("authorize.user.username.null"), //
    AUTHORIZEUSER_PASSWORD_NULL("authorize.user.password.null"), //
    AUTHORIZEUSER_USERNOTFOUND("authorize.user.usernotfound"), //
    AUTHORIZEUSER_PASSWORDWRONG("authorize.user.passwordwrong"), //

    // --- User Management Errors ---
    USER_ID_NULL("error.user.id.null"), //
    USER_NOT_FOUND("error.user.id.not.found"), //
    USER_NOT_ACTIVE("error.user.id.not.active"), //
    USER_ALREADY_EXISTS("error.user.id.already.exists"), //
    USER_USERNAME_NULL("error.user.username.null"), //
    USER_USERNAME_TOO_SHORT("error.user.username.too.short"), //
    USER_USERNAME_TOO_LONG("error.user.username.too.long"), //
    USER_USERNAME_ALREADY_USED("error.user.username.already.used"), //
    USER_PASSWORD_NULL("error.user.password.null"), //
    USER_PASSWORD_TOO_SHORT("error.user.password.too.short"), //
    USER_PASSWORD_TOO_LONG("error.user.password.too.long"), //
    USER_REPEAT_PASSWORD_NULL("error.user.repeat.password.null"), //
    USER_REPEAT_PASSWORD_NOT_EQUAL("error.user.repeat.password.not.equal"), //
    USER_NAME_NULL("error.user.name.null"), //
    USER_NAME_TOO_SHORT("error.user.name.too.short"), //
    USER_NAME_TOO_LONG("error.user.name.too.long"), //
    USER_EMAIL_NULL("error.user.email.null"), //
    USER_EMAIL_NOT_VALID("error.user.email.notvalid"), //
    USER_EMAIL_TOO_SHORT("error.user.email.too.short"), //
    USER_EMAIL_TOO_LONG("error.user.email.too.long"), //
    USER_EMAIL_ALREADY_USED("error.user.email.already.used"), //
    USER_ROLE_NULL("error.user.role.null"), //
    USER_STATUS_NULL("error.user.status.null"), //
    USER_ROLE_ADMIN_NOT_USABLE("error.user.role.admin.not.usable"), //
    USER_ROLE_MANAGER_NOT_USABLE("error.user.role.manager.not.usable"), //
    USER_ROLE_ADMIN_NOT_DELETE("error.user.role.admin.not.delete"), //
    USER_ROLE_MANAGER_NOT_DELETE("error.user.role.manager.not.delete"), //
    USER_ROLE_ADMIN_NOT_CHANGE_PASSWORD("error.user.role.admin.not.change.password"), //
    USER_ROLE_MANAGER_NOT_CHANGE_PASSWORD("error.user.role.manager.not.change.password"), //

    // --- User Role Management Errors ---
    USER_ROLE_ROLENAME_NULL("error.user.role.rolename.null"), //
    USER_ROLE_ROLENAME_WRONG_LENGTH("error.user.role.rolename.wrong.length"), //
    USER_ROLE_ROLENAME_TOO_SHORT("error.user.role.rolename.too.short"), //
    USER_ROLE_ROLENAME_TOO_LONG("error.user.role.rolename.too.long"), //
    USER_ROLE_ROLENAME_ALREADY_PRESENT("error.user.role.rolename.already.present"), //
    USER_ROLE_ROLENAME_NOT_PRESENT("error.user.role.rolename.not.present"), //
    USER_ROLE_DESCRIPTION_NULL("error.user.role.description.null"), //
    USER_ROLE_DESCRIPTION_WRONG_LENGTH("error.user.role.description.wrong.length"), //
    USER_ROLE_DESCRIPTION_TOO_SHORT("error.user.role.description.too.short"), //
    USER_ROLE_DESCRIPTION_TOO_LONG("error.user.role.description.too.long");//

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static ErrorCode getDefault() {
        return ERROR_INTERNAL_SERVER_ERROR;
    }

}
