package com.yeungjin.translogic.adapter.chat;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yeungjin.translogic.R;
import com.yeungjin.translogic.object.chat.MessageInfo;
import com.yeungjin.translogic.object.chat.MessageType;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int NOTICE = 0;
    private static final int OPPONENT = 1;
    private static final int MYSELF = 2;

    private ArrayList<MessageInfo> data = new ArrayList<>();

    ViewGroup.LayoutParams layoutParams;

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
            // 상대방이 전송한 것이 이미지인지 확인 후 실행
            if (isImage(info.message)) {
                layoutParams = ((OpponentViewHolder) holder).message.getLayoutParams();
                layoutParams.width = dpToPx(150);
                layoutParams.height = dpToPx(250);
                ((OpponentViewHolder) holder).message.setLayoutParams(layoutParams);
                ((OpponentViewHolder) holder).message.setBackground(new BitmapDrawable(Resources.getSystem(), StringToBitmap(info.message)));
                ((OpponentViewHolder) holder).message.setText("");
            }
            else
                ((OpponentViewHolder) holder).message.setText(info.message);
            ((OpponentViewHolder) holder).time.setText(info.time);
        } else {
            // 내가 보낸 것이 이미지인지 확인 후 실행
            if (isImage(info.message)) {
                layoutParams = ((MyselfViewHolder) holder).message.getLayoutParams();
                layoutParams.width = dpToPx(150);
                layoutParams.height = dpToPx(250);
                ((MyselfViewHolder) holder).message.setLayoutParams(layoutParams);
                ((MyselfViewHolder) holder).message.setBackground(new BitmapDrawable(Resources.getSystem(), StringToBitmap(info.message)));
                ((MyselfViewHolder) holder).message.setText("");
            }
            else
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

    public MessageInfo getItem(int position) {
        return data.get(position);
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

    // 해당 문자열이 일반 메시지인지 Base64로 인코딩된 이미지인지 확인하는 메소드
    private boolean isImage(String str) {
        Pattern pattern =  Pattern.compile("/9j/4AAQSkZJRgABAQAAAQABAAD/[A-Za-z0-9]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    // Base64 -> 비트맵으로 변환하는 메소드
    private Bitmap StringToBitmap(String str) {
        try {
            byte[] encodeByte = Base64.decode(str, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }
}
