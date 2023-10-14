package com.yeungjin.translogic.adapter.task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.CommonAdapter;
import com.yeungjin.translogic.adapter.CommonViewHolder;
import com.yeungjin.translogic.object.TASK;

public class ContentAdapter extends CommonAdapter<TASK, ContentAdapter.ViewHolder> {
    public ContentAdapter(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_task_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    public static class ViewHolder extends CommonViewHolder {
        public TextView content;
        public Button request;

        public ViewHolder(@NonNull View view) {
            super(view);
        }

        @Override
        protected void setId() {
            content = view.findViewById(R.id.adapter_task_content__content);
            request = view.findViewById(R.id.adapter_task_content__request);
        }
    }
}
