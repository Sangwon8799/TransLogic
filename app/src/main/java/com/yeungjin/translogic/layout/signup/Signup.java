package com.yeungjin.translogic.layout.signup;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yeungjin.translogic.R;

public class Signup extends AppCompatActivity {
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.layout_signup_signup);

      ScrollView information = (ScrollView) findViewById(R.id.layout_signup_signup__information);
      DisplayMetrics metrics = getResources().getDisplayMetrics();
      RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                      RelativeLayout.LayoutParams.MATCH_PARENT,
                      metrics.heightPixels * 5 / 10);
      params.addRule(RelativeLayout.BELOW, R.id.layout_signup_signup__TITLE);
      information.setLayoutParams(params);

      EditText name = (EditText) findViewById(R.id.layout_signup_signup__name);
      EditText username = (EditText) findViewById(R.id.layout_signup_signup__username);
      EditText password = (EditText) findViewById(R.id.layout_signup_signup__password);
      EditText passwordConfirm = (EditText) findViewById(R.id.layout_signup_signup__password_confirm);
      EditText email = (EditText) findViewById(R.id.layout_signup_signup__email);
      EditText company = (EditText) findViewById(R.id.layout_signup_signup__company);
      EditText degree = (EditText) findViewById(R.id.layout_signup_signup__degree);

      TextView username_note = (TextView) findViewById(R.id.layout_signup_signup__username_note);
      TextView password_note = (TextView) findViewById(R.id.layout_signup_signup__password_note);
      TextView email_note = (TextView) findViewById(R.id.layout_signup_signup__email_note);

      Button submit = (Button) findViewById(R.id.layout_signup_signup__submit);
      submit.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            finish();
         }
      });
      Button cancel = (Button) findViewById(R.id.layout_signup_signup__cancel);
      cancel.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            finish();
         }
      });
   }
}
