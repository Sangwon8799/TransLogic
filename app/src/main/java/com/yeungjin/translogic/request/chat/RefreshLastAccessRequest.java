package com.yeungjin.translogic.request.chat;

import com.yeungjin.translogic.request.Request;

public class RefreshLastAccessRequest extends Request {
    public RefreshLastAccessRequest(long chat_number, long employee_number) {
        super("RefreshLastAccess");

        parameters.put("chat_number", String.valueOf(chat_number));
        parameters.put("employee_number", String.valueOf(employee_number));
    }
}
