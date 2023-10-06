package com.yeungjin.translogic.request.chat;

import com.android.volley.Response;
import com.yeungjin.translogic.request.Request;

public class GetSearchedChatRequest extends Request {
    public GetSearchedChatRequest(long index, String search, Response.Listener<String> listener) {
        super("GetSearchedChat", listener);

        parameters.put("index", String.valueOf(index));
        parameters.put("search", search);
    }
}
