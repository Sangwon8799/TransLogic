package com.yeungjin.translogic.request.employee;

import com.yeungjin.translogic.request.ThreadRequest;

public class GetGroupThread extends ThreadRequest {
    public GetGroupThread(long employee_number) {
        super("GetGroup");

        parameters.put("employee_number", employee_number);
    }
}
