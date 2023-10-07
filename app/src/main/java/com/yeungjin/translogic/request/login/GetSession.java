package com.yeungjin.translogic.request.login;

import com.yeungjin.translogic.request.ThreadRequest;

public class GetSession extends ThreadRequest {
    public GetSession(String username) {
        super("GetSession");

        parameters.put("username", username);
    }
}
