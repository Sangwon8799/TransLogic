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
import com.yeungjin.translogic.object.database.EMPLOYEE_GROUP;
import com.yeungjin.translogic.request.Request;
import com.yeungjin.translogic.request.employee.FirstGetGroupRequest;
import com.yeungjin.translogic.request.employee.GetGroupRequest;
import com.yeungjin.translogic.utility.DateFormat;
import com.yeungjin.translogic.utility.Session;

import org.json.JSONObject;

public class GroupAdapter extends CommonListAdapter<EMPLOYEE_GROUP, GroupAdapter.ViewHolder> {
    private static final int UNSELECT = -1;
    private int currentPosition = UNSELECT;

    private OnSelectListener selectListener;

    public GroupAdapter(Context context) {
        super(context, new FirstGetGroupRequest(Session.user.EMPLOYEE_NUMBER));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_employee_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EMPLOYEE_GROUP group = data.get(position);

        holder.group.setText(group.EMPLOYEE_GROUP_NAME);
        holder.group.setChecked(position == currentPosition);
        holder.group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((RadioButton) view).isChecked()) {
                    int pastPosition = currentPosition;

                    if (currentPosition != holder.getAdapterPosition()) {
                        currentPosition = holder.getAdapterPosition();

                        if (selectListener != null) {
                            selectListener.select(group.EMPLOYEE_GROUP_NUMBER);
                        }
                    } else {
                        currentPosition = UNSELECT;

                        if (selectListener != null) {
                            selectListener.unselect();
                        }
                    }

                    if (pastPosition != UNSELECT) {
                        notifyItemChanged(pastPosition);
                    }
                }
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
        currentPosition = UNSELECT;

        Request request = new GetGroupRequest(Session.user.EMPLOYEE_NUMBER, new ReloadListener());
        Request.sendRequest(context, request);
    }

    @Override
    public void load() {
        reload();
    }

    public boolean isSelected() {
        return currentPosition != UNSELECT;
    }

    public long getNumber() {
        return data.get(currentPosition).EMPLOYEE_GROUP_NUMBER;
    }

    public void setOnSelectListener(OnSelectListener listener) {
        this.selectListener = listener;
    }

    public static class ViewHolder extends CommonViewHolder {
        public RadioButton group;

        public ViewHolder(View view) {
            super(view);
            init();
        }

        @Override
        protected void setId() {
            group = view.findViewById(R.id.adapter_employee_group__name);
        }
    }

    public interface OnSelectListener {
        void select(long group_number);
        void unselect();
    }
}
