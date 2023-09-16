package com.yeungjin.translogic.request.employee;

import com.android.volley.Response;
import com.yeungjin.translogic.request.Request;

public class GetSearchedEmployeeRequest extends Request {
    public GetSearchedEmployeeRequest(String content, int index, Response.Listener<String> listener) {
        super("GetSearchedEmployee.db", listener);

        parameters.put("content", content);
        parameters.put("index", String.valueOf(index));
    }
}
