package com.yeungjin.translogic.display.main;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.yeungjin.translogic.R;

public class Layout extends AppCompatActivity {
   private Layout.Display displays[] = {
           new Layout.Display(new com.yeungjin.translogic.display.user.Layout(), R.id.display_main_layout__menu_icon__user),
           new Layout.Display(new com.yeungjin.translogic.display.chat.Layout(), R.id.display_main_layout__menu_icon__chat),
           new Layout.Display(new com.yeungjin.translogic.display.task.Layout(), R.id.display_main_layout__menu_icon__task),
           new Layout.Display(new com.yeungjin.translogic.display.setting.Layout(), R.id.display_main_layout__menu_icon__setting)
   };
   private FragmentManager manager = getSupportFragmentManager();
   private FragmentTransaction transaction = manager.beginTransaction();

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.display_main_layout);

      for (Layout.Display display : displays) {
         transaction.add(R.id.display_main_layout__screen, display.layout);
         transaction.hide(display.layout);
      }
      transaction.show(displays[0].layout).commit();

      BottomNavigationView menu = (BottomNavigationView) findViewById(R.id.display_main_layout__menu);
      menu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
         @Override
         public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            transaction = manager.beginTransaction();

            for (Layout.Display display : displays) {
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
