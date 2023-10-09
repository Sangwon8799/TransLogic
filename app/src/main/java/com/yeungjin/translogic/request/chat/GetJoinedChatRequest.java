package com.yeungjin.translogic.request.chat;

import com.yeungjin.translogic.request.ThreadRequest;

public class GetJoinedChatRequest extends ThreadRequest {
    public GetJoinedChatRequest(long employee_number) {
        super("GetJoinedChat");

        parameters.put("employee_number", employee_number);
    }
}
