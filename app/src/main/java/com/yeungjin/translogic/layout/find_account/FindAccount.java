package com.yeungjin.translogic.layout.find_account;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yeungjin.translogic.R;

public class FindAccount extends AppCompatActivity {
   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.layout_find_account_find_account);

      Button username = (Button) findViewById(R.id.layout_find_account_find_account__username);
      username.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            // 추후 내용 추가
         }
      });

      Button password = (Button) findViewById(R.id.layout_find_account_find_account__password);
      password.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            // 추후 내용 추가
         }
      });
   }
}
