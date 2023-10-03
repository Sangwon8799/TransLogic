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
import com.yeungjin.translogic.adapter.CommonAdapter;
import com.yeungjin.translogic.adapter.CommonViewHolder;

public class EmployeeAdapter extends CommonAdapter<EmployeeAdapter.ViewHolder> {
    public EmployeeAdapter(Context context) {
        super(context);
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
    public void reload(CharSequence content) {

    }

    @Override
    public void load(CharSequence content) {

    }

    @Override
    public int getResponse(String response) throws Exception {
        return 0;
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
