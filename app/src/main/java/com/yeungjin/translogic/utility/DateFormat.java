package com.yeungjin.translogic.utility;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateFormat {
    public static final SimpleDateFormat DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
    public static final SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
    public static final SimpleDateFormat TIME = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);

    private DateFormat() { }
}
