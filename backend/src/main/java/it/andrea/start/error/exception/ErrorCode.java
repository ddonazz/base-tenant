package it.andrea.start.error.exception;

public enum ErrorCode {

    // --- Internal Generic Errors ---
    ERROR_INTERNAL_SERVER_ERROR("error.internal.server.error"),
    ERROR_TRANSACTION_ROLLED_BACK("error.transaction.rollback"),
    ERROR_PERSISTENCE_EXCEPTION("error.persistence.exception"),
    ERROR_CONSTRAINT_VIOLATION("error.constraint.violation"),
    ERROR_SQL_GENERIC("error.sql.generic"),
    ERROR_NULL_POINTER("error.null.pointer"),

    // --- Authorization Login Errors ---
    USER_AUTHORIZE_LOGIN_ACCOUNT_PENDING("user.authorize.login.account.pending"),
    USER_AUTHORIZE_LOGIN_ACCOUNT_SUSPENDED("user.authorize.login.account.suspended"),
    USER_AUTHORIZE_LOGIN_ACCOUNT_DEACTIVE("user.authorize.login.account.deactivate"), // Chiave corretta
    USER_AUTHORIZE_LOGIN_ACCOUNT_BLACKLIST("user.authorize.login.account.blacklist"),

    // --- Authorization User/Password Errors (Logica di Auth) ---
    AUTHORIZEUSER_USERNAME_NULL("authorize.user.username.null"),
    AUTHORIZEUSER_PASSWORD_NULL("authorize.user.password.null"),
    AUTHORIZEUSER_USERNOTFOUND("authorize.user.user.not.found"),
    AUTHORIZEUSER_PASSWORD_WRONG("authorize.user.password.wrong"),

    // --- User Management Errors (Logica Applicativa) ---
    USER_ID_NULL("error.user.id.null"), // ID nullo passato a un metodo che lo richiede
    USER_NOT_FOUND("error.user.id.not.found"), // Utente non trovato per ID
    USER_NOT_ACTIVE("error.user.id.not.active"), // Utente trovato ma non attivo per l'operazione
    USER_ALREADY_EXISTS("error.user.id.already.exists"), // Conflitto ID (raro) o errore logico generico esistenza
    USER_USERNAME_ALREADY_USED("error.user.username.already.used"), // Username già in uso (controllo logico)
    USER_EMAIL_ALREADY_USED("error.user.email.already.used"), // Email già in uso (controllo logico)
    USER_ROLE_NULL("error.user.role.null"), // Ruolo non fornito quando richiesto per logica

    // --- User Role Business Logic Errors ---
    USER_ROLE_ADMIN_NOT_USABLE("error.user.role.admin.not.usable"),
    USER_ROLE_MANAGER_NOT_USABLE("error.user.role.manager.not.usable"),
    USER_ROLE_ADMIN_NOT_DELETE("error.user.role.admin.not.delete"),
    USER_ROLE_MANAGER_NOT_DELETE("error.user.role.manager.not.delete"),
    USER_ROLE_ADMIN_NOT_CHANGE_PASSWORD("error.user.role.admin.not.change.password"),
    USER_ROLE_MANAGER_NOT_CHANGE_PASSWORD("error.user.role.manager.not.change.password"),
    USER_REPEAT_PASSWORD_NOT_EQUAL("error.user.repeat.password.not.equal"),

    // --- Job Logic Errors ---
    JOB_SCHEDULING_EXCEPTION("job.scheduling.exception"),;

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public static ErrorCode getDefault() {
        return ERROR_INTERNAL_SERVER_ERROR;
    }

    public String getCode() {
        return code;
    }

}