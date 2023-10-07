package com.yeungjin.translogic.request.login;

import com.yeungjin.translogic.request.ThreadRequest;

public class GetSessionRequest extends ThreadRequest {
    public GetSessionRequest(String username) {
        super("GetSession");

        parameters.put("username", username);
    }
}
