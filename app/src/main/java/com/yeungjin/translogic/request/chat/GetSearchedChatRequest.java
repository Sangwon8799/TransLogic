package com.yeungjin.translogic.request.chat;

import com.android.volley.Response;
import com.yeungjin.translogic.request.Request;

public class GetSearchedChatRequest extends Request {
    public GetSearchedChatRequest(String content, int index, Response.Listener<String> listener) {
        super("GetSearchedChat.db", listener);

        parameters.put("content", content);
        parameters.put("index", String.valueOf(index));
    }
}
