package com.yeungjin.translogic.adapter.chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.CommonListAdapter;
import com.yeungjin.translogic.adapter.CommonViewHolder;
import com.yeungjin.translogic.layout.chat.RoomLayout;
import com.yeungjin.translogic.object.CHAT;
import com.yeungjin.translogic.object.MESSAGE;
import com.yeungjin.translogic.server.DBThread;
import com.yeungjin.translogic.server.DBVolley;
import com.yeungjin.translogic.server.Server;
import com.yeungjin.translogic.utility.Image;
import com.yeungjin.translogic.utility.Session;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ChatAdapter extends CommonListAdapter<CHAT, ChatAdapter.ViewHolder> {
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("a hh:mm", Locale.KOREA);
    private final Map<Long, Integer> unread_counts = new HashMap<>();

    public Listener listener;

    public ChatAdapter(@NonNull Context context) {
        super(context, new DBThread("GetChat", new HashMap<String, Object>() {{
            put("employee_number", Session.USER.EMPLOYEE_NUMBER);
            put("index", 0);
        }}));
        for (CHAT chat : data) {
            getUnreadCount(chat.CHAT_NUMBER);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CHAT chat = data.get(position);

        Glide.with(holder.image.getContext()).load(Server.IMAGE_URL + chat.CHAT_IMAGE).into(holder.image);
        holder.title.setText(chat.CHAT_TITLE);
        if (chat.CHAT_LAST_CONTENT.startsWith("message_") || Image.isImage(chat.CHAT_LAST_CONTENT)) {
            holder.content.setText("사진");
        } else {
            holder.content.setText(chat.CHAT_LAST_CONTENT);
        }
        holder.time.setText(FORMAT.format(chat.CHAT_LAST_CONTENT_ENROLL_DATE));

        int count = Objects.requireNonNull(unread_counts.get(chat.CHAT_NUMBER));
        if (count == 0 && holder.unread_count.getVisibility() == View.VISIBLE) {
            holder.unread_count.setVisibility(View.GONE);
        } else if (count > 0 && holder.unread_count.getVisibility() == View.GONE) {
            holder.unread_count.setVisibility(View.VISIBLE);
        }
        holder.unread_count.setText(String.valueOf(count));
    }

    @Override
    public void reload() {
        new DBVolley(context, "GetChat", new HashMap<String, Object>() {{
            put("employee_number", Session.USER.EMPLOYEE_NUMBER);
            put("index", 0);
        }}, new ReloadListener());
    }

    @Override
    public void load() {
        new DBVolley(context, "GetChat", new HashMap<String, Object>() {{
            put("employee_number", Session.USER.EMPLOYEE_NUMBER);
            put("index", data.size());
        }}, new LoadListener());
    }

    public void reload(CharSequence search) {
        new DBVolley(context, "GetChat", new HashMap<String, Object>() {{
            put("employee_number", Session.USER.EMPLOYEE_NUMBER);
            put("index", 0);
            put("search", search);
        }}, new ReloadListener());
    }

    public void load(CharSequence search) {
        new DBVolley(context, "GetChat", new HashMap<String, Object>() {{
            put("employee_number", Session.USER.EMPLOYEE_NUMBER);
            put("index", data.size());
            put("search", search);
        }}, new LoadListener());
    }

    public void refresh(MESSAGE message) {
        for (int index = 0; index < data.size(); index++) {
            if (data.get(index).CHAT_NUMBER == message.MESSAGE_CHAT_NUMBER) {
                if (message.MESSAGE_CONTENT.startsWith("message_") || Image.isImage(message.MESSAGE_CONTENT)) {
                    data.get(index).CHAT_LAST_CONTENT = "사진";
                } else {
                    data.get(index).CHAT_LAST_CONTENT = message.MESSAGE_CONTENT;
                }
                data.get(index).CHAT_LAST_CONTENT_ENROLL_DATE = message.MESSAGE_ENROLL_DATE;

                if (Session.CHAT == null) {
                    unread_counts.put(message.MESSAGE_CHAT_NUMBER, Objects.requireNonNull(unread_counts.get(message.MESSAGE_CHAT_NUMBER)) + 1);
                }

                notifyItemChanged(index);
                break;
            }
        }
    }

    public void getUnreadCount(long chat_number) {
        int count = Integer.parseInt(new DBThread("GetUnreadCount", new HashMap<String, Object>() {{
            put("chat_number", chat_number);
            put("employee_number", Session.USER.EMPLOYEE_NUMBER);
        }}).getResponse().trim());

        unread_counts.put(chat_number, count);
    }

    public class ViewHolder extends CommonViewHolder {
        public ImageView image;
        public TextView title;
        public TextView content;
        public TextView time;
        public TextView unread_count;

        public ViewHolder(@NonNull View view) {
            super(view);

            image.setClipToOutline(true);
        }

        @Override
        protected void setId() {
            image = view.findViewById(R.id.adapter_chat_chat__image);
            title = view.findViewById(R.id.adapter_chat_chat__title);
            content = view.findViewById(R.id.adapter_chat_chat__content);
            time = view.findViewById(R.id.adapter_chat_chat__time);
            unread_count = view.findViewById(R.id.adapter_chat_chat__unread_count);
        }

        @Override
        protected void setListener() {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    Session.CHAT = new CHAT();
                    Session.CHAT.CHAT_NUMBER = data.get(position).CHAT_NUMBER;
                    Session.CHAT.CHAT_TITLE = data.get(position).CHAT_TITLE;
                    unread_counts.put(data.get(position).CHAT_NUMBER, 0);

                    new DBVolley(context, "RefreshLastAccess", new HashMap<String, Object>() {{
                        put("chat_number", Session.CHAT.CHAT_NUMBER);
                        put("employee_number", Session.USER.EMPLOYEE_NUMBER);
                    }});

                    Intent intent = new Intent(view.getContext(), RoomLayout.class);
                    Objects.requireNonNull(listener).click(intent, position);
                }
            });
        }
    }

    public interface Listener {
        void click(Intent intent, int position);
    }
}
