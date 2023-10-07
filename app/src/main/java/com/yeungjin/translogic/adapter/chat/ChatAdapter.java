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
import com.yeungjin.translogic.object.database.CHAT;
import com.yeungjin.translogic.request.Request;
import com.yeungjin.translogic.request.chat.FirstGetChatRequest;
import com.yeungjin.translogic.utility.Server;
import com.yeungjin.translogic.request.chat.GetChatRequest;
import com.yeungjin.translogic.request.chat.GetSearchedChatRequest;
import com.yeungjin.translogic.utility.DateFormat;
import com.yeungjin.translogic.utility.Session;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ChatAdapter extends CommonListAdapter<CHAT, ChatAdapter.ViewHolder> {
    private static final SimpleDateFormat time = new SimpleDateFormat("a hh:mm", Locale.KOREA);

    private OnClickListener listener;

    public ChatAdapter(Context context) {
        super(context, new FirstGetChatRequest());
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

        Glide.with(holder.image.getContext()).load(Server.ImageURL + chat.CHAT_IMAGE).into(holder.image);
        holder.title.setText(chat.CHAT_TITLE);
        holder.content.setText(chat.CHAT_LAST_CONTENT.equals("null") ? "" : chat.CHAT_LAST_CONTENT);
        holder.time.setText(time.format(chat.CHAT_ENROLL_DATE));
    }

    @Override
    protected int getResponse(String response) throws Exception {
        JSONObject json = new JSONObject(response);

        array = json.getJSONArray("chat");
        for (int index = 0; index < array.length(); index++) {
            object = array.getJSONObject(index);

            CHAT chat = new CHAT();
            chat.CHAT_NUMBER = object.getLong("CHAT_NUMBER");
            chat.CHAT_EMPLOYEE_NUMBER = object.getLong("CHAT_EMPLOYEE_NUMBER");
            chat.CHAT_TITLE = object.getString("CHAT_TITLE");
            chat.CHAT_LAST_ACCESS = DateFormat.DATETIME.parse(object.getString("CHAT_LAST_ACCESS"));
            chat.CHAT_LAST_CONTENT = object.getString("CHAT_LAST_CONTENT");
            chat.CHAT_IMAGE = object.getString("CHAT_IMAGE");
            chat.CHAT_ENROLL_DATE = DateFormat.DATETIME.parse(object.getString("CHAT_ENROLL_DATE"));

            data.add(chat);
        }

        return array.length();
    }

    @Override
    public void reload() {
        Request request = new GetChatRequest(0, new ReloadListener());
        Request.sendRequest(context, request);
    }

    @Override
    public void load() {
        Request request = new GetChatRequest(data.size(), new LoadListener());
        Request.sendRequest(context, request);
    }

    public void reload(CharSequence search) {
        Request request = new GetSearchedChatRequest(0, search.toString(), new ReloadListener());
        Request.sendRequest(context, request);
    }

    public void load(CharSequence search) {
        Request request = new GetSearchedChatRequest(data.size(), search.toString(), new LoadListener());
        Request.sendRequest(context, request);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends CommonViewHolder {
        public ImageView image;
        public TextView title;
        public TextView content;
        public TextView time;
        public TextView unreadCount;

        public ViewHolder(View view) {
            super(view);
            init();

            image.setClipToOutline(true);
        }

        @Override
        protected void setId() {
            image = view.findViewById(R.id.adapter_chat_chat__image);
            title = view.findViewById(R.id.adapter_chat_chat__title);
            content = view.findViewById(R.id.adapter_chat_chat__content);
            time = view.findViewById(R.id.adapter_chat_chat__time);
            unreadCount = view.findViewById(R.id.adapter_chat_chat__unread_count);
        }

        @Override
        protected void setListener() {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        Session.chat.CHAT_NUMBER = data.get(getAdapterPosition()).CHAT_NUMBER;
                        Session.chat.CHAT_TITLE = data.get(getAdapterPosition()).CHAT_TITLE;

                        Intent intent = new Intent(view.getContext(), RoomLayout.class);
                        listener.click(intent);
                    }
                }
            });
        }
    }

    public interface OnClickListener {
        void click(Intent intent);
    }
}
