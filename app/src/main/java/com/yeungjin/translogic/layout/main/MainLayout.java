package com.yeungjin.translogic.layout.main;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

public class MainLayout extends AppCompatActivity {
    private final Layout[] layouts = {
            new Layout(new EmployeeLayout(), R.id.layout_main_main__menu_icon__user),
            new Layout(new ChatLayout(), R.id.layout_main_main__menu_icon__chat),
            new Layout(new TaskLayout(), R.id.layout_main_main__menu_icon__task),
            new Layout(new SettingLayout(), R.id.layout_main_main__menu_icon__setting)
    };
    private final FragmentManager manager = getSupportFragmentManager();
    private FragmentTransaction transaction = manager.beginTransaction();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_main);

        for (Layout layout : layouts) {
            transaction.add(R.id.layout_main_main__screen, layout.layout);
            transaction.hide(layout.layout);
        }
        transaction.show(layouts[0].layout).commit();

        BottomNavigationView menu = (BottomNavigationView) findViewById(R.id.layout_main_main__menu);
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
