package com.dx3evm.hamunication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.dx3evm.hamunication.Models.Course;
import com.dx3evm.hamunication.Models.Module;
import com.dx3evm.hamunication.Models.Topic;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ViewTopicActivity extends AppCompatActivity {

    String currentUserFullName = "";
    FirebaseAuth firebaseAuth;
    RelativeLayout mediaFrameLayout;
    TextView tvBack, tvTopicName, tvTopicDescription;
    Course course = null;
    Module module = null;
    Topic topic = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_topic);
        
        firebaseAuth = FirebaseAuth.getInstance();

        getUserDetails();

        mediaFrameLayout = findViewById(R.id.mediaFrameLayout);
        tvTopicName = findViewById(R.id.tvTopicName);
        tvTopicDescription = findViewById(R.id.tvTopicDescription);
        tvBack = findViewById(R.id.tvBack);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        course = (Course) getIntent().getSerializableExtra("course");
        module = (Module) getIntent().getSerializableExtra("module");
        topic = (Topic) getIntent().getSerializableExtra("topic");



        if (topic != null) {
            tvTopicName.setText(topic.getTopicTitle());
            tvTopicDescription.setText(topic.getTopicDescription());

            for(Map.Entry<String, Map<String, String>> list : topic.getUrlList().entrySet()){
                Map<String, String> mediaValues = list.getValue();

                String mediaLink = mediaValues.get("link");
                String mediaType = mediaValues.get("type");

                if(mediaType.equals("video")){
                    Toast.makeText(this, "Video: " + mediaLink, Toast.LENGTH_SHORT).show();
                    displayVideoFromURL(mediaLink);
                }else if(mediaType.equals("image")){
                    displayImageFromURL(mediaLink);
                }else{
                    Toast.makeText(this, "Invalid File Type", Toast.LENGTH_SHORT).show();
                }
            }

        } else {

        }
    }
    private void displayImageFromURL(String imageUrl){
        ImageView imageView = new ImageView(this);
        imageView.setId(View.generateViewId());
        RelativeLayout.LayoutParams imageLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );


        Glide.with(this).load(imageUrl).into(imageView);
        imageView.setLayoutParams(imageLayoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        if (mediaFrameLayout.getChildCount() > 0) {
            int imageId = mediaFrameLayout.getChildAt(mediaFrameLayout.getChildCount() - 1).getId();
            imageLayoutParams.addRule(RelativeLayout.BELOW, imageId);

            imageLayoutParams.setMargins(0, 100, 0, 0);
        }

        mediaFrameLayout.addView(imageView);
        mediaFrameLayout.setVisibility(View.VISIBLE);

    }

    public void displayVideoFromURL(String videoUrl){
        Toast.makeText(this, "Video Method Area", Toast.LENGTH_SHORT).show();
        VideoView videoView = new VideoView(this);
        videoView.setId(View.generateViewId());
        RelativeLayout.LayoutParams videoLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        if (mediaFrameLayout.getChildCount() > 0) {
            int previousVideoId = mediaFrameLayout.getChildAt(mediaFrameLayout.getChildCount() - 1).getId();
            videoLayoutParams.addRule(RelativeLayout.BELOW, previousVideoId);

            videoLayoutParams.setMargins(0, 100, 0, 0);
        }

        videoView.setLayoutParams(videoLayoutParams);
        videoView.setVideoURI(Uri.parse(videoUrl));

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        mediaFrameLayout.addView(videoView);
        mediaFrameLayout.setVisibility(View.VISIBLE);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

            }
        });

    }

    public void markAsComplete(String topicId){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Courses").child(course.getId()).child("Module").child(module.getId()).child("Topic").child(topicId);
        
        databaseReference.child("CompletedUsers").child(firebaseAuth.getCurrentUser().getUid()).setValue(currentUserFullName).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });
    }

    public void getUserDetails(){
        if(firebaseAuth.getCurrentUser() != null){
            String currentUser = firebaseAuth.getCurrentUser().getUid();

            DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference().child("Users");

            usersReference.child(currentUser).child("FullName").addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    currentUserFullName = snapshot.getValue(String.class);
                    markAsComplete(topic.getTopicID());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void checkIfCompleted(String userId) {
        DatabaseReference completedUsersReference = FirebaseDatabase.getInstance().getReference()
                .child("Courses").child(course.getId()).child("Module").child(module.getId())
                .child("Topic").child(topic.getTopicID()).child("CompletedUsers");

        completedUsersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot userSnapshot : snapshot.getChildren()){
                        String id = userSnapshot.getKey();
                        
                        if(id.equals(userId)){
                            Toast.makeText(ViewTopicActivity.this, "Topic Completed", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(ViewTopicActivity.this, "Topic not completed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
               
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if needed
            }
        });
    }


}