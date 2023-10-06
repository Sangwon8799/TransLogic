package com.yeungjin.translogic.request.employee;

import com.android.volley.Response;
import com.yeungjin.translogic.request.Request;

public class ChangeGroupNameRequest extends Request {
    public ChangeGroupNameRequest(long group_number, String name, Response.Listener<String> listener) {
        super("ChangeGroupName", listener);

        parameters.put("group_number", String.valueOf(group_number));
        parameters.put("name", name);
    }
}
