package com.yeungjin.translogic.request.chat;

import com.android.volley.Response;
import com.yeungjin.translogic.request.Request;

public class GetMessageRequest extends Request {
    public GetMessageRequest(long chat_number, long index, Response.Listener<String> listener) {
        super("GetMessage", listener);

        parameters.put("chat_number", String.valueOf(chat_number));
        parameters.put("index", String.valueOf(index));
    }
}
