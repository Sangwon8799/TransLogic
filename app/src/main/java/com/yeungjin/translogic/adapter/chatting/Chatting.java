package com.yeungjin.translogic.adapter.chatting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yeungjin.translogic.R;

public class Chatting extends RecyclerView.Adapter<Chatting.ViewHolder> implements Filterable {
   @Override
   public Filter getFilter() {
      return new Filter() {
         @Override
         protected FilterResults performFiltering(CharSequence constraint) {
            return null;
         }

         @Override
         protected void publishResults(CharSequence constraint, FilterResults results) {

         }
      };
   }

   @NonNull
   @Override
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat_chat, parent, false);
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
      public ImageView image;
      public TextView title;
      public TextView content;
      public TextView time;
      public TextView unreadCount;

      public ViewHolder(View view) {
         super(view);

         image = (ImageView) view.findViewById(R.id.adapter_chat_chat__image);
         title = (TextView) view.findViewById(R.id.adapter_chat_chat__title);
         content = (TextView) view.findViewById(R.id.adapter_chat_chat__content);
         time = (TextView) view.findViewById(R.id.adapter_chat_chat__time);
         unreadCount = (TextView) view.findViewById(R.id.adapter_chat_chat__unread_count);
      }
   }
}
