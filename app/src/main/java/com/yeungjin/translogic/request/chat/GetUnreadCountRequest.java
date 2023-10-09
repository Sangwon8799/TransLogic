package com.yeungjin.translogic.request.chat;

import com.android.volley.Response;
import com.yeungjin.translogic.request.Request;

public class GetUnreadCountRequest extends Request {
    public GetUnreadCountRequest(long chat_number, long employee_number, Response.Listener<String> listener) {
        super("GetUnreadCount", listener);

        parameters.put("chat_number", String.valueOf(chat_number));
        parameters.put("employee_number", String.valueOf(employee_number));
    }
}
