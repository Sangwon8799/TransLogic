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
import com.yeungjin.translogic.adapter.CommonListAdapter;
import com.yeungjin.translogic.adapter.CommonViewHolder;
import com.yeungjin.translogic.object.MESSAGE;
import com.yeungjin.translogic.request.Request;
import com.yeungjin.translogic.request.chat.GetMessageRequest;
import com.yeungjin.translogic.request.chat.GetMessageThread;
import com.yeungjin.translogic.utility.BitmapTranslator;
import com.yeungjin.translogic.utility.Json;
import com.yeungjin.translogic.utility.Session;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class RoomAdapter extends CommonListAdapter<MESSAGE, RecyclerView.ViewHolder> {
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("a hh:mm", Locale.KOREA);

    public static final int NOTICE = 0;
    public static final int OPPONENT = 1;
    public static final int MYSELF = 2;

    public Listener listener;

    public RoomAdapter(@NonNull Context context) {
        super(context, new GetMessageThread(Session.entered_chat.CHAT_NUMBER));
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
        MESSAGE message = DATA.get(position);

        if (holder instanceof NoticeViewHolder) {
            NoticeViewHolder notice = (NoticeViewHolder) holder;

            notice.content.setText(message.MESSAGE_CONTENT);
        } else if (holder instanceof OpponentViewHolder) {
            OpponentViewHolder opponent = (OpponentViewHolder) holder;

            opponent.name.setText(message.MESSAGE_EMPLOYEE_NAME);
            if (BitmapTranslator.isImage(message.MESSAGE_CONTENT)) {
                opponent.content.setBackground(new BitmapDrawable(Resources.getSystem(), BitmapTranslator.toBitmap(message.MESSAGE_CONTENT)));
            } else {
                opponent.content.setText(message.MESSAGE_CONTENT);
            }
            opponent.time.setText(FORMAT.format(message.MESSAGE_ENROLL_DATE));
        } else {
            MyselfViewHolder myself = (MyselfViewHolder) holder;

            if (BitmapTranslator.isImage(message.MESSAGE_CONTENT)) {
                myself.content.setBackground(new BitmapDrawable(Resources.getSystem(), BitmapTranslator.toBitmap(message.MESSAGE_CONTENT)));
            } else {
                myself.content.setText(message.MESSAGE_CONTENT);
            }
            myself.time.setText(FORMAT.format(message.MESSAGE_ENROLL_DATE));
        }
    }

    @Override
    public int getItemViewType(int position) {
        long employee_number = DATA.get(position).MESSAGE_EMPLOYEE_NUMBER;

        if (employee_number == Session.user.EMPLOYEE_NUMBER) {
            return MYSELF;
        } else {
            return OPPONENT;
        }
    }

    @Override
    protected int getResponse(@NonNull String response) throws Exception {
        array = new JSONObject(response).getJSONArray("message");
        for (int index = 0; index < array.length(); index++) {
            object = array.getJSONObject(index);
            DATA.add(Json.from(object, MESSAGE.class));
        }

        return array.length();
    }

    @Override
    public void reload() {
        load();
    }

    @Override
    public void load() {
        Request request = new GetMessageRequest(Session.entered_chat.CHAT_NUMBER, DATA.size(), new LoadListener());
        Request.sendRequest(CONTEXT, request);
    }

    public void addMessage(MESSAGE message) {
        DATA.add(message);
        notifyItemInserted(DATA.size() - 1);

        if (listener != null) {
            listener.scroll(DATA.size() - 1);
        }
    }

    public static class NoticeViewHolder extends CommonViewHolder {
        public TextView content;

        public NoticeViewHolder(@NonNull View view) {
            super(view);
        }

        @Override
        protected void setId() {
            content = VIEW.findViewById(R.id.adapter_chat_message_notice__notice);
        }
    }

    public static class OpponentViewHolder extends CommonViewHolder {
        public ImageView image;
        public TextView name;
        public TextView content;
        public TextView time;

        public OpponentViewHolder(@NonNull View view) {
            super(view);

            image.setClipToOutline(true);
        }

        @Override
        protected void setId() {
            image = VIEW.findViewById(R.id.adapter_chat_message_opponent__image);
            name = VIEW.findViewById(R.id.adapter_chat_message_opponent__name);
            content = VIEW.findViewById(R.id.adapter_chat_message_opponent__message);
            time = VIEW.findViewById(R.id.adapter_chat_message_opponent__time);
        }
    }

    public static class MyselfViewHolder extends CommonViewHolder {
        public TextView content;
        public TextView time;

        public MyselfViewHolder(@NonNull View view) {
            super(view);
        }

        @Override
        protected void setId() {
            content = VIEW.findViewById(R.id.adapter_chat_message_myself__message);
            time = VIEW.findViewById(R.id.adapter_chat_message_myself__time);
        }
    }

    public interface Listener {
        void scroll(int position);
    }
}
