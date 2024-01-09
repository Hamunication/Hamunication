package com.dx3evm.hamunication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateNewsActivity extends AppCompatActivity {

    String currentUserFullName = "";
    FirebaseAuth firebaseAuth;
    ImageView ivNewsImg;
    EditText etNewsTitle, etNewsDescription;
    MaterialButton mtrlBtnSaveNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_news);

        firebaseAuth = FirebaseAuth.getInstance();

        getUser();

        etNewsTitle = findViewById(R.id.etNewsTitle);
        etNewsDescription = findViewById(R.id.etNewsDescription);
        ivNewsImg = findViewById(R.id.ivNewsImg);
        mtrlBtnSaveNews = findViewById(R.id.mtrlBtnSaveNews);

        ivNewsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 1);
            }
        });

        mtrlBtnSaveNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(firebaseAuth.getCurrentUser() != null){
                        getUser();

                        Date date = new Date();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy MMM dd");

                        Field mUriField = ImageView.class.getDeclaredField("mUri");
                        mUriField.setAccessible(true);

                        Uri imageUri = (Uri) mUriField.get(ivNewsImg);
                        String newsTitle = etNewsTitle.getText().toString();
                        String newsDescription = etNewsDescription.getText().toString();
                        String formattedDate = simpleDateFormat.format(date);

                        createNews(String.valueOf(imageUri), newsTitle, newsDescription, currentUserFullName, formattedDate);

                    }

                }catch (Exception ex){

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            ivNewsImg.setImageURI(selectedImage);
        }

    }

    public void createNews(String newsImage, String newsTitle, String newsDescription, String newsEditor, String newsTime){
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference imageReference = storage.getReference().child("news_images/" + newsTitle + "-" + System.currentTimeMillis() + ".png");

        UploadTask uploadTask = imageReference.putFile(Uri.parse(newsImage));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("News");

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUrl = uri.toString();

                        Map<String, Object> newsList = new HashMap<>();
                        newsList.put("newsImage", downloadUrl);
                        newsList.put("newsTitle", newsTitle);
                        newsList.put("newsDescription", newsDescription);
                        newsList.put("newsEditor", newsEditor);
                        newsList.put("newsTime", newsTime);

                        databaseReference.push().setValue(newsList).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(CreateNewsActivity.this, "News has been published.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CreateNewsActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

    public void getUser(){
        if(firebaseAuth.getCurrentUser() != null){
            String currentUser = firebaseAuth.getCurrentUser().getUid();

            DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference().child("Users");

            usersReference.child(currentUser).child("FullName").addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    currentUserFullName = snapshot.getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}