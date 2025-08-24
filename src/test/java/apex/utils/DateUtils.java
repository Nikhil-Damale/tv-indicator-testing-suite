package apex.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateUtils {
  // Market in US/Eastern; adjust to previous business day
  public static String yesterdayYYYYMMDD() {
    ZoneId ny = ZoneId.of("America/New_York");
    LocalDate d = LocalDate.now(ny).minusDays(1);
    // Skip weekends
    while (d.getDayOfWeek()==DayOfWeek.SATURDAY || d.getDayOfWeek()==DayOfWeek.SUNDAY) {
      d = d.minusDays(1);
    }
    return d.format(DateTimeFormatter.ISO_LOCAL_DATE);
  }
}
