package com.yeungjin.translogic.layout.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yeungjin.translogic.R;
import com.yeungjin.translogic.layout.find_account.FindAccount;
import com.yeungjin.translogic.layout.main.Main;
import com.yeungjin.translogic.layout.signup.Signup;

public class Login extends AppCompatActivity {
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.layout_login_login);

      EditText username = (EditText) findViewById(R.id.layout_login_login__username);
      EditText password = (EditText) findViewById(R.id.layout_login_login__password);

      TextView findAccount = (TextView) findViewById(R.id.layout_login_login__find_account);
      findAccount.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), FindAccount.class);
            startActivity(intent);
         }
      });

      Button login = (Button) findViewById(R.id.layout_login_login__login);
      login.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), Main.class);
            startActivity(intent);
            finish();
         }
      });
      Button signup = (Button) findViewById(R.id.layout_login_login__signup);
      signup.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), Signup.class);
            startActivity(intent);
         }
      });
   }
}
