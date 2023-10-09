package com.yeungjin.translogic.request.chat;

import com.yeungjin.translogic.request.Request;

public class InsertChatMemberRequest extends Request {
    public InsertChatMemberRequest(long chat_number, long employee_number) {
        super("InsertChatMember");

        parameters.put("chat_number", String.valueOf(chat_number));
        parameters.put("employee_number", String.valueOf(employee_number));
    }
}
