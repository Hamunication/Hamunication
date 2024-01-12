package com.dx3evm.hamunication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dx3evm.hamunication.Models.Account;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    TextInputEditText etFullName, etEmail, etPassword;
    MaterialCheckBox mtrlChkBoxAdmin;

    MaterialButton mtrlBtnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        mtrlChkBoxAdmin = findViewById(R.id.mtrlChkBoxAdmin);
        mtrlBtnCreateAccount = findViewById(R.id.mtrlBtnCreateAccount);


        mtrlBtnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fullName = etFullName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                boolean admin = mtrlChkBoxAdmin.isChecked();

                createNewAccount(fullName, email, password, admin);
            }
        });

    }

    public void createNewAccount(String fullName, String email, String password, boolean admin){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                FirebaseUser newUser = task.getResult().getUser();

                Map<String, Object> newUserData = new HashMap<>();

                String userId = newUser.getUid();
                newUserData.put("FullName", fullName);
                newUserData.put("Email", email);
                newUserData.put("Password", password);
                newUserData.put("isAdmin", admin);

                DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users").child(userId);
                databaseReference.setValue(newUserData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(CreateAccountActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateAccountActivity.this, "Error Creating account", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}