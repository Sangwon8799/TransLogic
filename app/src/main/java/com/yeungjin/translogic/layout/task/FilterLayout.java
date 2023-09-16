package com.yeungjin.translogic.layout.task;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.yeungjin.translogic.R;

public class FilterLayout extends Dialog {
    public FilterLayout(@NonNull Context context, int width, int height) {
        super(context);
        setContentView(R.layout.layout_task_filter);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = width * 9 / 10;
        params.height = height * 9 / 10;
        getWindow().setAttributes(params);
    }
}
