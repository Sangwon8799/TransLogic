package com.yeungjin.translogic.layout;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.yeungjin.translogic.utility.Session;

import java.util.Objects;

public abstract class CommonDialog extends Dialog {
    protected final Context context;

    public CommonDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    protected void init(int width, int height) {
        setId();
        setAdapter();
        setListener();
        setWindow(width, height);
    }

    protected abstract void setId();

    protected void setAdapter() { }

    protected abstract void setListener();

    protected void setWindow(int width, int height) {
        WindowManager.LayoutParams params = Objects.requireNonNull(getWindow()).getAttributes();
        switch (width) {
            case WindowManager.LayoutParams.MATCH_PARENT:
            case WindowManager.LayoutParams.WRAP_CONTENT:
                params.width = width;
                break;
            default:
                params.width = Session.width * width / 10;
        }
        switch (height) {
            case WindowManager.LayoutParams.MATCH_PARENT:
            case WindowManager.LayoutParams.WRAP_CONTENT:
                params.height = height;
                break;
            default:
                params.height = Session.height * height / 10;
        }
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
