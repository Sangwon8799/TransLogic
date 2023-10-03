package com.yeungjin.translogic.adapter.chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.CommonAdapter;
import com.yeungjin.translogic.adapter.CommonViewHolder;
import com.yeungjin.translogic.layout.chat.RoomLayout;
import com.yeungjin.translogic.object.database.CHAT;
import com.yeungjin.translogic.request.Request;
import com.yeungjin.translogic.request.chat.GetChatRequest;
import com.yeungjin.translogic.request.chat.GetSearchedChatRequest;
import com.yeungjin.translogic.utility.DateFormat;
import com.yeungjin.translogic.utility.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ChatAdapter extends CommonAdapter<ChatAdapter.ViewHolder> {
    private final SimpleDateFormat time = new SimpleDateFormat("a hh:mm", Locale.KOREA);
    private OnItemClickListener listener;

    public ChatAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CHAT chat = (CHAT) data.get(position);

        holder.title.setText(chat.CHAT_TITLE);
        holder.content.setText(chat.CHAT_LAST_CONTENT.equals("null") ? "" : chat.CHAT_LAST_CONTENT);
        holder.time.setText(time.format(chat.CHAT_ENROLL_DATE));
    }

    @Override
    public void reload(CharSequence content) {
        Request request;
        if (content == null) {
            request = new GetChatRequest(0, new ReloadListener());
        } else {
            request = new GetSearchedChatRequest(content.toString(), 0, new ReloadListener());
        }
        Request.sendRequest(context, request);
    }

    @Override
    public void load(CharSequence content) {
        Request request;
        if (content == null) {
            request = new GetChatRequest(data.size(), new LoadListener());
        } else {
            request = new GetSearchedChatRequest(content.toString(), data.size(), new LoadListener());
        }
        Request.sendRequest(context, request);
    }

    @Override
    protected int getResponse(String response) throws Exception {
        JSONObject json = new JSONObject(response);

        JSONObject object;
        JSONArray array;

        array = json.getJSONArray("chat");
        for (int step = 0; step < array.length(); step++) {
            object = array.getJSONObject(step);

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

    public void setOnItemClickListener(OnItemClickListener listener) {
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
                        Session.chat.CHAT_NUMBER = ((CHAT) data.get(getAdapterPosition())).CHAT_NUMBER;
                        Session.chat.CHAT_TITLE = ((CHAT) data.get(getAdapterPosition())).CHAT_TITLE;

                        Intent intent = new Intent(view.getContext(), RoomLayout.class);
                        listener.execute(intent);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        public void execute(Intent intent);
    }
}
