package com.yeungjin.translogic.layout;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class CommonActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void init() {
        setId();
        setAdapter();
        setListener();
    }

    protected abstract void setId();

    protected void setAdapter() { }

    protected abstract void setListener();
}
