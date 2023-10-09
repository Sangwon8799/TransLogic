package com.yeungjin.translogic.adapter.task;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.CommonViewHolder;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_task_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends CommonViewHolder {
        public TextView content;
        public Button request;

        public ViewHolder(@NonNull View view) {
            super(view);
        }

        @Override
        protected void setId() {
            content = VIEW.findViewById(R.id.adapter_task_content__content);
            request = VIEW.findViewById(R.id.adapter_task_content__request);
        }
    }
}
