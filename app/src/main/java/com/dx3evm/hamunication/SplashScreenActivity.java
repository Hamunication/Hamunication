package com.dx3evm.hamunication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //FirebaseAuth.getInstance().signOut();
                authenticateUser();
            }
        }, 2000);
    }

    public void authenticateUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("isAdmin");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean isAdmin = Boolean.TRUE.equals(snapshot.getValue(Boolean.class));
                    if(isAdmin){
                        startActivity(new Intent(SplashScreenActivity.this, AdminMainActivity.class));
                        finish();
                    }
                    else{
                        startActivity(new Intent(SplashScreenActivity.this, UserMainActivity.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
            finish();
        }

    }
}