package com.yeungjin.translogic.request.chat;

import com.android.volley.Response;
import com.yeungjin.translogic.request.Request;

public class CreateChatRequest extends Request {
    public CreateChatRequest(String title, long employee_number, Response.Listener<String> listener) {
        super("CreateChat", listener);

        parameters.put("title", title);
        parameters.put("employee_number", String.valueOf(employee_number));
    }
}
