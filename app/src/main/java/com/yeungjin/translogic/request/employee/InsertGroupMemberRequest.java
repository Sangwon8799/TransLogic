package com.yeungjin.translogic.request.employee;

import com.yeungjin.translogic.request.Request;

public class InsertGroupMemberRequest extends Request {
    public InsertGroupMemberRequest(long group_number, long employee_number) {
        super("InsertGroupMember");

        parameters.put("group_number", String.valueOf(group_number));
        parameters.put("employee_number", String.valueOf(employee_number));
    }
}
