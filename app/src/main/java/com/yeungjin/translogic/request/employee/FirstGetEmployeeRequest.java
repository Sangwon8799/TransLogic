package com.yeungjin.translogic.request.employee;

import com.yeungjin.translogic.request.ThreadRequest;

public class FirstGetEmployeeRequest extends ThreadRequest {
    public FirstGetEmployeeRequest() {
        super("GetEmployee");

        parameters.put("index", 0);
    }
}
