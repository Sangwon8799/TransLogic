package com.yeungjin.translogic.adapter.chat;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.CommonAdapter;
import com.yeungjin.translogic.adapter.CommonViewHolder;
import com.yeungjin.translogic.object.chat.Message;
import com.yeungjin.translogic.utility.BitmapTranslator;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MessageAdapter extends CommonAdapter<Message, RecyclerView.ViewHolder> {
    private static final SimpleDateFormat format = new SimpleDateFormat("a hh:mm", Locale.KOREA);

    public static final int NOTICE = 0;
    public static final int OPPONENT = 1;
    public static final int MYSELF = 2;

    public MessageAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case NOTICE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat_message_notice, parent, false);
                return new NoticeViewHolder(view);
            case OPPONENT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat_message_opponent, parent, false);
                return new OpponentViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat_message_myself, parent, false);
                return new MyselfViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = data.get(position);

        if (holder instanceof NoticeViewHolder) {
            ((NoticeViewHolder) holder).content.setText(message.content);
        } else if (holder instanceof OpponentViewHolder) {
            ((OpponentViewHolder) holder).name.setText(message.name);
            if (BitmapTranslator.isImage(message.content)) {
                ((OpponentViewHolder) holder).content.setBackground(new BitmapDrawable(Resources.getSystem(), BitmapTranslator.toBitmap(message.content)));
            } else {
                ((OpponentViewHolder) holder).content.setText(message.content);
            }
            ((OpponentViewHolder) holder).time.setText(format.format(message.time));
        } else {
            if (BitmapTranslator.isImage(message.content)) {
                ((MyselfViewHolder) holder).content.setBackground(new BitmapDrawable(Resources.getSystem(), BitmapTranslator.toBitmap(message.content)));
            } else {
                ((MyselfViewHolder) holder).content.setText(message.content);
            }
            ((MyselfViewHolder) holder).time.setText(format.format(message.time));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }

    public void addMessage(Message message) {
        data.add(message);
        notifyItemInserted(data.size() - 1);
    }

    public static class NoticeViewHolder extends CommonViewHolder {
        public TextView content;

        public NoticeViewHolder(View view) {
            super(view);
            init();
        }

        @Override
        protected void setId() {
            content = view.findViewById(R.id.adapter_chat_message_notice__notice);
        }
    }

    public static class OpponentViewHolder extends CommonViewHolder {
        public ImageView image;
        public TextView name;
        public TextView content;
        public TextView time;

        public OpponentViewHolder(View view) {
            super(view);
            init();

            image.setClipToOutline(true);
        }

        @Override
        protected void setId() {
            image = view.findViewById(R.id.adapter_chat_message_opponent__image);
            name = view.findViewById(R.id.adapter_chat_message_opponent__name);
            content = view.findViewById(R.id.adapter_chat_message_opponent__message);
            time = view.findViewById(R.id.adapter_chat_message_opponent__time);
        }
    }

    public static class MyselfViewHolder extends CommonViewHolder {
        public TextView content;
        public TextView time;

        public MyselfViewHolder(View view) {
            super(view);
            init();
        }

        @Override
        protected void setId() {
            content = view.findViewById(R.id.adapter_chat_message_myself__message);
            time = view.findViewById(R.id.adapter_chat_message_myself__time);
        }
    }
}
