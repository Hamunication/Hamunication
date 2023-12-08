package com.dx3evm.hamunication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class AdminMainActivity extends AppCompatActivity {

    CardView cardViewCourses, cardViewNews, cardViewAccounts, cardViewSettings, cardViewLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        cardViewCourses = findViewById(R.id.cardViewCourses);
        cardViewNews = findViewById(R.id.cardViewNews);
        cardViewAccounts = findViewById(R.id.cardViewAccounts);
        cardViewSettings = findViewById(R.id.cardViewSettings);
        cardViewLogout = findViewById(R.id.cardViewLogout);

        cardViewCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminMainActivity.this, CreateCourseActivity.class));
            }
        });


        cardViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AdminMainActivity.this, LoginActivity.class));
            }
        });
    }
}