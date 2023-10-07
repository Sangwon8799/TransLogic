package com.yeungjin.translogic.request.chat;

import com.yeungjin.translogic.request.ThreadRequest;

public class FirstGetChatRequest extends ThreadRequest {
    public FirstGetChatRequest() {
        super("GetChat");

        parameters.put("index", 0);
    }
}
