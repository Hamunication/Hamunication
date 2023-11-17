package com.dx3evm.hamunication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dx3evm.hamunication.Models.Course;
import com.dx3evm.hamunication.Models.Module;
import com.dx3evm.hamunication.Models.Topic;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EditTopicActivity extends AppCompatActivity {

    EditText etTopicName, etTopicDescription;

    MaterialButton mtrlBtnSave;

    Course course = null;
    Module module = null;
    Topic topic = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_topic);

        etTopicName = findViewById(R.id.etTopicName);
        etTopicDescription = findViewById(R.id.etTopicDescription);
        mtrlBtnSave = findViewById(R.id.btnSave);

        course = (Course) getIntent().getSerializableExtra("course");
        module = (Module) getIntent().getSerializableExtra("module");
        topic = (Topic) getIntent().getSerializableExtra("topic");

        if (topic != null) {
            etTopicName.setText(topic.getTopicTitle());
            etTopicDescription.setText(topic.getTopicDescription());
        } else {
            etTopicDescription.setHeight(300);
        }

        mtrlBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTopic(etTopicName.getText().toString(), etTopicDescription.getText().toString());
            }
        });
    }

    public void createTopic(String title, String description){
        if(topic == null){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(course.getId()).child("Module").child(module.getId());

            Map<String, String> topicData = new HashMap<>();
            topicData.put("Title", title);
            topicData.put("Description", description);

            databaseReference.child("Topic").push().setValue(topicData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(EditTopicActivity.this, "Topic Created!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });


        }
    }
}