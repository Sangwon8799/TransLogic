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
import com.yeungjin.translogic.object.database.EMPLOYEE_GROUP;
import com.yeungjin.translogic.request.Request;
import com.yeungjin.translogic.request.employee.GetGroupRequest;
import com.yeungjin.translogic.request.employee.FirstGetGroupRequest;
import com.yeungjin.translogic.utility.DateFormat;
import com.yeungjin.translogic.utility.Session;

import org.json.JSONObject;

public class GroupEditAdapter extends CommonListAdapter<EMPLOYEE_GROUP, GroupEditAdapter.ViewHolder> {
    public GroupEditAdapter(Context context) {
        super(context, new FirstGetGroupRequest(Session.user.EMPLOYEE_NUMBER));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_employee_group_edit_list, parent, false);
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
    protected int getResponse(String response) throws Exception {
        JSONObject http = new JSONObject(response);

        array = http.getJSONArray("group");
        for (int index = 0; index < array.length(); index++) {
            object = array.getJSONObject(index);

            EMPLOYEE_GROUP group = new EMPLOYEE_GROUP();
            group.EMPLOYEE_GROUP_NUMBER = object.getLong("EMPLOYEE_GROUP_NUMBER");
            group.EMPLOYEE_GROUP_NAME = object.getString("EMPLOYEE_GROUP_NAME");
            group.EMPLOYEE_GROUP_EMPLOYEE_NUMBER = object.getLong("EMPLOYEE_GROUP_EMPLOYEE_NUMBER");
            group.EMPLOYEE_GROUP_ENROLL_DATE = DateFormat.DATETIME.parse(object.getString("EMPLOYEE_GROUP_ENROLL_DATE"));

            data.add(group);
        }

        return array.length();
    }

    @Override
    public void reload() {
        Request request = new GetGroupRequest(Session.user.EMPLOYEE_NUMBER, new ReloadListener());
        Request.sendRequest(context, request);
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
            init();
        }

        @Override
        protected void setId() {
            name = view.findViewById(R.id.adapter_employee_group_edit_list__name);
            rename = view.findViewById(R.id.adapter_employee_group_edit_list__rename);
            remove = view.findViewById(R.id.adapter_employee_group_edit_list__remove);
        }
    }
}
