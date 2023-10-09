package com.yeungjin.translogic.request.chat;

import com.yeungjin.translogic.object.MESSAGE;
import com.yeungjin.translogic.request.Request;

public class CreateMessageRequest extends Request {
    public CreateMessageRequest(MESSAGE message) {
        super("CreateMessage");

        parameters.put("chat_number", String.valueOf(message.MESSAGE_CHAT_NUMBER));
        parameters.put("employee_number", String.valueOf(message.MESSAGE_EMPLOYEE_NUMBER));
        parameters.put("employee_name", message.MESSAGE_EMPLOYEE_NAME);
        parameters.put("content", message.MESSAGE_CONTENT);
    }
}
