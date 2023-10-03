package com.yeungjin.translogic.layout;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

public abstract class CommonDialog extends Dialog {
    protected final Context context;

    public CommonDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    protected void init() {
        setId();
        setListener();
    }

    protected abstract void setId();

    protected abstract void setListener();
}
