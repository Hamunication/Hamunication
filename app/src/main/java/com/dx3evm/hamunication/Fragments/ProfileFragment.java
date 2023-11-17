package com.dx3evm.hamunication.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dx3evm.hamunication.LoginActivity;
import com.dx3evm.hamunication.R;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth fAuth;
    RelativeLayout profileLayout;

    LinearLayout loadingLayout;
    TextView tvFullName, tvEmail;
    com.google.android.material.button.MaterialButton btnSignOut;



    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_profile, container, false);

        fAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        profileLayout = fragmentView.findViewById(R.id.profileLayout);
        loadingLayout = fragmentView.findViewById(R.id.loadingLayout);
        tvFullName = fragmentView.findViewById(R.id.tvFullName);
        tvEmail = fragmentView.findViewById(R.id.tvEmail);
        btnSignOut = fragmentView.findViewById(R.id.btnSignOut);

        getUserDetails();

        btnSignOut.setOnClickListener(view -> {
            fAuth.signOut();
            startActivity(new Intent(view.getContext().getApplicationContext(), LoginActivity.class));
            getActivity().finish();
        });

        return fragmentView;
    }

    public void getUserDetails() {
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(fAuth.getCurrentUser().getUid());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String fullName = snapshot.child("FullName").getValue(String.class);
                    String email = snapshot.child("Email").getValue(String.class);

                    tvFullName.setText(fullName);
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