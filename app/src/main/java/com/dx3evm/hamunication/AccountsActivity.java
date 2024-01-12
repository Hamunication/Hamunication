package com.dx3evm.hamunication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dx3evm.hamunication.Adapters.AccountAdapter;
import com.dx3evm.hamunication.Models.Account;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AccountsActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;

    List<Account> accountsList;

    private RecyclerView rvUserAccounts;
    private AccountAdapter accountAdapter;

    MaterialButton mtrlBtnNewAccount;

    public static final String NEXT_SCREEN = "user_details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        firebaseDatabase = FirebaseDatabase.getInstance();
        accountsList = new ArrayList<>();
        rvUserAccounts = findViewById(R.id.rvUserAccounts);
        mtrlBtnNewAccount = findViewById(R.id.mtrlBtnNewAccount);

        accountAdapter = new AccountAdapter(AccountsActivity.this, accountsList);
        rvUserAccounts.setLayoutManager(new LinearLayoutManager(this));
        rvUserAccounts.setAdapter(accountAdapter);

        mtrlBtnNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountsActivity.this, CreateAccountActivity.class));
            }
        });
        accountAdapter.setOnClickListener(new AccountAdapter.OnClickListener() {
            @Override
            public void OnClick(int position, Account account) {
                Intent intent = new Intent(AccountsActivity.this, EditUserActivity.class);

                intent.putExtra(NEXT_SCREEN, account);
                startActivity(intent);
            }
        });

        getAccounts();

    }

    public void getAccounts(){
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                accountsList.clear();
                for(DataSnapshot userSnapshot : snapshot.getChildren()){

                    if(userSnapshot.exists()){
                        String userId = userSnapshot.getKey();
                        String fullName = userSnapshot.child("FullName").getValue(String.class);
                        String email = userSnapshot.child("Email").getValue(String.class);
                        String password = userSnapshot.child("Password").getValue(String.class);
                        Boolean isAdmin = userSnapshot.child("isAdmin").getValue(Boolean.class);

                        Account account = new Account();

                        account.setId(userId);
                        account.setFullName(fullName);
                        account.setEmail(email);
                        account.setPassword(password);
                        account.setAdmin(isAdmin);

                        accountsList.add(account);
                    }
                }
                accountAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}