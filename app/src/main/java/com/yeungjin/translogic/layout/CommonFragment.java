package com.yeungjin.translogic.layout;

import android.view.View;

import androidx.fragment.app.Fragment;

public abstract class CommonFragment extends Fragment {
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
