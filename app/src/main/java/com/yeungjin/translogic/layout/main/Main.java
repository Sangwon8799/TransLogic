package com.yeungjin.translogic.layout.main;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.yeungjin.translogic.R;
import com.yeungjin.translogic.layout.chat.Chat;
import com.yeungjin.translogic.layout.employee.Employee;
import com.yeungjin.translogic.layout.setting.Setting;
import com.yeungjin.translogic.layout.task.Task;

public class Main extends AppCompatActivity {
   private Main.Display displays[] = {
           new Main.Display(new Employee(), R.id.layout_main_main__menu_icon__user),
           new Main.Display(new Chat(), R.id.layout_main_main__menu_icon__chat),
           new Main.Display(new Task(), R.id.layout_main_main__menu_icon__task),
           new Main.Display(new Setting(), R.id.layout_main_main__menu_icon__setting)
   };
   private FragmentManager manager = getSupportFragmentManager();
   private FragmentTransaction transaction = manager.beginTransaction();

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.layout_main_main);

      for (Main.Display display : displays) {
         transaction.add(R.id.layout_main_main__screen, display.layout);
         transaction.hide(display.layout);
      }
      transaction.show(displays[0].layout).commit();

      BottomNavigationView menu = (BottomNavigationView) findViewById(R.id.layout_main_main__menu);
      menu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
         @Override
         public boolean onNavigationItemSelected(MenuItem item) {
            transaction = manager.beginTransaction();

            for (Main.Display display : displays) {
               if (item.getItemId() == display.id) {
                  transaction.show(display.layout);
               } else {
                  transaction.hide(display.layout);
               }
            }

            transaction.commit();
            return true;
         }
      });
   }

   private class Display {
      public Fragment layout;
      public int id;

      public Display(Fragment layout, int id) {
         this.layout = layout;
         this.id = id;
      }
   }
}
