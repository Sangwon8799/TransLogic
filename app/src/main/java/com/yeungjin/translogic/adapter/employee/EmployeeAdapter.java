package com.yeungjin.translogic.adapter.employee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.CommonListAdapter;
import com.yeungjin.translogic.adapter.CommonViewHolder;
import com.yeungjin.translogic.object.EMPLOYEE;
import com.yeungjin.translogic.server.DBVolley;
import com.yeungjin.translogic.server.DBThread;
import com.yeungjin.translogic.utility.ContactNumber;
import com.yeungjin.translogic.server.Server;
import com.yeungjin.translogic.utility.Session;

import java.util.HashMap;

public class EmployeeAdapter extends CommonListAdapter<EMPLOYEE, EmployeeAdapter.ViewHolder> {
    public EmployeeAdapter(@NonNull Context context) {
        super(context, new DBThread("GetEmployee", new HashMap<String, Object>() {{
            put("employee_number", Session.USER.EMPLOYEE_NUMBER);
            put("index", 0);
        }}));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_employee_employee, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EMPLOYEE employee = data.get(position);

        Glide.with(holder.image.getContext()).load(Server.IMAGE_URL + employee.EMPLOYEE_IMAGE).into(holder.image);
        holder.name.setText(employee.EMPLOYEE_NAME);
        holder.contact_number.setText(ContactNumber.parse(employee.EMPLOYEE_CONTACT_NUMBER));
    }

    @Override
    public void reload() {
        new DBVolley(context, "GetEmployee", new HashMap<String, Object>() {{
            put("employee_number", Session.USER.EMPLOYEE_NUMBER);
            put("index", 0);
        }}, new ReloadListener());
    }

    @Override
    public void load() {
        new DBVolley(context, "GetEmployee", new HashMap<String, Object>() {{
            put("employee_number", Session.USER.EMPLOYEE_NUMBER);
            put("index", data.size());
        }}, new LoadListener());
    }

    public void reload(CharSequence search) {
        new DBVolley(context, "GetEmployee", new HashMap<String, Object>() {{
            put("employee_number", Session.USER.EMPLOYEE_NUMBER);
            put("index", 0);
            put("search", search);
        }}, new ReloadListener());
    }

    public void load(CharSequence search) {
        new DBVolley(context, "GetEmployee", new HashMap<String, Object>() {{
            put("employee_number", Session.USER.EMPLOYEE_NUMBER);
            put("index", data.size());
            put("search", search);
        }}, new LoadListener());
    }

    public void reload(long group_number) {
        new DBVolley(context, "GetEmployee", new HashMap<String, Object>() {{
            put("group_number", group_number);
            put("index", 0);
        }}, new ReloadListener());
    }

    public void load(long group_number) {
        new DBVolley(context, "GetEmployee", new HashMap<String, Object>() {{
            put("group_number", group_number);
            put("index", data.size());
        }}, new LoadListener());
    }

    public void reload(long group_number, CharSequence search) {
        new DBVolley(context, "GetEmployee", new HashMap<String, Object>() {{
            put("group_number", group_number);
            put("index", 0);
            put("search", search);
        }}, new ReloadListener());
    }

    public void load(long group_number, CharSequence search) {
        new DBVolley(context, "GetEmployee", new HashMap<String, Object>() {{
            put("group_number", group_number);
            put("index", data.size());
            put("search", search);
        }}, new LoadListener());
    }

    public static class ViewHolder extends CommonViewHolder {
        public ImageView image;
        public TextView name;
        public TextView company;
        public TextView contact_number;

        public ViewHolder(@NonNull View view) {
            super(view);

            image.setClipToOutline(true);
        }

        @Override
        protected void setId() {
            image = view.findViewById(R.id.adapter_employee_employee__image);
            name = view.findViewById(R.id.adapter_employee_employee__name);
            company = view.findViewById(R.id.adapter_employee_employee__company);
            contact_number = view.findViewById(R.id.adapter_employee_employee__contact_number);
        }
    }
}
