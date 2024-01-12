package com.dx3evm.hamunication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dx3evm.hamunication.Adapters.AdminMenuAdapter;
import com.dx3evm.hamunication.Models.AdminMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminMainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    List<AdminMenu> adminMenuList;

    AdminMenuAdapter adminMenuAdapter;

    TextView tvUsername, tvEmail;
    ImageButton imgBtnLogout;
    RecyclerView rvAdminSelections;

    CoordinatorLayout profileLayout;

    LinearLayout loadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();

        adminMenuList = new ArrayList<>();

        imgBtnLogout = findViewById(R.id.imgBtnLogout);

        tvUsername = findViewById(R.id.tvUsername);

        tvEmail = findViewById(R.id.tvEmail);

        rvAdminSelections = findViewById(R.id.rvAdminSelections);

        profileLayout = findViewById(R.id.profileLayout);

        loadingLayout = findViewById(R.id.loadingLayout);

        getUserDetails();

        imgBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(AdminMainActivity.this, LoginActivity.class));
                finish();
                Toast.makeText(AdminMainActivity.this, "Click", Toast.LENGTH_SHORT).show();
            }
        });

        adminMenuList.add(new AdminMenu(R.drawable.ic_menu_course, "Courses"));
        adminMenuList.add(new AdminMenu(R.drawable.ic_menu_news, "News"));
        adminMenuList.add(new AdminMenu(R.drawable.ic_menu_accounts, "Accounts"));


        adminMenuAdapter = new AdminMenuAdapter(adminMenuList);
        adminMenuAdapter.setOnClickListener(new AdminMenuAdapter.OnClickListener() {
            @Override
            public void onClick(int position, AdminMenu adminMenu) {
                if(adminMenu.getTitle().equals("Courses")){
                    startActivity(new Intent(AdminMainActivity.this, CreateCourseActivity.class));
                }
                else if(adminMenu.getTitle().equals("News")){
                    startActivity(new Intent(AdminMainActivity.this, CreateNewsActivity.class));
                }
                else if(adminMenu.getTitle().equals("Accounts")){
                    startActivity(new Intent(AdminMainActivity.this, AccountsActivity.class));
                }
                else{

                }
            }
        });

        rvAdminSelections.setLayoutManager(new LinearLayoutManager(this));
        rvAdminSelections.setAdapter(adminMenuAdapter);

    }

    public void getUserDetails() {
        if(firebaseAuth.getCurrentUser() != null){
            DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(firebaseAuth.getCurrentUser().getUid());
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String fullName = snapshot.child("FullName").getValue(String.class);
                        String email = snapshot.child("Email").getValue(String.class);

                        tvUsername.setText(fullName);
                        tvEmail.setText(email);

                        loadingLayout.setVisibility(View.GONE);
                        profileLayout.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}