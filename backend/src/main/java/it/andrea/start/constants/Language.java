package it.andrea.start.constants;

import java.util.Locale;
import java.util.Optional;

public enum Language {

    ITALIAN("it", "IT", Locale.ITALIAN), // it-IT
    ENGLISH_US("en", "US", Locale.US), // en-US
    ENGLISH_UK("en", "GB", Locale.UK); // en-GB

    private final String languageCode; // ISO 639-1
    private final String countryCode; // ISO 3166-1 
    private final Locale locale;
    private final String bcp47Tag;

    Language(String languageCode, String countryCode, Locale locale) {
        this.languageCode = languageCode;
        this.countryCode = countryCode != null ? countryCode : "";
        this.locale = locale;
        if (this.countryCode.isEmpty()) {
            this.bcp47Tag = this.languageCode;
        } else {
            this.bcp47Tag = this.languageCode + "-" + this.countryCode;
        }
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getBcp47Tag() {
        return bcp47Tag;
    }

    public static Optional<Language> fromTag(String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            return Optional.empty();
        }
        for (Language lang : values()) {
            if (lang.bcp47Tag.equalsIgnoreCase(tag)) {
                return Optional.of(lang);
            }
        }
        String langPart = tag.split("-")[0];
        for (Language lang : values()) {
            if (lang.languageCode.equalsIgnoreCase(langPart)) {
                 return Optional.of(lang); 
            }
        }

        return Optional.empty();
    }

    public static Language getDefault() {
        return ITALIAN;
    }
}
