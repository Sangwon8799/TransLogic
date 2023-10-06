package com.yeungjin.translogic.request.employee;

import com.android.volley.Response;
import com.yeungjin.translogic.request.Request;

public class GetGroupedEmployeeRequest extends Request {
    public GetGroupedEmployeeRequest(long group_number, long index, Response.Listener<String> listener) {
        super("GetGroupedEmployee", listener);

        parameters.put("group_number", String.valueOf(group_number));
        parameters.put("index", String.valueOf(index));
    }
}
