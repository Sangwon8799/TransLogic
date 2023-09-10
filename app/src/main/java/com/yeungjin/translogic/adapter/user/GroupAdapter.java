package com.yeungjin.translogic.adapter.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yeungjin.translogic.R;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
   @NonNull
   @Override
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user_group, parent, false);
      return new ViewHolder(view);
   }

   @Override
   public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

   }

   @Override
   public int getItemCount() {
      return 0;
   }

   @Override
   public long getItemId(int position) {
      return position;
   }

   @Override
   public int getItemViewType(int position) {
      return position;
   }

   public class ViewHolder extends RecyclerView.ViewHolder {
      public RadioButton group;

      public ViewHolder(View view) {
         super(view);

         group = (RadioButton) view.findViewById(R.id.adapter_user_group__name);
      }
   }
}
