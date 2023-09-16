package com.yeungjin.translogic.request.session;

import com.android.volley.Response;
import com.yeungjin.translogic.request.Request;

public class GetSession extends Request {
    public GetSession(String username, Response.Listener<String> listener) {
        super("GetSession.db", listener);

        parameters.put("username", username);
    }
}
