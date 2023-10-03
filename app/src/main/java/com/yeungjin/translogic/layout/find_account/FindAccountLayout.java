package com.yeungjin.translogic.layout.find_account;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.yeungjin.translogic.R;
import com.yeungjin.translogic.layout.CommonActivity;

public class FindAccountLayout extends CommonActivity {
    private Button username;
    private Button password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_find_account_find_account);
        init();
    }

    @Override
    protected void setId() {
        username = findViewById(R.id.layout_find_account_find_account__username);
        password = findViewById(R.id.layout_find_account_find_account__password);
    }

    @Override
    protected void setListener() {
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 추후 내용 추가
            }
        });
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 추후 내용 추가
            }
        });
    }
}
