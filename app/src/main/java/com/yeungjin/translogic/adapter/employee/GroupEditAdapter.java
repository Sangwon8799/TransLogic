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
import com.yeungjin.translogic.request.Request;
import com.yeungjin.translogic.request.employee.GetGroupRequest;
import com.yeungjin.translogic.request.employee.GetGroupThread;
import com.yeungjin.translogic.utility.Json;
import com.yeungjin.translogic.utility.Session;

import org.json.JSONObject;

public class GroupEditAdapter extends CommonListAdapter<EMPLOYEE_GROUP, GroupEditAdapter.ViewHolder> {
    public GroupEditAdapter(Context context) {
        super(context, new GetGroupThread(Session.user.EMPLOYEE_NUMBER));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_employee_group_edit_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EMPLOYEE_GROUP group = DATA.get(position);

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
    protected int getResponse(@NonNull String response) throws Exception {
        array = new JSONObject(response).getJSONArray("group");
        for (int index = 0; index < array.length(); index++) {
            object = array.getJSONObject(index);
            DATA.add(Json.from(object, EMPLOYEE_GROUP.class));
        }

        return array.length();
    }

    @Override
    public void reload() {
        Request request = new GetGroupRequest(Session.user.EMPLOYEE_NUMBER, new ReloadListener());
        Request.sendRequest(CONTEXT, request);
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
            name = VIEW.findViewById(R.id.adapter_employee_group_edit_list__name);
            rename = VIEW.findViewById(R.id.adapter_employee_group_edit_list__rename);
            remove = VIEW.findViewById(R.id.adapter_employee_group_edit_list__remove);
        }
    }
}
