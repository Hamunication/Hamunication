package com.dx3evm.hamunication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dx3evm.hamunication.Models.Course;
import com.dx3evm.hamunication.Models.Module;
import com.dx3evm.hamunication.Models.Topic;
import com.google.android.material.button.MaterialButton;

public class ViewTopicActivity extends AppCompatActivity {

    TextView tvEditTopic, tvTopicName, tvTopicDescription;

    Course course = null;
    Module module = null;
    Topic topic = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_topic);

        tvEditTopic = findViewById(R.id.tvEditTopic);
        tvTopicName = findViewById(R.id.tvTopicName);
        tvTopicDescription = findViewById(R.id.tvTopicDescription);

        course = (Course) getIntent().getSerializableExtra("course");
        module = (Module) getIntent().getSerializableExtra("module");
        topic = (Topic) getIntent().getSerializableExtra("topic");

        if (topic != null) {
            tvEditTopic.setText(topic.getTopicTitle());
            tvTopicName.setText(topic.getTopicTitle());
            tvTopicDescription.setText(topic.getTopicDescription());
        } else {

        }
    }


}