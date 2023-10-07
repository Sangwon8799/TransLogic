package com.yeungjin.translogic.layout.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yeungjin.translogic.R;
import com.yeungjin.translogic.layout.CommonFragment;
import com.yeungjin.translogic.utility.Server;
import com.yeungjin.translogic.utility.ContactNumber;
import com.yeungjin.translogic.utility.Session;

public class SettingLayout extends CommonFragment {
    private ImageView image;
    private TextView name;
    private TextView username;
    private TextView company;
    private TextView contactNumber;
    private TextView email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_setting_setting, container, false);
        init();

        Glide.with(image.getContext()).load(Server.ImageURL + Session.user.EMPLOYEE_IMAGE).into(image);
        name.setText(Session.user.EMPLOYEE_NAME);
        username.setText(Session.user.EMPLOYEE_USERNAME);
        contactNumber.setText(ContactNumber.parse(Session.user.EMPLOYEE_CONTACT_NUMBER));
        email.setText(Session.user.EMPLOYEE_EMAIL);

        image.setClipToOutline(true);

        return view;
    }

    @Override
    protected void setId() {
        image = view.findViewById(R.id.layout_setting_setting__image);
        name = view.findViewById(R.id.layout_setting_setting__name);
        username = view.findViewById(R.id.layout_setting_setting__username);
        company = view.findViewById(R.id.layout_setting_setting__company);
        contactNumber = view.findViewById(R.id.layout_setting_setting__contact_number);
        email = view.findViewById(R.id.layout_setting_setting__email);
    }

    @Override
    protected void setListener() {

    }
}
