package com.yeungjin.translogic.request.employee;

import com.android.volley.Response;
import com.yeungjin.translogic.request.Request;

public class GetGroupedSearchedEmployeeRequest extends Request {
    public GetGroupedSearchedEmployeeRequest(long group_number, long index, String search, Response.Listener<String> listener) {
        super("GetGroupedSearchedEmployee", listener);

        parameters.put("group_number", String.valueOf(group_number));
        parameters.put("index", String.valueOf(index));
        parameters.put("search", search);
    }
}
