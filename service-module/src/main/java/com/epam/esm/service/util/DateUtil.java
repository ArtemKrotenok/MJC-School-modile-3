package com.epam.esm.service.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class DateUtil {

    public static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    public static String convert(Timestamp date) {
        TimeZone tz = TimeZone.getTimeZone(TimeZone.getDefault().toZoneId());
        DateFormat df = new SimpleDateFormat(DATE_PATTERN);
        df.setTimeZone(tz);
        return df.format(date);
    }

    public static Timestamp convert(String date) {
        return Timestamp.valueOf(LocalDateTime.parse(date));
    }

    public static Timestamp getNow() {
        return Timestamp.valueOf(LocalDateTime.now());
    }
}
