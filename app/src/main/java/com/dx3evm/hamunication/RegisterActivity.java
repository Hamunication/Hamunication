package com.dx3evm.hamunication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String fullName, email, password, confirmPassword;

    EditText etFullName, etEmail, etPassword, etConfirmPassword;
    MaterialButton mtrlBtnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        mtrlBtnCreateAccount = findViewById(R.id.mtrlBtnCreateAccount);


        mtrlBtnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullName = etFullName.getText().toString();
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                confirmPassword = etConfirmPassword.getText().toString();

                createUserAccount(fullName, email, password);
            }
        });
    }

    public void createUserAccount(String fullName, String email, String password){

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task ->{
            if(task.isSuccessful()){
                String userId = firebaseAuth.getCurrentUser().getUid();
                Map<String, String> newUser = new HashMap<>();
                newUser.put("FullName", fullName);
                newUser.put("Email", email);
                newUser.put("Password", password);

                DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(userId);

                databaseReference.setValue(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(RegisterActivity.this, "Account Created Successfully!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Error: Account Creation", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{

            }
        });
    }
}