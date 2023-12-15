package com.dx3evm.hamunication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.dx3evm.hamunication.Fragments.CourseFragment;
import com.dx3evm.hamunication.Fragments.DashboardFragment;
import com.dx3evm.hamunication.Fragments.MessageFragment;
import com.dx3evm.hamunication.Fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class UserMainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, new DashboardFragment())
                .commit();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener((NavigationBarView.OnItemSelectedListener) item -> {
            Fragment selectedFragment = null;
            if(item.getItemId() == R.id.menu_dashboard){
                selectedFragment = new DashboardFragment();
            }else if (item.getItemId() == R.id.menu_courses){
                selectedFragment = new CourseFragment();
            }else if(item.getItemId() == R.id.menu_account){
                selectedFragment = new ProfileFragment();
            }

            if(selectedFragment != null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, selectedFragment)
                        .commit();
            }
            return true;
        });
    }
}