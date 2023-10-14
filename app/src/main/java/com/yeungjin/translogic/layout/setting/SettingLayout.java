package com.yeungjin.translogic.layout.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yeungjin.translogic.R;
import com.yeungjin.translogic.layout.CommonFragment;
import com.yeungjin.translogic.layout.login.LoginLayout;
import com.yeungjin.translogic.utility.ContactNumber;
import com.yeungjin.translogic.server.Server;
import com.yeungjin.translogic.utility.Session;

public class SettingLayout extends CommonFragment {
    private ImageView image;
    private TextView name;
    private TextView username;
    private TextView company;
    private TextView contact_number;
    private TextView email;
    private LinearLayout logout;

    public Listener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_setting_setting, container, false);
        init();

        Glide.with(image.getContext()).load(Server.IMAGE_URL + Session.USER.EMPLOYEE_IMAGE).into(image);
        name.setText(Session.USER.EMPLOYEE_NAME);
        username.setText(Session.USER.EMPLOYEE_USERNAME);
        contact_number.setText(ContactNumber.parse(Session.USER.EMPLOYEE_CONTACT_NUMBER));
        email.setText(Session.USER.EMPLOYEE_EMAIL);

        image.setClipToOutline(true);

        return view;
    }

    @Override
    protected void setId() {
        image = view.findViewById(R.id.layout_setting_setting__image);
        name = view.findViewById(R.id.layout_setting_setting__name);
        username = view.findViewById(R.id.layout_setting_setting__username);
        company = view.findViewById(R.id.layout_setting_setting__company);
        contact_number = view.findViewById(R.id.layout_setting_setting__contact_number);
        email = view.findViewById(R.id.layout_setting_setting__email);
        logout = view.findViewById(R.id.layout_setting_setting__logout);
    }

    @Override
    protected void setListener() {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginLayout.class);
                intent.putExtra("logout", true);
                startActivity(intent);

                if (listener != null) {
                    listener.exit();
                }
            }
        });
    }

    public interface Listener {
        void exit();
    }
}
