package com.yeungjin.translogic.adapter.chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.CommonListAdapter;
import com.yeungjin.translogic.adapter.CommonViewHolder;
import com.yeungjin.translogic.layout.chat.RoomLayout;
import com.yeungjin.translogic.object.CHAT;
import com.yeungjin.translogic.object.MESSAGE;
import com.yeungjin.translogic.server.DBVolley;
import com.yeungjin.translogic.server.DBThread;
import com.yeungjin.translogic.server.Server;
import com.yeungjin.translogic.utility.Session;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

public class ChatAdapter extends CommonListAdapter<CHAT, ChatAdapter.ViewHolder> {
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("a hh:mm", Locale.KOREA);

    public Listener listener;

    public ChatAdapter(@NonNull Context context) {
        super(context, new DBThread("GetChat", new HashMap<String, Object>() {{
            put("employee_number", Session.user.EMPLOYEE_NUMBER);
            put("index", 0);
        }}));
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
        holder.content.setText(chat.CHAT_LAST_CONTENT.equals("null") ? null : chat.CHAT_LAST_CONTENT);
        holder.time.setText(FORMAT.format(chat.CHAT_LAST_CONTENT_ENROLL_DATE));
        new DBVolley(context, "GetUnreadCount", new HashMap<String, Object>() {{
            put("chat_number", chat.CHAT_NUMBER);
            put("employee_number", Session.user.EMPLOYEE_NUMBER);
        }}, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int unread_count = Integer.parseInt(response.trim());

                if (unread_count == 0 && holder.unread_count.getVisibility() == View.VISIBLE) {
                    holder.unread_count.setVisibility(View.GONE);
                } else if (unread_count > 0 && holder.unread_count.getVisibility() == View.GONE) {
                    holder.unread_count.setVisibility(View.VISIBLE);
                }
                holder.unread_count.setText(response.trim());
            }
        });
        // (10.09 22:45) 채팅목록에서 검색시 VolleyRequest 응답 대기로 인한 데이터 갱신이 느려지므로 코드 수정이 필요
    }

    @Override
    public void reload() {
        new DBVolley(context, "GetChat", new HashMap<String, Object>() {{
            put("employee_number", Session.user.EMPLOYEE_NUMBER);
            put("index", 0);
        }}, new ReloadListener());
    }

    @Override
    public void load() {
        new DBVolley(context, "GetChat", new HashMap<String, Object>() {{
            put("employee_number", Session.user.EMPLOYEE_NUMBER);
            put("index", data.size());
        }}, new LoadListener());
    }

    public void reload(CharSequence search) {
        new DBVolley(context, "GetChat", new HashMap<String, Object>() {{
            put("employee_number", Session.user.EMPLOYEE_NUMBER);
            put("index", 0);
            put("search", search);
        }}, new ReloadListener());
    }

    public void load(CharSequence search) {
        new DBVolley(context, "GetChat", new HashMap<String, Object>() {{
            put("employee_number", Session.user.EMPLOYEE_NUMBER);
            put("index", data.size());
            put("search", search);
        }}, new LoadListener());
    }

    public void refresh(MESSAGE message) {
        for (int index = 0; index < data.size(); index++) {
            if (data.get(index).CHAT_NUMBER == message.MESSAGE_CHAT_NUMBER) {
                CHAT chat = data.get(index);
                chat.CHAT_LAST_CONTENT = message.MESSAGE_CONTENT;
                chat.CHAT_LAST_CONTENT_ENROLL_DATE = message.MESSAGE_ENROLL_DATE;

                data.remove(index);
                data.add(0, chat);

                notifyItemMoved(index, 0);
                notifyItemChanged(0);
                break;
            }
        }
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
                    if (listener != null) {
                        Session.entered_chat.CHAT_NUMBER = data.get(getAdapterPosition()).CHAT_NUMBER;
                        Session.entered_chat.CHAT_TITLE = data.get(getAdapterPosition()).CHAT_TITLE;

                        Intent intent = new Intent(view.getContext(), RoomLayout.class);
                        listener.click(intent, getAdapterPosition());
                    }
                }
            });
        }
    }

    public interface Listener {
        void click(Intent intent, int position);
    }
}
