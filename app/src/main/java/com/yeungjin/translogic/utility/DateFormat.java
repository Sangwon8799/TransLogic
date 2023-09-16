package com.yeungjin.translogic.utility;

import java.text.SimpleDateFormat;

public class DateFormat {
    private static final DateFormat format = new DateFormat();

    public final SimpleDateFormat DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public final SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd");
    public final SimpleDateFormat TIME = new SimpleDateFormat("HH:mm:ss");

    private DateFormat() {
    }

    public static DateFormat getInstance() {
        return format;
    }
}
