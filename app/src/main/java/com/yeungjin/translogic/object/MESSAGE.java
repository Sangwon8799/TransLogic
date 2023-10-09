package com.yeungjin.translogic.object;

import java.util.Date;

public class MESSAGE {
    public long MESSAGE_CHAT_NUMBER;
    public long MESSAGE_EMPLOYEE_NUMBER;
    public String MESSAGE_EMPLOYEE_NAME;
    public String MESSAGE_CONTENT;
    public Date MESSAGE_ENROLL_DATE;

    public MESSAGE(long MESSAGE_CHAT_NUMBER, long MESSAGE_EMPLOYEE_NUMBER, String MESSAGE_EMPLOYEE_NAME, String MESSAGE_CONTENT) {
        this.MESSAGE_CHAT_NUMBER = MESSAGE_CHAT_NUMBER;
        this.MESSAGE_EMPLOYEE_NUMBER = MESSAGE_EMPLOYEE_NUMBER;
        this.MESSAGE_EMPLOYEE_NAME = MESSAGE_EMPLOYEE_NAME;
        this.MESSAGE_CONTENT = MESSAGE_CONTENT;
        this.MESSAGE_ENROLL_DATE = new Date(System.currentTimeMillis());
    }
}
