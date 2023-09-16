package com.yeungjin.translogic.adapter.employee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.yeungjin.translogic.R;
import com.yeungjin.translogic.object.database.EMPLOYEE;
import com.yeungjin.translogic.request.Request;
import com.yeungjin.translogic.request.employee.GetEmployeeRequest;
import com.yeungjin.translogic.request.employee.GetSearchedEmployeeRequest;
import com.yeungjin.translogic.utility.DateFormat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {
    private Context context;
    private int index;

    private ArrayList<EMPLOYEE> data;

    public EmployeeAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    public void getEmployee(int index) {
        load(new GetEmployeeRequest(index, new Listener()), index);
    }

    public void getSearchedEmployee(String content, int index) {
        load(new GetSearchedEmployeeRequest(content, index, new Listener()), index);
    }

    private void load(Request request, int index) {
        this.index = index;

        Request.queue = Volley.newRequestQueue(context);
        Request.queue.add(request);
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

        holder.name.setText(employee.EMPLOYEE_NAME);
        holder.contactNumber.setText(employee.EMPLOYEE_CONTACT_NUMBER);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;
        public TextView company;
        public TextView contactNumber;

        public ViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.adapter_employee_employee__image);
            name = (TextView) view.findViewById(R.id.adapter_employee_employee__name);
            company = (TextView) view.findViewById(R.id.adapter_employee_employee__company);
            contactNumber = (TextView) view.findViewById(R.id.adapter_employee_employee__contact_number);
        }
    }

    private class Listener implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
            if (index == 0) {
                data = new ArrayList<>();
            }

            try {
                JSONObject json = new JSONObject(response);

                JSONObject object;
                JSONArray array;

                array = json.getJSONArray("employee");
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
                    employee.EMPLOYEE_REGISTER_DATE = DateFormat.getInstance().DATE.parse(object.getString("EMPLOYEE_REGISTER_DATE"));

                    data.add(employee);
                }
            } catch (Exception error) {
                error.printStackTrace();
            }

            if (index == 0) {
                notifyDataSetChanged();
            } else {
                notifyItemRangeInserted(index * 20, 20);
            }
        }
    }
}
