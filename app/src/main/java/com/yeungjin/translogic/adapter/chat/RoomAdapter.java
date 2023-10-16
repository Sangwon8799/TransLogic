package com.yeungjin.translogic.adapter.chat;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.CommonListAdapter;
import com.yeungjin.translogic.adapter.CommonViewHolder;
import com.yeungjin.translogic.object.view.EMPLOYEE_INFO;
import com.yeungjin.translogic.object.view.MESSAGE_INFO;
import com.yeungjin.translogic.server.DBThread;
import com.yeungjin.translogic.server.DBVolley;
import com.yeungjin.translogic.server.Server;
import com.yeungjin.translogic.utility.Image;
import com.yeungjin.translogic.utility.Session;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class RoomAdapter extends CommonListAdapter<MESSAGE_INFO, RecyclerView.ViewHolder> {
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("a hh:mm", Locale.KOREA);

    public static final int NOTICE = 0;
    public static final int OPPONENT = 1;
    public static final int MYSELF = 2;

    public Listener listener;

    public RoomAdapter(@NonNull Context context) {
        super(context, new DBThread("GetMessage", new HashMap<String, Object>() {{
            put("chat_number", Session.CHAT.CHAT_NUMBER);
            put("index", 0);
        }}));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case NOTICE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat_room_notice, parent, false);
                return new NoticeViewHolder(view);
            case OPPONENT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat_room_opponent, parent, false);
                return new OpponentViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat_room_myself, parent, false);
                return new MyselfViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MESSAGE_INFO message = data.get(position);

        if (holder instanceof NoticeViewHolder) {
            NoticeViewHolder instance = (NoticeViewHolder) holder;

            instance.content.setText(message.MESSAGE_CONTENT);
        } else if (holder instanceof OpponentViewHolder) {
            OpponentViewHolder instance = (OpponentViewHolder) holder;

            Glide.with(instance.image.getContext()).load(Server.IMAGE_URL + new DBThread("GetEmployeeImage", new HashMap<String, Object>() {{
                put("employee_number", message.MESSAGE_EMPLOYEE_NUMBER);
            }}).getResponse()).into(instance.image);
            instance.name.setText(message.MESSAGE_EMPLOYEE_NAME);
            setContent(instance, message);
            instance.time.setText(FORMAT.format(message.MESSAGE_ENROLL_DATE));
        } else {
            MyselfViewHolder instance = (MyselfViewHolder) holder;

            setContent(instance, message);
            instance.time.setText(FORMAT.format(message.MESSAGE_ENROLL_DATE));
        }
    }

    @Override
    public int getItemViewType(int position) {
        long employee_number = data.get(position).MESSAGE_EMPLOYEE_NUMBER;

        if (employee_number == Session.USER.EMPLOYEE_NUMBER) {
            return MYSELF;
        } else if (employee_number == -1) {
            return NOTICE;
        } else {
            return OPPONENT;
        }
    }

    @Override
    public void reload() {
        load();
    }

    @Override
    public void load() {
        new DBVolley(context, "GetMessage", new HashMap<String, Object>() {{
            put("chat_number", Session.CHAT.CHAT_NUMBER);
            put("index", data.size());
        }}, new LoadListener());
    }

    public void addMessage(MESSAGE_INFO message) {
        data.add(message);
        notifyItemInserted(data.size() - 1);
        Objects.requireNonNull(listener).scroll(data.size() - 1);
    }

    private <ViewHolder extends MessageViewHolder> void setContent(ViewHolder holder, MESSAGE_INFO message) {
        if (message.MESSAGE_CONTENT.startsWith("message_")) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpURLConnection connection = (HttpURLConnection) new URL(Server.IMAGE_URL + message.MESSAGE_CONTENT).openConnection();

                        if (connection != null) {
                            connection.setConnectTimeout(10000);
                            connection.setDoInput(true);

                            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                                holder.content.setBackground(new BitmapDrawable(Resources.getSystem(), bitmap));
                                holder.content.setText(null);
                                holder.content.setOnClickListener(new ImageClickListener());
                            }
                        }
                    } catch (Exception error) {
                        error.printStackTrace();
                    }
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (Exception error) {
                error.printStackTrace();
            }
        } else if (Image.isImage(message.MESSAGE_CONTENT)) {
            holder.content.setBackground(new BitmapDrawable(Resources.getSystem(), Image.toBitmap(message.MESSAGE_CONTENT)));
            holder.content.setText(null);
            holder.content.setOnClickListener(new ImageClickListener());
        } else {
            if (holder instanceof OpponentViewHolder) {
                holder.content.setBackgroundResource(R.drawable.adapter_chat_room_opponent__message_background);
            } else {
                holder.content.setBackgroundResource(R.drawable.adapter_chat_room_myself__message_background);
            }
            holder.content.setText(message.MESSAGE_CONTENT);
            holder.content.setOnClickListener(null);
        }
    }

    public static class NoticeViewHolder extends CommonViewHolder {
        public TextView content;

        public NoticeViewHolder(@NonNull View view) {
            super(view);
        }

        @Override
        protected void setId() {
            content = view.findViewById(R.id.adapter_chat_room_notice__notice);
        }
    }

    public static class OpponentViewHolder extends MessageViewHolder {
        public ImageView image;
        public TextView name;
        public TextView time;

        public OpponentViewHolder(@NonNull View view) {
            super(view);

            image.setClipToOutline(true);
        }

        @Override
        protected void setId() {
            image = view.findViewById(R.id.adapter_chat_room_opponent__image);
            name = view.findViewById(R.id.adapter_chat_room_opponent__name);
            content = view.findViewById(R.id.adapter_chat_room_opponent__content);
            time = view.findViewById(R.id.adapter_chat_room_opponent__time);
        }
    }

    public static class MyselfViewHolder extends MessageViewHolder {
        public TextView time;

        public MyselfViewHolder(@NonNull View view) {
            super(view);
        }

        @Override
        protected void setId() {
            content = view.findViewById(R.id.adapter_chat_room_myself__message);
            time = view.findViewById(R.id.adapter_chat_room_myself__time);
        }
    }

    private abstract static class MessageViewHolder extends CommonViewHolder {
        public TextView content;

        public MessageViewHolder(@NonNull View view) {
            super(view);
        }
    }

    private static class ImageClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // 추후 추가
        }
    }

    public interface Listener {
        void scroll(int position);
    }

    public static class MenuAdapter extends CommonListAdapter<EMPLOYEE_INFO, MenuAdapter.ViewHolder> {
        public MenuAdapter(@NonNull Context context) {
            super(context, new DBThread("GetChatMember", new HashMap<String, Object>() {{
                put("chat_number", Session.CHAT.CHAT_NUMBER);
                put("index", 0);
            }}));
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat_room_menu, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            EMPLOYEE_INFO employee = data.get(position);

            Glide.with(holder.image.getContext()).load(Server.IMAGE_URL + employee.EMPLOYEE_IMAGE).into(holder.image);
            holder.name.setText(employee.EMPLOYEE_NAME);
            holder.company.setText(employee.EMPLOYEE_COMPANY_NAME);
        }

        @Override
        public void reload() {
            new DBVolley(context, "GetChatMember", new HashMap<String, Object>() {{
                put("chat_number", Session.CHAT.CHAT_NUMBER);
                put("index", 0);
            }}, new ReloadListener());
        }

        @Override
        public void load() {
            new DBVolley(context, "GetChatMember", new HashMap<String, Object>() {{
                put("chat_number", Session.CHAT.CHAT_NUMBER);
                put("index", data.size());
            }}, new LoadListener());
        }

        public static class ViewHolder extends CommonViewHolder {
            public ImageView image;
            public TextView name;
            public TextView company;

            public ViewHolder(@NonNull View view) {
                super(view);

                image.setClipToOutline(true);
            }

            @Override
            protected void setId() {
                image = view.findViewById(R.id.adapter_chat_room_menu__image);
                name = view.findViewById(R.id.adapter_chat_room_menu__name);
                company = view.findViewById(R.id.adapter_chat_room_menu__company);
            }
        }
    }
}
