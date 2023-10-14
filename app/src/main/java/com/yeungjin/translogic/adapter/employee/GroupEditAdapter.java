package com.yeungjin.translogic.adapter.employee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.CommonListAdapter;
import com.yeungjin.translogic.adapter.CommonViewHolder;
import com.yeungjin.translogic.object.EMPLOYEE_GROUP;
import com.yeungjin.translogic.server.DBVolley;
import com.yeungjin.translogic.server.DBThread;
import com.yeungjin.translogic.utility.Session;

import java.util.HashMap;

public class GroupEditAdapter extends CommonListAdapter<EMPLOYEE_GROUP, GroupEditAdapter.ViewHolder> {
    public GroupEditAdapter(@NonNull Context context) {
        super(context, new DBThread("GetGroup", new HashMap<String, Object>() {{
            put("employee_number", Session.USER.EMPLOYEE_NUMBER);
        }}));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_employee_group_edit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EMPLOYEE_GROUP group = data.get(position);

        holder.name.setText(group.EMPLOYEE_GROUP_NAME);
        holder.rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 추후 추가
            }
        });
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 추후 추가
            }
        });
    }

    @Override
    public void reload() {
        new DBVolley(context, "GetGroup", new HashMap<String, Object>() {{
            put("employee_number", Session.USER.EMPLOYEE_NUMBER);
        }}, new ReloadListener());
    }

    @Override
    public void load() {
        reload();
    }

    public static class ViewHolder extends CommonViewHolder {
        public TextView name;
        public ImageButton rename;
        public ImageButton remove;

        public ViewHolder(View view) {
            super(view);
        }

        @Override
        protected void setId() {
            name = view.findViewById(R.id.adapter_employee_group_edit__name);
            rename = view.findViewById(R.id.adapter_employee_group_edit__rename);
            remove = view.findViewById(R.id.adapter_employee_group_edit__remove);
        }
    }
}
