package it.andrea.start.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelperDate {

    public static final String TIMESTAMP_WITH_TIMEZONE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final String TIMESTAMP_WITHOUT_TIMEZONE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DATE_FORMAT_LINE = "yyyy-MM-dd";
    public static final String DATE_FORMAT_SLASH = "dd/MM/yyyy";
    public static final String DATE_FORMAT_DAY = "dd-MM-yyyy";

    private static final Logger LOG = LoggerFactory.getLogger(HelperDate.class);

    private HelperDate() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static LocalDateTime parseDate(String dateStr, String format, boolean returnNullOnException) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        try {
            return dateStr != null ? LocalDateTime.parse(dateStr, formatter) : null;
        } catch (DateTimeParseException e) {
            if (!returnNullOnException) {
                throw new RuntimeException("Failed to parse date: " + dateStr, e);
            }
            return null;
        }
    }

    public static String formatDate(LocalDateTime date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return date != null ? date.format(formatter) : null;
    }

    public static String formatDate(LocalDate date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return date != null ? date.format(formatter) : null;
    }

    public static LocalDateTime parseDateOrThrow(String dateStr, String format) {
        if (dateStr == null) {
            throw new IllegalArgumentException("dateStr cannot be null");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(dateStr, formatter);
    }

    public static LocalDateTime parseDateLogError(String dateStr, String format) {
        if (dateStr == null || format == null) {
            LOG.error("Date string or format is null");
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDateTime.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            LOG.error("Parse error for date: " + dateStr + " with format " + format, e);
            return null;
        }
    }

    private static LocalDate parseDateString(String dateString, String format) {
        if (StringUtils.isBlank(dateString)) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            LOG.error("Failed to parse date: " + dateString + " with format " + format, e);
            return null;
        }
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static LocalDate stringToDateBar(String s) {
        if (s.contains("-")) {
            return stringToDate(s);
        }
        return parseDateString(s, DATE_FORMAT_SLASH);
    }

    public static LocalDate stringToDate(String s) {
        if (s.contains("/")) {
            return stringToDateBar(s);
        }
        return parseDateString(s, DATE_FORMAT_LINE);
    }

    public static String dateToString(LocalDate date) {
        return formatDate(date, DATE_FORMAT_LINE);
    }

    public static String dateToString(LocalDate date, String format) {
        return formatDate(date, format);
    }

    public static String dateToString(LocalDateTime date) {
        return formatDate(date, TIMESTAMP_WITHOUT_TIMEZONE_FORMAT);
    }

    public static String dateToString(LocalDateTime date, String format) {
        return formatDate(date, format);
    }

    public static Integer year() {
        return LocalDate.now().getYear();
    }

    public static Integer year(LocalDate date) {
        return date.getYear();
    }

    public static Integer month(LocalDate date) {
        return date.getMonthValue();
    }

    public static Integer day(LocalDate date) {
        return date.getDayOfMonth();
    }

    public static int totalDayOfMonth(int year, int month) {
        LocalDate date = LocalDate.of(year, month, 1);
        return date.lengthOfMonth();
    }

    public static int totalDayOfMonth(LocalDate date) {
        return date.lengthOfMonth();
    }

    public static int getDayOfWeek(LocalDate date) {
        return date.getDayOfWeek().getValue();
    }

    public static int getDayOfYear(LocalDate date) {
        return date.getDayOfYear();
    }

    public static long getDaysBetweenTwoDates(LocalDate dateStart, LocalDate dateEnd) {
        if (dateStart == null || dateEnd == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return ChronoUnit.DAYS.between(dateStart, dateEnd);
    }

    public static long getSecondsBetweenTwoDates(LocalDateTime dateStart, LocalDateTime dateEnd) {
        if (dateStart == null || dateEnd == null) {
            throw new IllegalArgumentException("Both start and end dates must be non-null");
        }
        return ChronoUnit.SECONDS.between(dateStart, dateEnd);
    }

    public static List<Integer> getYearsBetweenDates(LocalDate dateStart, LocalDate dateEnd) {
        List<Integer> years = new ArrayList<>();
        if (dateStart == null || dateEnd == null) {
            throw new IllegalArgumentException("Both start and end dates must be non-null");
        }
        if (dateStart.isAfter(dateEnd)) {
            return years;
        }
        for (int year = dateStart.getYear(); year <= dateEnd.getYear(); year++) {
            years.add(year);
        }
        return years;
    }

    public static LocalDate addYear(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.plusYears(1);
    }

    public static LocalDate addDaysToDate(LocalDate date, int days) {
        if (date == null) {
            throw new IllegalArgumentException("Date must not be null");
        }
        return date.plusDays(days);
    }

    public static LocalDateTime addSecondsToDate(LocalDateTime date, int seconds) {
        if (date == null) {
            return null;
        }
        return date.plusSeconds(seconds);
    }

    public static LocalDate succ(LocalDate start, LocalDate stop, int dayOfWeek) {
        if (start.get(ChronoField.DAY_OF_WEEK) == dayOfWeek) {
            return start;
        }
        int daysToAdd = (dayOfWeek - start.get(ChronoField.DAY_OF_WEEK) + 7) % 7;
        if (daysToAdd == 0) {
            daysToAdd = 7;
        }
        LocalDate nextDay = start.plusDays(daysToAdd);
        if (!nextDay.isAfter(stop)) {
            return nextDay;
        }
        return null;
    }

    public static int dayOfWeek(LocalDateTime date) {
        if (date == null) {
            throw new IllegalArgumentException("Date must not be null");
        }
        return date.getDayOfWeek().getValue();
    }

    public static LocalDate getDate(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }

}
