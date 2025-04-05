
package it.andrea.start.exception;

public interface ExceptionCodeError {

    public static final String CODE_USER_AUTHORIZE_LOGIN_ACCOUNT_PENDING = "user.authorize.login.account.pending";
    public static final String CODE_USER_AUTHORIZE_LOGIN_ACCOUNT_SUSPENDED = "user.authorize.login.account.suspended";
    public static final String CODE_USER_AUTHORIZE_LOGIN_ACCOUNT_DEACTIVE = "user.authorize.login.account.deactive";
    public static final String CODE_USER_AUTHORIZE_LOGIN_ACCOUNT_BLACKLIST = "user.authorize.login.account.blacklist";

    public static final String CODE_AUTHORIZEUSER_USERNAME_NULL = "authorizeuser.username.null";
    public static final String CODE_AUTHORIZEUSER_PASSWORD_NULL = "authorizeuser.password.null";
    public static final String CODE_AUTHORIZEUSER_USERNOTFOUND = "authorizeuser.usernotfound";
    public static final String CODE_AUTHORIZEUSER_PASSWORDWRONG = "authorizeuser.passwordwrong";

    public static final String CODE_USER_ID_NULL = "error.user.id.null";
    public static final String CODE_USER_USER_NOT_FOUND = "error.user.id.not.found";
    public static final String CODE_USER_USER_NOT_ACTIVE = "error.user.id.not.active";
    public static final String CODE_USER_USERNAME_NULL = "error.user.username.null";
    public static final String CODE_USER_USERNAME_TOO_SHORT = "error.user.username.too.short";
    public static final String CODE_USER_USERNAME_TOO_LONG = "error.user.username.too.long";
    public static final String CODE_USER_USERNAME_ALREADY_USED = "error.user.username.already.used";
    public static final String CODE_USER_PASSWORD_NULL = "error.user.password.null";
    public static final String CODE_USER_PASSWORD_TOO_SHORT = "error.user.password.too.short";
    public static final String CODE_USER_PASSWORD_TOO_LONG = "error.user.password.too.long";
    public static final String CODE_USER_REPEAT_PASSWORD_NULL = "error.user.repeat.password.null";
    public static final String CODE_USER_REPEAT_PASSWORD_NOT_EQUAL = "error.user.repeat.password.not.equal";
    public static final String CODE_USER_NAME_NULL = "error.user.name.null";
    public static final String CODE_USER_NAME_TOO_SHORT = "error.user.name.too.short";
    public static final String CODE_USER_NAME_TOO_LONG = "error.user.name.too.long";
    public static final String CODE_USER_EMAIL_NULL = "error.user.email.null";
    public static final String CODE_USER_EMAIL_NOT_VALID = "error.user.email.notvalid";
    public static final String CODE_USER_EMAIL_TOO_SHORT = "error.user.email.too.short";
    public static final String CODE_USER_EMAIL_TOO_LONG = "error.user.email.too.long";
    public static final String CODE_USER_EMAIL_ALREADY_USED = "error.user.email.already.used";
    public static final String CODE_USER_ROLE_NULL = "error.user.role.null";
    public static final String CODE_USER_STATUS_NULL = "error.user.status.null";
    public static final String CODE_USER_ROLE_ADMIN_NOT_USABLE = "error.user.role.admin.not.usable";
    public static final String CODE_USER_ROLE_MANAGER_NOT_USABLE = "error.user.role.manager.not.usable";
    public static final String CODE_USER_ROLE_ADMIN_NOT_DELETE = "error.user.role.admin.not.delete";
    public static final String CODE_USER_ROLE_MANAGER_NOT_DELETE = "error.user.role.manager.not.delete";
    public static final String CODE_USER_ROLE_ADMIN_NOT_CHANGE_PASSWORD = "error.user.role.admin.not.change.password";
    public static final String CODE_USER_ROLE_MANAGER_NOT_CHANGE_PASSWORD = "error.user.role.manager.not.change.password";

    public static final String CODE_USER_ROLE_ROLENAME_NULL = "error.user.role.rolename.null";
    public static final String CODE_USER_ROLE_ROLENAME_WRONG_LENGHT = "error.user.role.rolename.wrong.lenght";
    public static final String CODE_USER_ROLE_ROLENAME_TOO_SHORT = "error.user.role.rolename.too.short";
    public static final String CODE_USER_ROLE_ROLENAME_TOO_LONG = "error.user.role.rolename.too.long";
    public static final String CODE_USER_ROLE_ROLENAME_ALREADY_PRESENT = "error.user.role.rolename.already.present";
    public static final String CODE_USER_ROLE_ROLENAME_NOT_PRESENT = "error.user.role.rolename.not.present";
    public static final String CODE_USER_ROLE_DESCRIPTION_NULL = "error.user.role.description.null";
    public static final String CODE_USER_ROLE_DESCRIPTION_WRONG_LENGHT = "error.user.role.description.wrong.lenght";
    public static final String CODE_USER_ROLE_DESCRIPTION_TOO_SHORT = "error.user.role.description.too.short";
    public static final String CODE_USER_ROLE_DESCRIPTION_TOO_LONG = "error.user.role.description.too.long";

}
