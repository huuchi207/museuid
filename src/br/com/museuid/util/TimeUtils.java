package br.com.museuid.util;

import br.com.museuid.config.ConstantConfig;
import br.com.museuid.screen.product_statistic.ProductStatisticController;
import br.com.museuid.screen.statistic.RevenueStatisticController;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
    String r = toString(localDate, DEFAULT_DATE_FORMAT);
    r += " 00:00:00";
    return r;
  }

  public static String convertDateTimeToEndTimeFormat(LocalDate localDate) {
    String r = toString(localDate, DEFAULT_DATE_FORMAT);
    r += " 23:59:00";
    return r;
  }

  public static String convertDateTimeToRequestTimeFormat(Integer d, Integer m, Integer y, boolean isFromDate) {
    if (m != null) {
      m++;// real month
    }
    if (m == null) {//period = YEAR, format = yyyy
      return y + "";
    } else if (d == null) {//period = MONTH, format = yyyyMM
      return y + ((m > 10) ? (m + "") : ("0" + m));
    } else {//period = DAY, format = MM/dd/yyyy
      String date = ((m > 10) ? (m + "") : ("0" + m)) + "/" + ((d > 10) ? (d + "") : ("0" + d)) + "/" + y;
      if (isFromDate) {
        date += " 00:00:00";
      } else {
        date += " 23:59:59";
      }
      return date;
    }
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

  public static String convertToLocalFormat(String dateStr) {
    try {
      TimeZone utc = TimeZone.getTimeZone("UTC");
      SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
      SimpleDateFormat destFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
      sourceFormat.setTimeZone(utc);
      Date convertedDate = sourceFormat.parse(dateStr);
      return destFormat.format(convertedDate);
    } catch (Exception e) {
      return "";
    }
  }

  public static boolean validDate(String fromDate, String toDate, int period) {
    SimpleDateFormat sdf;
    Date from, to;
    try {
      switch (period) {
        case RevenueStatisticController
          .SESSION:
        case RevenueStatisticController
          .DAY:
          sdf = new SimpleDateFormat(DATE_FORMAT);
          to = sdf.parse(toDate);
          from = sdf.parse(fromDate);
          if (to.compareTo(from) >= 0)
            return true;
          else return false;
        case RevenueStatisticController
          .MONTH:
          sdf = new SimpleDateFormat("MM/yyyy");
          to = sdf.parse(toDate);
          from = sdf.parse(fromDate);
          if (to.compareTo(from) >= 0)
            return true;
          else return false;
        case RevenueStatisticController
          .YEAR:
          sdf = new SimpleDateFormat("yyyy");
          to = sdf.parse(toDate);
          from = sdf.parse(fromDate);
          if (to.compareTo(from) >= 0)
            return true;
          else return false;
      }
    } catch (ParseException e) {
      e.printStackTrace();
      return false;
    }
    return false;
  }

  public static boolean validDate(String fromDate, String toDate, ProductStatisticController.Period period) {
    switch (period) {
      case YEAR:
        return validDate(fromDate, toDate, RevenueStatisticController.YEAR);
      case MONTH:
        return validDate(fromDate, toDate, RevenueStatisticController.MONTH);
      case DAY:
        return validDate(fromDate, toDate, RevenueStatisticController.DAY);
        default:
        return false;
    }
  }

  public static boolean validDistanceDate(String fromDate, String toDate, int period) {
    SimpleDateFormat sdf;
    Date from, to;
    try {
      switch (period) {
        case RevenueStatisticController
          .SESSION:
        case RevenueStatisticController
          .DAY:
          sdf = new SimpleDateFormat(DATE_FORMAT);
          to = sdf.parse(toDate);
          from = sdf.parse(fromDate);
          if (ChronoUnit.DAYS.between(from.toInstant(), to.toInstant()) +1 > 31){//+1: include from
            return false;
          } else return true;
        case RevenueStatisticController
          .MONTH:
          sdf = new SimpleDateFormat("MM/yyyy");
          to = sdf.parse(toDate);
          from = sdf.parse(fromDate);
          if (numberOfMonthBetweenDates(from.getMonth(), from.getYear(), to.getMonth(), to.getYear()) > 31){//+1: include from
            return false;
          } else return true;
        case RevenueStatisticController
          .YEAR:
          sdf = new SimpleDateFormat("yyyy");
          to = sdf.parse(toDate);
          from = sdf.parse(fromDate);
          if (to.getYear()- from.getYear()+1 > 31){//+1: include from
            return false;
          } else return true;
      }
    } catch (ParseException e) {
      e.printStackTrace();
      return false;
    }
    return false;
  }

  public static boolean validDistanceDate(String fromDate, String toDate, ProductStatisticController.Period period){
    switch (period) {
      case YEAR:
        return validDistanceDate(fromDate, toDate, RevenueStatisticController.YEAR);
      case MONTH:
        return validDistanceDate(fromDate, toDate, RevenueStatisticController.MONTH);
      case DAY:
        return validDistanceDate(fromDate, toDate, RevenueStatisticController.DAY);
      default:
        return false;
    }
  }

  private static int numberOfMonthBetweenDates(int startMonth, int startYear, int endMonth, int endYear){
    int distance = 0;
    if (endYear - startYear>1){
      distance+= (endYear - startYear-1)*12;
      distance+= endMonth+1;//real end month
      distance+= 12-(startMonth+1)+1;
    } else if (endYear- startYear ==1){
      distance+= endMonth+1;//real end month
      distance+= 12-(startMonth+1)+1;
    } else {
      distance+= endMonth- startMonth+1;
    }
    return distance;
  }
}
