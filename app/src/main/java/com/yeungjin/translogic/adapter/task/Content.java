package com.yeungjin.translogic.adapter.task;

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
      return null;
   }

   @Override
   public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

   }

   @Override
   public int getItemCount() {
      return 0;
   }

   public class ViewHolder extends RecyclerView.ViewHolder {
      public TextView content;
      public TextView date;
      public Button request;
      public Button moveToChat;

      public ViewHolder(View view) {
         super(view);

         content = (TextView) view.findViewById(R.id.adapter_task_content__content);
         date = (TextView) view.findViewById(R.id.adapter_task_content__date);
         request = (Button) view.findViewById(R.id.adapter_task_content__request);
         moveToChat = (Button) view.findViewById(R.id.adapter_task_content__move_to_chat);
      }
   }
}
