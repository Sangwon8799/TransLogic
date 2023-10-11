package com.yeungjin.translogic.layout;

import androidx.appcompat.app.AppCompatActivity;

public abstract class CommonActivity extends AppCompatActivity {
    protected void init() {
        setId();
        setAdapter();
        setListener();
    }

    protected abstract void setId();

    protected void setAdapter() { }

    protected abstract void setListener();
}
