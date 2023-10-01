package com.yeungjin.translogic.adapter.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yeungjin.translogic.R;
import com.yeungjin.translogic.object.chat.MessageInfo;
import com.yeungjin.translogic.object.chat.MessageType;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int NOTICE = 0;
    private static final int OPPONENT = 1;
    private static final int MYSELF = 2;

    private ArrayList<MessageInfo> data = new ArrayList<>();

    public void addItem(MessageInfo info) {
        data.add(info);
        notifyItemInserted(data.size() - 1);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == NOTICE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat_message_notice, parent, false);
            return new NoticeViewHolder(view);
        } else if (viewType == OPPONENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat_message_opponent, parent, false);
            return new OpponentViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat_message_myself, parent, false);
            return new MyselfViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageInfo info = data.get(position);

        if (holder instanceof NoticeViewHolder) {
            ((NoticeViewHolder) holder).notice.setText(info.message);
        } else if (holder instanceof OpponentViewHolder) {
            ((OpponentViewHolder) holder).name.setText(info.sender);
            ((OpponentViewHolder) holder).message.setText(info.message);
            ((OpponentViewHolder) holder).time.setText(info.time);
        } else {
            ((MyselfViewHolder) holder).message.setText(info.message);
            ((MyselfViewHolder) holder).time.setText(info.time);
        }
    }

    @Override
    public int getItemViewType(int position) {
        MessageType type = data.get(position).type;

        if (type == MessageType.NOTICE) {
            return NOTICE;
        } else if (type == MessageType.OPPONENT) {
            return OPPONENT;
        } else {
            return MYSELF;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class NoticeViewHolder extends RecyclerView.ViewHolder {
        public TextView notice;

        public NoticeViewHolder(View view) {
            super(view);

            notice = (TextView) view.findViewById(R.id.adapter_chat_message_notice__notice);
        }
    }

    public static class OpponentViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;
        public TextView message;
        public TextView time;

        public OpponentViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.adapter_chat_message_opponent__image);
            name = (TextView) view.findViewById(R.id.adapter_chat_message_opponent__name);
            message = (TextView) view.findViewById(R.id.adapter_chat_message_opponent__message);
            time = (TextView) view.findViewById(R.id.adapter_chat_message_opponent__time);
        }
    }

    public static class MyselfViewHolder extends RecyclerView.ViewHolder {
        public TextView message;
        public TextView time;

        public MyselfViewHolder(View view) {
            super(view);

            message = (TextView) view.findViewById(R.id.adapter_chat_message_myself__message);
            time = (TextView) view.findViewById(R.id.adapter_chat_message_myself__time);
        }
    }
}
