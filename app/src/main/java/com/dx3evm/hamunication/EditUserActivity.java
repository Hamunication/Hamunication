package com.dx3evm.hamunication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dx3evm.hamunication.Dialogs.DeleteAccountDialog;
import com.dx3evm.hamunication.Models.Account;
import com.dx3evm.hamunication.Models.Course;
import com.dx3evm.hamunication.Models.Module;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditUserActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;

    Account account = null;

    DeleteAccountDialog deleteAccountDialog;
    TextInputEditText etFullName, etEmail, etPassword;
    MaterialCheckBox mtrlChkBoxAdmin;

    MaterialButton mtrlBtnSaveChanges, mtrlBtnDeleteAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        firebaseDatabase = FirebaseDatabase.getInstance();

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        mtrlChkBoxAdmin = findViewById(R.id.mtrlChkBoxAdmin);
        mtrlBtnSaveChanges = findViewById(R.id.mtrlBtnSaveChanges);
        mtrlBtnDeleteAccount = findViewById(R.id.mtrlBtnDeleteAccount);

        mtrlBtnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fullName = etFullName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                boolean admin = mtrlChkBoxAdmin.isChecked();

                saveChanges(account.getId(), fullName, email, password, admin);
            }
        });

        mtrlBtnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditUserActivity.this);
                builder.setTitle("Delete Account")
                        .setMessage("Are you sure? This will delete " + account.getFullName() + " account.")
                        .setPositiveButton("Delete Account", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAccount(account.getId());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // User clicked the Cancel button, do nothing or handle accordingly
                            }
                        }).show();
            }
        });

        getUserDetails();
    }

    public void getUserDetails(){

        if(getIntent().hasExtra("user_details")){
            account = (Account) getIntent().getSerializableExtra("user_details");

            if(account != null){
               etFullName.setText(account.getFullName());
               etEmail.setText(account.getEmail());
               etPassword.setText(account.getPassword());
               mtrlChkBoxAdmin.setChecked(account.isAdmin());
            }
        }

    }

    public void saveChanges(String userId, String fullName, String email, String password, boolean admin){
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users").child(userId);
        Map<String, Object> updatedUserData = new HashMap<>();
        updatedUserData.put("FullName", fullName);
        updatedUserData.put("Email", email);
        updatedUserData.put("Password", password);
        updatedUserData.put("isAdmin", admin);

        databaseReference.updateChildren(updatedUserData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(EditUserActivity.this, "Account successfully updated!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void deleteAccount(String userId){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(EditUserActivity.this, "Account deleted Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditUserActivity.this, "Error deleting account!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}