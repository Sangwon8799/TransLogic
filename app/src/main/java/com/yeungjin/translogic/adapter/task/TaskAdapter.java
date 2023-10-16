package com.yeungjin.translogic.adapter.task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.CommonListAdapter;
import com.yeungjin.translogic.adapter.CommonViewHolder;
import com.yeungjin.translogic.object.view.TASK_INFO;
import com.yeungjin.translogic.server.DBThread;
import com.yeungjin.translogic.server.DBVolley;
import com.yeungjin.translogic.server.Server;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

public class TaskAdapter extends CommonListAdapter<TASK_INFO, TaskAdapter.ViewHolder> {
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("a HH:mm", Locale.KOREA);

    public TaskAdapter(@NonNull Context context) {
        super(context, new DBThread("GetTask", new HashMap<String, Object>() {{
            put("index", 0);
        }}));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_task_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TASK_INFO task = data.get(position);

        Glide.with(holder.image.getContext()).load(Server.IMAGE_URL + task.TASK_EMPLOYEE_IMAGE).into(holder.image);
        holder.name.setText(task.TASK_EMPLOYEE_NAME);
        holder.company.setText(task.TASK_COMPANY_NAME);
        holder.time.setText(FORMAT.format(task.TASK_ENROLL_DATE));
    }

    @Override
    public void reload() {
        new DBVolley(context, "GetTask", new HashMap<String, Object>() {{
            put("index", 0);
        }}, new ReloadListener());
    }

    @Override
    public void load() {
        new DBVolley(context, "GetTask", new HashMap<String, Object>() {{
            put("index", data.size());
        }}, new LoadListener());
    }

    public static class ViewHolder extends CommonViewHolder {
        public ImageView image;
        public TextView name;
        public TextView company;
        public LinearLayout information;
        public TextView content;
        public TextView time;
        public LinearLayout action;
        public Button request;
        public Button chat;

        public ViewHolder(View view) {
            super(view);

            image.setClipToOutline(true);
        }

        @Override
        protected void setId() {
            image = view.findViewById(R.id.adapter_task_content__image);
            name = view.findViewById(R.id.adapter_task_content__name);
            company = view.findViewById(R.id.adapter_task_content__company);
            information = view.findViewById(R.id.adapter_task_content__information);
            content = view.findViewById(R.id.adapter_task_content__content);
            time = view.findViewById(R.id.adapter_task_content__time);
            action = view.findViewById(R.id.adapter_task_content__action);
            request = view.findViewById(R.id.adapter_task_content__request);
            chat = view.findViewById(R.id.adapter_task_content__chat);
        }
    }
}
