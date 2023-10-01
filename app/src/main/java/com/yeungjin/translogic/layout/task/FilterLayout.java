package com.yeungjin.translogic.layout.task;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.yeungjin.translogic.R;

public class FilterLayout extends Dialog {
    public FilterLayout(@NonNull Context context, int width, int height) {
        super(context);
        setContentView(R.layout.layout_task_filter);
        init(width, height);


    }

    private void init(int width, int height) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = width * 9 / 10;
        params.height = height * 8 / 10;
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
