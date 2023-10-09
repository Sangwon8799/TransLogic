package com.yeungjin.translogic.request.chat;

import com.yeungjin.translogic.request.ThreadRequest;

public class GetMessageThread extends ThreadRequest {
    public GetMessageThread(long chat_number) {
        super("GetMessage");

        parameters.put("chat_number", chat_number);
        parameters.put("index", 0);
    }
}
