package com.yeungjin.translogic.adapter.chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.yeungjin.translogic.R;
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
import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<CHAT> data = new ArrayList<>();

    private final SimpleDateFormat time = new SimpleDateFormat("a hh:mm");
    private OnItemClickListener listener;

    public ChatAdapter(Context context) {
        this.context = context;
    }

    public void reload() {
        reload(null);
    }

    public void reload(String content) {
        Request request;
        if (content == null) {
            request = new GetChatRequest(0, new ReloadListener());
        } else {
            request = new GetSearchedChatRequest(content, 0, new ReloadListener());
        }
        Request.queue = Volley.newRequestQueue(context);
        Request.queue.add(request);
    }

    public void load() {
        load(null);
    }

    public void load(String content) {
        Request request;
        if (content == null) {
            request = new GetChatRequest(data.size(), new LoadListener());
        } else {
            request = new GetSearchedChatRequest(content, data.size(), new LoadListener());
        }
        Request.queue = Volley.newRequestQueue(context);
        Request.queue.add(request);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
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

        holder.title.setText(chat.CHAT_TITLE);
        holder.content.setText(chat.CHAT_LAST_CONTENT.equals("null") ? "" : chat.CHAT_LAST_CONTENT);
        holder.time.setText(time.format(chat.CHAT_ENROLL_DATE));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        Intent intent = new Intent(view.getContext(), RoomLayout.class);
                        intent.putExtra("name", Session.user.EMPLOYEE_NAME);
                        intent.putExtra("number", String.valueOf(data.get(getAdapterPosition()).CHAT_NUMBER));
                        intent.putExtra("title", data.get(getAdapterPosition()).CHAT_TITLE);

                        listener.execute(intent);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        public void execute(Intent intent);
    }

    private class ReloadListener implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
            notifyItemRangeRemoved(0, data.size());
            data.clear();

            try {
                notifyItemRangeInserted(0, getResponse(response));
            } catch (Exception error) {
                error.printStackTrace();
            }
        }
    }

    private class LoadListener implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
            int position = data.size();

            try {
                notifyItemRangeInserted(position, getResponse(response));
            } catch (Exception error) {
                error.printStackTrace();
            }
        }
    }

    private int getResponse(String response) throws Exception {
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
            chat.CHAT_LAST_ACCESS = DateFormat.getInstance().DATETIME.parse(object.getString("CHAT_LAST_ACCESS"));
            chat.CHAT_LAST_CONTENT = object.getString("CHAT_LAST_CONTENT");
            chat.CHAT_IMAGE = object.getString("CHAT_IMAGE");
            chat.CHAT_ENROLL_DATE = DateFormat.getInstance().DATETIME.parse(object.getString("CHAT_ENROLL_DATE"));

            data.add(chat);
        }

        return array.length();
    }
}
