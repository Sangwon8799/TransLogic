package com.yeungjin.translogic.adapter.task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.CommonListAdapter;
import com.yeungjin.translogic.adapter.CommonViewHolder;
import com.yeungjin.translogic.object.EMPLOYEE;
import com.yeungjin.translogic.request.employee.GetEmployeeThread;

public class EmployeeAdapter extends CommonListAdapter<EMPLOYEE, EmployeeAdapter.ViewHolder> {
    public EmployeeAdapter(Context context) {
        super(context, new GetEmployeeThread());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_task_employee, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getResponse(String response) throws Exception {
        return 0;
    }

    @Override
    public void reload() {

    }

    @Override
    public void load() {

    }

    public static class ViewHolder extends CommonViewHolder {
        public ImageView image;
        public TextView name;
        public TextView company;
        public RecyclerView content;

        public ViewHolder(View view) {
            super(view);
            init();

            content.setLayoutManager(new LinearLayoutManager(view.getContext()));
        }

        @Override
        protected void setId() {
            image = view.findViewById(R.id.adapter_task_employee__image);
            name = view.findViewById(R.id.adapter_task_employee__name);
            company = view.findViewById(R.id.adapter_task_employee__company);
            content = view.findViewById(R.id.adapter_task_employee__content);
        }
    }
}
