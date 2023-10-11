package com.yeungjin.translogic.layout;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.yeungjin.translogic.R;
import com.yeungjin.translogic.layout.chat.ChatLayout;
import com.yeungjin.translogic.layout.employee.EmployeeLayout;
import com.yeungjin.translogic.layout.setting.SettingLayout;
import com.yeungjin.translogic.layout.task.TaskLayout;
import com.yeungjin.translogic.utility.Session;

public class MainLayout extends CommonActivity {
    private final Layout[] layouts = {
            new Layout(new EmployeeLayout(), R.id.layout_main__menu_icon__user),
            new Layout(new ChatLayout(), R.id.layout_main__menu_icon__chat),
            new Layout(new TaskLayout(), R.id.layout_main__menu_icon__task),
            new Layout(new SettingLayout(), R.id.layout_main__menu_icon__setting)
    };
    private final FragmentManager manager = getSupportFragmentManager();
    private FragmentTransaction transaction = manager.beginTransaction();

    private BottomNavigationView menu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        for (Layout layout : layouts) {
            transaction.add(R.id.layout_main_screen, layout.layout);
            transaction.hide(layout.layout);
        }
        transaction.show(layouts[0].layout).commit();

        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        for (long chat_number : Session.joined_chat) {
            Session.socket.emit("LEAVE", chat_number);
        }
    }

    @Override
    protected void setId() {
        menu = findViewById(R.id.layout_main__menu);
    }

    @Override
    protected void setListener() {
        menu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                transaction = manager.beginTransaction();

                for (Layout layout : layouts) {
                    if (item.getItemId() == layout.id) {
                        transaction.show(layout.layout);
                    } else {
                        transaction.hide(layout.layout);
                    }
                }

                transaction.commit();
                return true;
            }
        });
        ((SettingLayout) layouts[3].layout).listener = new SettingLayout.Listener() {
            @Override
            public void exit() {
                finish();
            }
        };
    }

    private static class Layout {
        public Fragment layout;
        public int id;

        public Layout(Fragment layout, int id) {
            this.layout = layout;
            this.id = id;
        }
    }
}
