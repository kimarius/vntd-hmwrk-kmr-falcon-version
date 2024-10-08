package com.falcon.shipping.utl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateFormatterUtil {
  public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public static String formatDate(LocalDate date) {
    return date.format(DEFAULT_DATE_FORMATTER);
  }
}
