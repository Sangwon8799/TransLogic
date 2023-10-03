package com.yeungjin.translogic.adapter.employee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.CommonAdapter;
import com.yeungjin.translogic.adapter.CommonViewHolder;
import com.yeungjin.translogic.object.database.EMPLOYEE;
import com.yeungjin.translogic.request.Request;
import com.yeungjin.translogic.request.employee.GetEmployeeRequest;
import com.yeungjin.translogic.request.employee.GetSearchedEmployeeRequest;
import com.yeungjin.translogic.utility.DateFormat;

import org.json.JSONArray;
import org.json.JSONObject;

public class GroupUnselectedAdapter extends CommonAdapter<GroupUnselectedAdapter.ViewHolder> {
    public GroupUnselectedAdapter(Context context) {
        super(context);
    }

    @Override
    public void reload(CharSequence content) {
        Request request;
        if (content == null) {
            request = new GetEmployeeRequest(0, new ReloadListener());
        } else {
            request = new GetSearchedEmployeeRequest(content.toString(), 0, new ReloadListener());
        }
        Request.sendRequest(context, request);
    }

    @Override
    public void load(CharSequence content) {
        Request request;
        if (content == null) {
            request = new GetEmployeeRequest(data.size(), new LoadListener());
        } else {
            request = new GetSearchedEmployeeRequest(content.toString(), data.size(), new LoadListener());
        }
        Request.sendRequest(context, request);
    }

    @Override
    protected int getResponse(String response) throws Exception {
        JSONObject http = new JSONObject(response);
        JSONObject object;
        JSONArray array;

        array = http.getJSONArray("employee");
        for (int step = 0; step < array.length(); step++) {
            object = array.getJSONObject(step);

            EMPLOYEE employee = new EMPLOYEE();
            employee.EMPLOYEE_NUMBER = object.getLong("EMPLOYEE_NUMBER");
            employee.EMPLOYEE_NAME = object.getString("EMPLOYEE_NAME");
            employee.EMPLOYEE_USERNAME = object.getString("EMPLOYEE_USERNAME");
            employee.EMPLOYEE_PASSWORD = object.getString("EMPLOYEE_PASSWORD");
            employee.EMPLOYEE_CONTACT_NUMBER = object.getString("EMPLOYEE_CONTACT_NUMBER");
            employee.EMPLOYEE_EMAIL = object.getString("EMPLOYEE_EMAIL");
            employee.EMPLOYEE_COMPANY_NUMBER = object.getLong("EMPLOYEE_COMPANY_NUMBER");
            employee.EMPLOYEE_DEGREE = object.getString("EMPLOYEE_DEGREE");
            employee.EMPLOYEE_IMAGE = object.getString("EMPLOYEE_IMAGE");
            employee.EMPLOYEE_REGISTER_DATE = DateFormat.DATE.parse(object.getString("EMPLOYEE_REGISTER_DATE"));

            data.add(employee);
        }

        return array.length();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_employee_group_unselected, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EMPLOYEE employee = (EMPLOYEE) data.get(position);

        holder.name.setText(employee.EMPLOYEE_NAME);
        holder.contactNumber.setText(employee.EMPLOYEE_CONTACT_NUMBER);
    }

    public static class ViewHolder extends CommonViewHolder {
        public CheckBox checkbox;
        public ImageView image;
        public TextView name;
        public TextView company;
        public TextView contactNumber;

        public ViewHolder(View view) {
            super(view);
            init();
        }

        @Override
        protected void setId() {
            checkbox = view.findViewById(R.id.adapter_employee_group_unselected__checkbox);
            image = view.findViewById(R.id.adapter_employee_group_unselected__image);
            name = view.findViewById(R.id.adapter_employee_group_unselected__name);
            company = view.findViewById(R.id.adapter_employee_group_unselected__company);
            contactNumber = view.findViewById(R.id.adapter_employee_group_unselected__contact_number);
        }
    }
}
