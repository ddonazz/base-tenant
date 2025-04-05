package it.andrea.start.constants;

public interface ApplicationConstants {

    // Entità
    static final String SYSTEM_ENTITY_MANAGE = "SYSTEM";
    static final String ADMIN_ENTITY_MANAGE = "ADMIN";

    // Validità del token in giorni
    static final int TOKEN_VALIDITY_DAYS = 30;

    // Lingue
    static final String DEFAULT_LANGUAGE = "it";
    static final String ENGLISH_LANGUAGE = "en";

    // Validità del token dell'utente in giorni
    static final int DAY_VALIDATION_TOKEN_USER = 3;

    // Ritardo standard in secondi per l'attivazione del trigger
    static final long SECOND_STANDARD_DELAY_START_TRIGGER = 5L;

    // Nomi dei job
    static final String INIZIALIZE_JOB = "InitializeJob";
    static final String INIZIALIZE_JOB_GROUP = INIZIALIZE_JOB + "_Group";

}
