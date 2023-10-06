package com.yeungjin.translogic.request.chat;

import com.android.volley.Response;
import com.yeungjin.translogic.request.Request;

public class GetChatRequest extends Request {
    public GetChatRequest(long index, Response.Listener<String> listener) {
        super("GetChat", listener);

        parameters.put("index", String.valueOf(index));
    }
}
