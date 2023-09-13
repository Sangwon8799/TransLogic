package com.yeungjin.translogic.adapter.task;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yeungjin.translogic.R;

public class Content extends RecyclerView.Adapter<Content.ViewHolder> {
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

   public static class ViewHolder extends RecyclerView.ViewHolder {
      public TextView content;
      public Button request;

      public ViewHolder(View view) {
         super(view);

         content = (TextView) view.findViewById(R.id.adapter_task_content__content);
         request = (Button) view.findViewById(R.id.adapter_task_content__request);
      }
   }
}
