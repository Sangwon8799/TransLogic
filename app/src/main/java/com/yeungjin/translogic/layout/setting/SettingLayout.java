package com.yeungjin.translogic.layout.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yeungjin.translogic.R;
import com.yeungjin.translogic.layout.CommonFragment;

public class SettingLayout extends CommonFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_setting_setting, container, false);
        init();

        return view;
    }

    @Override
    protected void setId() {

    }

    @Override
    protected void setListener() {

    }
}
