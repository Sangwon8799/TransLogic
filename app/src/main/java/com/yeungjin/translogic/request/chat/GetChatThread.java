package com.yeungjin.translogic.request.chat;

import com.yeungjin.translogic.request.ThreadRequest;

public class GetChatThread extends ThreadRequest {
    public GetChatThread(long employee_number) {
        super("GetChat");

        parameters.put("employee_number", employee_number);
        parameters.put("index", 0);
    }
}
