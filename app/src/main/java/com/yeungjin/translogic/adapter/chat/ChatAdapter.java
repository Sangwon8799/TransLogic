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
import com.yeungjin.translogic.request.Request;
import com.yeungjin.translogic.request.chat.GetChatRequest;
import com.yeungjin.translogic.request.chat.GetChatThread;
import com.yeungjin.translogic.request.chat.GetSearchedChatRequest;
import com.yeungjin.translogic.request.chat.GetUnreadCountRequest;
import com.yeungjin.translogic.utility.Json;
import com.yeungjin.translogic.utility.Server;
import com.yeungjin.translogic.utility.Session;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ChatAdapter extends CommonListAdapter<CHAT, ChatAdapter.ViewHolder> {
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("a hh:mm", Locale.KOREA);

    public Listener listener;

    public ChatAdapter(@NonNull Context context) {
        super(context, new GetChatThread(Session.user.EMPLOYEE_NUMBER));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CHAT chat = DATA.get(position);

        Glide.with(holder.image.getContext()).load(Server.IMAGE_URL + chat.CHAT_IMAGE).into(holder.image);
        holder.title.setText(chat.CHAT_TITLE);
        holder.content.setText(chat.CHAT_LAST_CONTENT.equals("null") ? null : chat.CHAT_LAST_CONTENT);
        holder.time.setText(FORMAT.format(chat.CHAT_LAST_CONTENT_ENROLL_DATE));
        Request request = new GetUnreadCountRequest(chat.CHAT_NUMBER, Session.user.EMPLOYEE_NUMBER, new Response.Listener<String>() {
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
        Request.sendRequest(CONTEXT, request);
        // (10.09 22:45) 채팅목록에서 검색시 VolleyRequest 응답 대기로 인한 데이터 갱신이 느려지므로 코드 수정이 필요
    }

    @Override
    protected int getResponse(@NonNull String response) throws Exception {
        array = new JSONObject(response).getJSONArray("chat");
        for (int index = 0; index < array.length(); index++) {
            object = array.getJSONObject(index);
            DATA.add(Json.from(object, CHAT.class));
        }
        return array.length();
    }

    @Override
    public void reload() {
        Request request = new GetChatRequest(Session.user.EMPLOYEE_NUMBER, 0, new ReloadListener());
        Request.sendRequest(CONTEXT, request);
    }

    @Override
    public void load() {
        Request request = new GetChatRequest(Session.user.EMPLOYEE_NUMBER, DATA.size(), new LoadListener());
        Request.sendRequest(CONTEXT, request);
    }

    public void reload(CharSequence search) {
        Request request = new GetSearchedChatRequest(Session.user.EMPLOYEE_NUMBER, 0, search.toString(), new ReloadListener());
        Request.sendRequest(CONTEXT, request);
    }

    public void load(CharSequence search) {
        Request request = new GetSearchedChatRequest(Session.user.EMPLOYEE_NUMBER, DATA.size(), search.toString(), new LoadListener());
        Request.sendRequest(CONTEXT, request);
    }

    public void refresh(MESSAGE message) {
        for (int index = 0; index < DATA.size(); index++) {
            if (DATA.get(index).CHAT_NUMBER == message.MESSAGE_CHAT_NUMBER) {
                CHAT chat = DATA.get(index);
                chat.CHAT_LAST_CONTENT = message.MESSAGE_CONTENT;
                chat.CHAT_LAST_CONTENT_ENROLL_DATE = message.MESSAGE_ENROLL_DATE;

                DATA.remove(index);
                DATA.add(0, chat);

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
            image = VIEW.findViewById(R.id.adapter_chat_chat__image);
            title = VIEW.findViewById(R.id.adapter_chat_chat__title);
            content = VIEW.findViewById(R.id.adapter_chat_chat__content);
            time = VIEW.findViewById(R.id.adapter_chat_chat__time);
            unread_count = VIEW.findViewById(R.id.adapter_chat_chat__unread_count);
        }

        @Override
        protected void setListener() {
            VIEW.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        Session.entered_chat.CHAT_NUMBER = DATA.get(getAdapterPosition()).CHAT_NUMBER;
                        Session.entered_chat.CHAT_TITLE = DATA.get(getAdapterPosition()).CHAT_TITLE;

                        Intent intent = new Intent(VIEW.getContext(), RoomLayout.class);
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
