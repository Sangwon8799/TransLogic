package com.yeungjin.translogic.layout.task;

import android.content.Context;

import androidx.annotation.NonNull;

import com.yeungjin.translogic.R;
import com.yeungjin.translogic.layout.CommonDialog;

public class FilterLayout extends CommonDialog {
    public FilterLayout(@NonNull Context context) {
        super(context);
        setContentView(R.layout.layout_task_filter);
        init(9, 8);
    }

    @Override
    protected void setId() {

    }

    @Override
    protected void setListener() {

    }
}
