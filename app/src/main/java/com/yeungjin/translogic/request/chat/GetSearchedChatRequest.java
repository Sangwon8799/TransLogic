package com.yeungjin.translogic.request.chat;

import com.android.volley.Response;
import com.yeungjin.translogic.request.Request;

public class GetSearchedChatRequest extends Request {
    public GetSearchedChatRequest(long employee_number, long index, String search, Response.Listener<String> listener) {
        super("GetSearchedChat", listener);

        parameters.put("employee_number", String.valueOf(employee_number));
        parameters.put("index", String.valueOf(index));
        parameters.put("search", search);
    }
}
