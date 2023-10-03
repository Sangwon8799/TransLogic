package com.yeungjin.translogic.utility;

import com.yeungjin.translogic.object.database.CHAT;
import com.yeungjin.translogic.object.database.EMPLOYEE;

public class Session {
    public static EMPLOYEE user = new EMPLOYEE();
    public static CHAT chat = new CHAT();

    private Session() { }
}
