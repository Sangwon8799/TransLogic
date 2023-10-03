package com.yeungjin.translogic.layout;

import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public abstract class CommonBottomSheetDialogFragment extends BottomSheetDialogFragment {
    protected View view;

    protected void init() {
        setId();
        setAdapter();
        setListener();
    }

    protected abstract void setId();

    protected void setAdapter() { }

    protected abstract void setListener();
}
