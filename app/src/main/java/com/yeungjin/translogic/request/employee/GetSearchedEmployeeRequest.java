package com.yeungjin.translogic.request.employee;

import com.android.volley.Response;
import com.yeungjin.translogic.request.Request;

public class GetSearchedEmployeeRequest extends Request {
    public GetSearchedEmployeeRequest(long index, String search, Response.Listener<String> listener) {
        super("GetSearchedEmployee", listener);

        parameters.put("index", String.valueOf(index));
        parameters.put("search", search);
    }
}
