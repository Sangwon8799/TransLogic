package com.yeungjin.translogic.adapter.employee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;

import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.CommonListAdapter;
import com.yeungjin.translogic.adapter.CommonViewHolder;
import com.yeungjin.translogic.object.table.EMPLOYEE_GROUP;
import com.yeungjin.translogic.server.DBVolley;
import com.yeungjin.translogic.server.DBThread;
import com.yeungjin.translogic.utility.Session;

import java.util.HashMap;

public class GroupAdapter extends CommonListAdapter<EMPLOYEE_GROUP, GroupAdapter.ViewHolder> {
    private static final int UNSELECT = -1;
    private int current_position = UNSELECT;

    public Listener listener;

    public GroupAdapter(@NonNull Context context) {
        super(context, new DBThread("GetGroup", new HashMap<String, Object>() {{
            put("employee_number", Session.USER.EMPLOYEE_NUMBER);
        }}));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_employee_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EMPLOYEE_GROUP group = data.get(position);

        holder.group.setText(group.EMPLOYEE_GROUP_NAME);
        holder.group.setChecked(position == current_position);
        holder.group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((RadioButton) view).isChecked()) {
                    int past_position = current_position;

                    if (current_position != holder.getAdapterPosition()) {
                        current_position = holder.getAdapterPosition();
                        if (listener != null) {
                            listener.select(group.EMPLOYEE_GROUP_NUMBER);
                        }
                    } else {
                        current_position = UNSELECT;
                        if (listener != null) {
                            listener.unselect();
                        }
                    }

                    if (past_position != UNSELECT) {
                        notifyItemChanged(past_position);
                    }
                }
            }
        });
    }

    @Override
    public void reload() {
        new DBVolley(context, "GetGroup", new HashMap<String, Object>() {{
            put("employee_number", Session.USER.EMPLOYEE_NUMBER);
        }}, new ReloadListener());

        current_position = UNSELECT;
    }

    @Override
    public void load() {
        reload();
    }

    public boolean isSelected() {
        return current_position != UNSELECT;
    }

    public long getNumber() {
        return data.get(current_position).EMPLOYEE_GROUP_NUMBER;
    }

    public static class ViewHolder extends CommonViewHolder {
        public RadioButton group;

        public ViewHolder(@NonNull View view) {
            super(view);
        }

        @Override
        protected void setId() {
            group = view.findViewById(R.id.adapter_employee_group__name);
        }
    }

    public interface Listener {
        void select(long group_number);
        void unselect();
    }
}
