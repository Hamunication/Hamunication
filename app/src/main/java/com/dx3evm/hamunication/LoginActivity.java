package com.dx3evm.hamunication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    EditText etEmail, etPasword;
    MaterialButton btnSignIn;
    TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPasword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvRegister = findViewById(R.id.tvRegister);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(etEmail.getText().toString(), etPasword.getText().toString());
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    public void signIn(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if(task.isSuccessful()){

               DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("isAdmin");
               databaseReference.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       boolean isAdmin = Boolean.TRUE.equals(snapshot.getValue(Boolean.class));
                       if(isAdmin){
                           startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));
                       }
                       else{
                           startActivity(new Intent(LoginActivity.this, UserMainActivity.class));
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
            }
            else{
                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}