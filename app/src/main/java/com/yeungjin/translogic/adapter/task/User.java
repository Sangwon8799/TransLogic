package com.yeungjin.translogic.adapter.task;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yeungjin.translogic.R;

public class User extends RecyclerView.Adapter<User.ViewHolder> implements Filterable {
   @Override
   public Filter getFilter() {
      return null;
   }

   @NonNull
   @Override
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_task_user, parent, false);
      return new ViewHolder(view);
   }

   @Override
   public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

   }

   @Override
   public int getItemCount() {
      return 0;
   }

   public class ViewHolder extends RecyclerView.ViewHolder {
      public ImageView image;
      public TextView name;
      public TextView company;
      public RecyclerView content;

      public ViewHolder(View view) {
         super(view);

         image = (ImageView) view.findViewById(R.id.adapter_task_user__image);
         name = (TextView) view.findViewById(R.id.adapter_task_user__name);
         company = (TextView) view.findViewById(R.id.adapter_task_user__company);
         content = (RecyclerView) view.findViewById(R.id.adapter_task_user__content);
         content.setLayoutManager(new LinearLayoutManager(view.getContext()));
      }
   }
}
