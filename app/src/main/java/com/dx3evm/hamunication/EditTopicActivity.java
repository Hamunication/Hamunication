package com.dx3evm.hamunication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.dx3evm.hamunication.Models.Course;
import com.dx3evm.hamunication.Models.Module;
import com.dx3evm.hamunication.Models.Topic;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class EditTopicActivity extends AppCompatActivity {

    RelativeLayout parentContainer;
    EditText etTopicName, etTopicDescription;
    MaterialButton mtrlBtnSave;
    RelativeLayout mediaFrameLayout;
    LinearLayout loadingLayout;

    MaterialButton btnAddImageVideo;

    TextView tvProgressLabel;

    Course course = null;
    Module module = null;
    Topic topic = null;

    private static final int PICK_IMAGE_REQUEST = 123;
    private static final int PICK_VIDEO_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_topic);

        parentContainer = findViewById(R.id.parentContainer);
        loadingLayout = findViewById(R.id.loadingLayout);
        etTopicName = findViewById(R.id.etTopicName);
        etTopicDescription = findViewById(R.id.etTopicDescription);
        mtrlBtnSave = findViewById(R.id.btnSave);
        mediaFrameLayout = findViewById(R.id.mediaFrameLayout);
        btnAddImageVideo = findViewById(R.id.btnAddImageVideo);
        tvProgressLabel = findViewById(R.id.tvProgressLabel);
        

        course = (Course) getIntent().getSerializableExtra("course");
        module = (Module) getIntent().getSerializableExtra("module");
        topic = (Topic) getIntent().getSerializableExtra("topic");

        if (topic != null) {
            etTopicName.setText(topic.getTopicTitle());

            Map<String, Map<String, String>> resources = topic.getUrlList();
            for (Map.Entry<String, Map<String, String>> list : topic.getUrlList().entrySet()) {
                Map<String, String> mediaValues = list.getValue();

                etTopicDescription.setText(topic.getTopicDescription());
                etTopicDescription.setHeight(500);

                String mediaLink = mediaValues.get("link");
                String mediaType = mediaValues.get("type");

                if (mediaType.equals("video")) {
                    Toast.makeText(this, "Video: " + mediaLink, Toast.LENGTH_SHORT).show();
                    displayVideoFromURL(mediaLink);
                } else if (mediaType.equals("image")) {
                    Toast.makeText(this, "Image: " + mediaLink, Toast.LENGTH_SHORT).show();
                    displayImageFromURL(mediaLink);
                    // Handle image case
                } else {
                    Toast.makeText(this, "Invalid File Type", Toast.LENGTH_SHORT).show();
                }
            }
        }

        mtrlBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout mediaFrameLayout = findViewById(R.id.mediaFrameLayout);
                Map<String, String> videoList = new HashMap<>();
                Map<String, String> imageList = new HashMap<>();
                Map<String, Map> linkList = new HashMap<>();
                AtomicInteger uploadCounter = new AtomicInteger(0);

                for(int i = 0; i < mediaFrameLayout.getChildCount(); i++){
                    View childView = mediaFrameLayout.getChildAt(i);
                    if(childView instanceof VideoView){
                        VideoView videoView = (VideoView) childView;
                        try {
                            Field mUriField = VideoView.class.getDeclaredField("mUri");
                            mUriField.setAccessible(true);
                            Uri videoUri = (Uri) mUriField.get(videoView);

                            if(videoUri != null){
                                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                                StorageReference videoReference = storageReference.child("videos/" + System.currentTimeMillis() + ".mp4");

                                UploadTask uploadTask = videoReference.putFile(videoUri);
                                uploadTask.addOnProgressListener(taskSnapshot -> {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                    disableInteractions();
                                    videoView.setEnabled(false);
                                    tvProgressLabel.setText("Uploading Images... " + (int)progress +"%");
                                });

                                uploadTask.addOnSuccessListener(taskSnapshot -> {
                                    if(taskSnapshot.getTask().isSuccessful()){
                                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                                            String downloadUrl = uri.toString();
                                            videoList.put("type","video");
                                            videoList.put("link", downloadUrl);
                                            linkList.put(String.valueOf(System.currentTimeMillis()), videoList);
                                            uploadCounter.incrementAndGet();

                                            if (uploadCounter.get() == mediaFrameLayout.getChildCount()) {
                                                // All uploads completed, call createTopic here
                                                createTopic(etTopicName.getText().toString(), etTopicDescription.getText().toString(), linkList);
                                                enableInteractions();
                                            }
                                        });
                                    }

                                });
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else if(childView instanceof ImageView){
                        ImageView imageView = (ImageView) childView;
                        try{
                            Field mUriField = ImageView.class.getDeclaredField("mUri");
                            mUriField.setAccessible(true);
                            Uri imageUri = (Uri) mUriField.get(imageView);

                            if(imageUri != null){
                                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                                StorageReference imageReference = storageReference.child("images/" + System.currentTimeMillis() + ".png");

                                UploadTask uploadTask = imageReference.putFile(imageUri);

                                uploadTask.addOnProgressListener(taskSnapshot -> {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                    disableInteractions();
                                    imageView.setEnabled(false);
                                    tvProgressLabel.setText("Uploading Videos... " + (int)progress +"%");
                                });

                                uploadTask.addOnSuccessListener(taskSnapshot -> {
                                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                                       String downloadUrl = uri.toString();
                                       imageList.put("type", "image");
                                       imageList.put("link", downloadUrl);
                                       linkList.put(String.valueOf(System.currentTimeMillis()), imageList);
                                       uploadCounter.incrementAndGet();

                                       if(uploadCounter.get() == mediaFrameLayout.getChildCount()){
                                           createTopic(etTopicName.getText().toString(), etTopicDescription.getText().toString(), linkList);
                                           enableInteractions();
                                       }

                                    });
                                });
                            }

                        }catch (Exception ex){
                            Toast.makeText(EditTopicActivity.this, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });


        btnAddImageVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickMedia();
            }
        });
    }

    public void pickMedia(){
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/* video/*");
        startActivityForResult(pickIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            assert data != null;
            Uri selectedMedia = data.getData();

            assert selectedMedia != null;
            String mimeType = getContentResolver().getType(selectedMedia);

            if (mimeType != null) {
                if (mimeType.startsWith("image/")) {
                    // Selected content is an image
                    displayImage(selectedMedia);
                } else if (mimeType.startsWith("video/")) {
                    // Selected content is a video
                    displayVideo(selectedMedia);
                }
            }
        }
    }

    private void displayImage(Uri imageUri) {
        GradientDrawable border = new GradientDrawable();
        border.setColor(Color.TRANSPARENT); // You can set a background color here if needed
        border.setStroke(5, Color.BLUE);

        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(imageUri);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);

            int imageWidth = options.outWidth;
            int imageHeight = options.outHeight;

            // Calculate the desired height based on the image width and the desired width (e.g., 500 pixels)
            int desiredHeight = (int) (500 * ((float) imageHeight / imageWidth));

            // Set the calculated height to the ImageView
            RelativeLayout.LayoutParams imageLayoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    desiredHeight
            );

            ImageView imageView = new ImageView(this);
            imageView.setId(View.generateViewId());
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFile(imageUri.getPath(), options);

            imageView.setImageURI(imageUri);
            imageView.setLayoutParams(imageLayoutParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setBackground(border);

            if (mediaFrameLayout.getChildCount() > 0) {
                int imageId = mediaFrameLayout.getChildAt(mediaFrameLayout.getChildCount() - 1).getId();
                imageLayoutParams.addRule(RelativeLayout.BELOW, imageId);

                imageLayoutParams.setMargins(0, 100, 0, 0);
            }

            mediaFrameLayout.addView(imageView);
            mediaFrameLayout.setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
    }

    private void displayVideo(Uri videoUri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(this, videoUri);

        String videoHeight =  retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        String videoWidth = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);

        GradientDrawable border = new GradientDrawable();
        border.setColor(Color.TRANSPARENT); // You can set a background color here if needed
        border.setStroke(5, Color.BLUE);

        VideoView videoView = new VideoView(this);
        videoView.setId(View.generateViewId());
        RelativeLayout.LayoutParams videoLayoutParams = new RelativeLayout.LayoutParams(
                Integer.parseInt(videoWidth),
                Integer.parseInt(videoHeight)
        );

        if (mediaFrameLayout.getChildCount() > 0) {
            int previousVideoId = mediaFrameLayout.getChildAt(mediaFrameLayout.getChildCount() - 1).getId();
            videoLayoutParams.addRule(RelativeLayout.BELOW, previousVideoId);

            videoLayoutParams.setMargins(0, 100, 0, 0);
        }

        videoView.setLayoutParams(videoLayoutParams);
        videoView.setBackground(border);
        videoView.setVideoURI(Uri.parse(videoUri.toString()));


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

    public void createTopic(String title, String description, Map<String, Map> mediaUrl){
        if(topic == null){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(course.getId()).child("Module").child(module.getId());

            Map<String, Object> topicData = new HashMap<>();
            topicData.put("Title", title);
            topicData.put("Description", description);
            topicData.put("Media", mediaUrl);

            databaseReference.child("Topic").push().setValue(topicData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(EditTopicActivity.this, "Topic Created Successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }
    }

    public void disableInteractions(){
        etTopicName.setEnabled(false);
        etTopicDescription.setEnabled(false);
        mtrlBtnSave.setEnabled(false);
        btnAddImageVideo.setEnabled(false);
        loadingLayout.setVisibility(View.VISIBLE);
    }

    public void enableInteractions(){
        etTopicName.setEnabled(true);
        etTopicDescription.setEnabled(true);
        mtrlBtnSave.setEnabled(true);
        btnAddImageVideo.setEnabled(true);
        loadingLayout.setVisibility(View.GONE);
    }

}