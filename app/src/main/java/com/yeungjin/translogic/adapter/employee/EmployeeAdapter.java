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
import com.yeungjin.translogic.request.Request;
import com.yeungjin.translogic.request.employee.GetEmployeeThread;
import com.yeungjin.translogic.request.employee.GetEmployeeRequest;
import com.yeungjin.translogic.request.employee.GetGroupedEmployeeRequest;
import com.yeungjin.translogic.request.employee.GetGroupedSearchedEmployeeRequest;
import com.yeungjin.translogic.request.employee.GetSearchedEmployeeRequest;
import com.yeungjin.translogic.utility.ContactNumber;
import com.yeungjin.translogic.utility.Json;
import com.yeungjin.translogic.utility.Server;

import org.json.JSONObject;

public class EmployeeAdapter extends CommonListAdapter<EMPLOYEE, EmployeeAdapter.ViewHolder> {
    public EmployeeAdapter(Context context) {
        super(context, new GetEmployeeThread());
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

        Glide.with(holder.image.getContext()).load(Server.ImageURL + employee.EMPLOYEE_IMAGE).into(holder.image);
        holder.name.setText(employee.EMPLOYEE_NAME);
        holder.contactNumber.setText(ContactNumber.parse(employee.EMPLOYEE_CONTACT_NUMBER));
    }

    @Override
    protected int getResponse(String response) throws Exception {
        array = new JSONObject(response).getJSONArray("employee");
        for (int index = 0; index < array.length(); index++) {
            object = array.getJSONObject(index);
            data.add(Json.from(object, EMPLOYEE.class));
        }

        return array.length();
    }

    @Override
    public void reload() {
        Request request = new GetEmployeeRequest(0, new ReloadListener());
        Request.sendRequest(context, request);
    }

    @Override
    public void load() {
        Request request = new GetEmployeeRequest(data.size(), new LoadListener());
        Request.sendRequest(context, request);
    }

    public void reload(CharSequence search) {
        Request request = new GetSearchedEmployeeRequest(0, search.toString(), new ReloadListener());
        Request.sendRequest(context, request);
    }

    public void load(CharSequence search) {
        Request request = new GetSearchedEmployeeRequest(data.size(), search.toString(), new LoadListener());
        Request.sendRequest(context, request);
    }

    public void reload(long group_number) {
        Request request = new GetGroupedEmployeeRequest(group_number, 0, new ReloadListener());
        Request.sendRequest(context, request);
    }

    public void load(long group_number) {
        Request request = new GetGroupedEmployeeRequest(group_number, data.size(), new LoadListener());
        Request.sendRequest(context, request);
    }

    public void reload(long group_number, CharSequence search) {
        Request request = new GetGroupedSearchedEmployeeRequest(group_number, 0, search.toString(), new ReloadListener());
        Request.sendRequest(context, request);
    }

    public void load(long group_number, CharSequence search) {
        Request request = new GetGroupedSearchedEmployeeRequest(group_number, data.size(), search.toString(), new LoadListener());
        Request.sendRequest(context, request);
    }

    public static class ViewHolder extends CommonViewHolder {
        public ImageView image;
        public TextView name;
        public TextView company;
        public TextView contactNumber;

        public ViewHolder(View view) {
            super(view);
            init();

            image.setClipToOutline(true);
        }

        @Override
        protected void setId() {
            image = view.findViewById(R.id.adapter_employee_employee__image);
            name = view.findViewById(R.id.adapter_employee_employee__name);
            company = view.findViewById(R.id.adapter_employee_employee__company);
            contactNumber = view.findViewById(R.id.adapter_employee_employee__contact_number);
        }
    }
}
