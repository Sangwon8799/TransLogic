package com.yeungjin.translogic.request.employee;

import com.yeungjin.translogic.request.ThreadRequest;

public class GetEmployeeThread extends ThreadRequest {
    public GetEmployeeThread() {
        super("GetEmployee");

        parameters.put("index", 0);
    }
}
