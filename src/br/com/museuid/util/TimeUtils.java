package br.com.museuid.util;

import br.com.museuid.config.ConstantConfig;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Handling, formatting and clacing dates,
 * mainly assisting in converting dates from the new java api from LocalDate to Timestamp for insertion into the database
 *
 * @author Angelica
 */
public class TimeUtils {
    private static String TIME_FULL_FORMAT = "dd/MM/yyyy hh:mm:ss";
    private static String DATE_FORMAT = "dd/MM/yyyy";
    private static String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";

    private TimeUtils() {
    }

    /**
     * Converts a LocalDate to Timestamp with zeroed times, ie midnight
     */
    public static Timestamp toTimestamp(LocalDate data) {
        return Timestamp.valueOf(data.atStartOfDay());
    }

    /**
     * Convert Local Date Time to Timestamp
     */
    public static Timestamp toTimestamp(LocalDateTime data) {
        return Timestamp.valueOf(data);
    }

    /**
     * Convert String to Timestamp
     */
    public static Timestamp toTimestamp(String data) {
        return Timestamp.valueOf(LocalDateTime.parse(data, formatter("yyyy-MM-dd HH:mm.ss")));
    }

    /**
     * Converts String to Timestamp indicating the format
     */
    public static Timestamp toTimestamp(String data, String modelo) {
        return Timestamp.valueOf(LocalDateTime.parse(data, formatter(modelo)));
    }

    /**
     * Convert Timestamp to Local Date Time
     */
    public static LocalDateTime toDateTime(Timestamp time) {
        return time.toLocalDateTime();
    }

    /**
     * Convert Timestamp to Local Date Time
     */
    public static LocalDate toDate(Timestamp time) {
        return time.toLocalDateTime().toLocalDate();
    }

    /**
     * Convert String to LocalDate
     */
    public static LocalDate toDate(String data) {
        return LocalDate.parse(data, formatter("yyyy-MM-dd"));
    }

    /**
     * Converts String to LocalDate indicating the format
     */
    public static LocalDate toDate(String data, String model) {
        return LocalDate.parse(data, formatter(model));
    }

    /**
     * Tranform Timestamp in String
     */
    public static String toString(Timestamp data) {
        return data == null ? "" : data.toLocalDateTime().format(formatter("dd/MM/yyyy"));
    }

    /**
     * Transforms Timestamp into String format indicated
     */
    public static String toString(Timestamp data, String model) {
        return data == null ? "" : data.toLocalDateTime().format(formatter(model));
    }

    /**
     * Converts LocalDateTime to String in dd / MM / yyyy format
     */
    public static String toString(LocalDate data) {
        return data == null ? "" : data.format(formatter("dd/MM/yyyy"));
    }

    /**
     * Converts LocalDateTime to String indicating toString format
     */
    public static String toString(LocalDate data, String model) {
        return data == null ? "" : data.format(formatter(model));
    }

    /**
     * Converts LocalDateTime to String indicating toString format
     */
    public static String toString(LocalDateTime data, String model) {
        return data == null ? "" : data.format(formatter(model));
    }

    /**
     * Informs the date of the number month and returns its abbreviated name in string Ex: 1-> JAN, 2-> FEV, 3-> MAR ... 12-> TEN
     */
    public static String getMonthString(String data) {
        return Month.of(Integer.parseInt(data)).getDisplayName(TextStyle.SHORT, new Locale(ConstantConfig.defaultLocaleCode)).toUpperCase();
    }

    /**
     * Returns current date in timestamp format
     */
    public static Timestamp atual() {
        return toTimestamp(LocalDate.now());
    }

    /**
     * Generate a formatter for formatting toDate in string in the format indicated
     */
    private static DateTimeFormatter formatter(String modelo) {
        return DateTimeFormatter.ofPattern(modelo);
    }

    /**
     * Block days of previous DatePickers to informed leaving in red dates not selectable
     */
    public static void blockDataAnterior(LocalDate data, DatePicker calendarario) {

        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {

                        super.updateItem(item, empty);

                        if (item.isBefore(data.plusDays(1))) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc8c3;");
                        }
                    }
                };
            }
        };

        calendarario.setDayCellFactory(dayCellFactory);
        calendarario.setValue(data.plusDays(1));
    }

    public static String convertDateTimeToStartTimeFormat(LocalDate localDate) {
        String r =  toString(localDate, DEFAULT_DATE_FORMAT);
        r+= " 00:00:00";
        return r;
    }
    public static String convertDateTimeToEndTimeFormat(LocalDate localDate) {
        String r =  toString(localDate, DEFAULT_DATE_FORMAT);
        r+= " 23:59:00";
        return r;
    }

    public static void reformatDatePickerValue(DatePicker datePicker) {
        datePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    try {
                        return DateTimeFormatter.ofPattern(DATE_FORMAT).format(date);
                    } catch (DateTimeException dte) {
                    }
                    return "";
                }
                return "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    try {
                        return LocalDate.parse(string, DateTimeFormatter.ofPattern(DATE_FORMAT));
                    } catch (DateTimeParseException dtpe) {
                       Messenger.erro(dtpe.getMessage());
                    }
                }
                return null;
            }

        });
    }
}
