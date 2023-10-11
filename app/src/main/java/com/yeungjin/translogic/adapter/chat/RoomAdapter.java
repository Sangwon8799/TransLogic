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

import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.CommonListAdapter;
import com.yeungjin.translogic.adapter.CommonViewHolder;
import com.yeungjin.translogic.object.MESSAGE;
import com.yeungjin.translogic.server.DBVolley;
import com.yeungjin.translogic.server.DBThread;
import com.yeungjin.translogic.utility.Image;
import com.yeungjin.translogic.server.Server;
import com.yeungjin.translogic.utility.Session;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

public class RoomAdapter extends CommonListAdapter<MESSAGE, RecyclerView.ViewHolder> {
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("a hh:mm", Locale.KOREA);

    public static final int NOTICE = 0;
    public static final int OPPONENT = 1;
    public static final int MYSELF = 2;

    public Listener listener;

    public RoomAdapter(@NonNull Context context) {
        super(context, new DBThread("GetMessage", new HashMap<String, Object>() {{
            put("chat_number", Session.entered_chat.CHAT_NUMBER);
        }}));
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
        MESSAGE message = data.get(position);

        if (holder instanceof NoticeViewHolder) {
            NoticeViewHolder notice = (NoticeViewHolder) holder;

            notice.content.setText(message.MESSAGE_CONTENT);
        } else if (holder instanceof OpponentViewHolder) {
            OpponentViewHolder opponent = (OpponentViewHolder) holder;

            opponent.name.setText(message.MESSAGE_EMPLOYEE_NAME);
            if (message.MESSAGE_CONTENT.startsWith("message_")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HttpURLConnection connection = (HttpURLConnection) new URL(Server.IMAGE_URL + message.MESSAGE_CONTENT).openConnection();

                            if (connection != null) {
                                connection.setConnectTimeout(10000);
                                connection.setDoInput(true);

                                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                    Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                                    opponent.content.setBackground(new BitmapDrawable(Resources.getSystem(), bitmap));
                                    opponent.content.setText(null);
                                    opponent.content.setOnClickListener(null);
                                }
                            }
                        } catch (Exception error) {
                            error.printStackTrace();
                        }
                    }
                }).start();
            } else if (Image.isImage(message.MESSAGE_CONTENT)) {
                opponent.content.setBackground(new BitmapDrawable(Resources.getSystem(), Image.toBitmap(message.MESSAGE_CONTENT)));
                opponent.content.setText(null);
                opponent.content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 추후 추가
                    }
                });
            } else {
                opponent.content.setBackgroundResource(R.drawable.adapter_chat_room_opponent__message_background);
                opponent.content.setText(message.MESSAGE_CONTENT);
                opponent.content.setOnClickListener(null);
            }
            opponent.time.setText(FORMAT.format(message.MESSAGE_ENROLL_DATE));
        } else {
            MyselfViewHolder myself = (MyselfViewHolder) holder;

            if (message.MESSAGE_CONTENT.startsWith("message_")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HttpURLConnection connection = (HttpURLConnection) new URL(Server.IMAGE_URL + message.MESSAGE_CONTENT).openConnection();

                            if (connection != null) {
                                connection.setConnectTimeout(10000);
                                connection.setDoInput(true);

                                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                    Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                                    myself.content.setBackground(new BitmapDrawable(Resources.getSystem(), bitmap));
                                    myself.content.setText(null);
                                    myself.content.setOnClickListener(null);
                                }
                            }
                        } catch (Exception error) {
                            error.printStackTrace();
                        }
                    }
                }).start();
            } else if (Image.isImage(message.MESSAGE_CONTENT)) {
                myself.content.setBackground(new BitmapDrawable(Resources.getSystem(), Image.toBitmap(message.MESSAGE_CONTENT)));
                myself.content.setText(null);
                myself.content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 추후 추가
                    }
                });
            } else {
                myself.content.setBackgroundResource(R.drawable.adapter_chat_room_myself__message_background);
                myself.content.setText(message.MESSAGE_CONTENT);
                myself.content.setOnClickListener(null);
            }
            myself.time.setText(FORMAT.format(message.MESSAGE_ENROLL_DATE));
        }
    }

    @Override
    public int getItemViewType(int position) {
        long employee_number = data.get(position).MESSAGE_EMPLOYEE_NUMBER;

        if (employee_number == Session.user.EMPLOYEE_NUMBER) {
            return MYSELF;
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
            put("chat_number", Session.entered_chat.CHAT_NUMBER);
            put("index", data.size());
        }}, new LoadListener());
    }

    public void addMessage(MESSAGE message) {
        data.add(message);
        notifyItemInserted(data.size() - 1);

        if (listener != null) {
            listener.scroll(data.size() - 1);
        }
    }

    public static class NoticeViewHolder extends CommonViewHolder {
        public TextView content;

        public NoticeViewHolder(@NonNull View view) {
            super(view);
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

        public OpponentViewHolder(@NonNull View view) {
            super(view);

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

        public MyselfViewHolder(@NonNull View view) {
            super(view);
        }

        @Override
        protected void setId() {
            content = view.findViewById(R.id.adapter_chat_message_myself__message);
            time = view.findViewById(R.id.adapter_chat_message_myself__time);
        }
    }

    public interface Listener {
        void scroll(int position);
    }
}
