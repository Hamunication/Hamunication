package com.dx3evm.hamunication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dx3evm.hamunication.Adapters.TopicAdapter;
import com.dx3evm.hamunication.Models.Course;
import com.dx3evm.hamunication.Models.Module;
import com.dx3evm.hamunication.Models.Topic;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EditModuleActivity extends AppCompatActivity {

    TopicAdapter topicAdapter;

    TextView tvModuleName;

    List<Topic> topicList;

    MaterialButton mtrlBtnAddTopic, mtrlBtnAddQuiz;

    RecyclerView rvTopicList;

    Course course = null;
    Module module = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_module);

        tvModuleName = findViewById(R.id.tvModuleName);
        mtrlBtnAddTopic = findViewById(R.id.mtrlBtnAddTopic);
        mtrlBtnAddQuiz = findViewById(R.id.mtrlBtnAddQuiz);
        rvTopicList = findViewById(R.id.rvTopicList);

        topicList = new ArrayList<>();
        topicAdapter = new TopicAdapter(this, topicList);

        topicAdapter.setOnClickListener(new TopicAdapter.OnClickListener() {
            @Override
            public void onClick(int position, Topic topic) {
                Intent intent = new Intent(EditModuleActivity.this, EditTopicActivity.class);
                intent.putExtra("course", course);
                intent.putExtra("module", module);
                intent.putExtra("topic", topic);

                startActivity(intent);
            }
        });

        rvTopicList.setLayoutManager(new LinearLayoutManager(this));
        rvTopicList.setAdapter(topicAdapter);

        mtrlBtnAddQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditModuleActivity.this, EditQuizActivity.class));
            }
        });

        mtrlBtnAddTopic.setOnClickListener(view -> {
            Intent intent = new Intent(EditModuleActivity.this, EditTopicActivity.class);
            intent.putExtra("course", course);
            intent.putExtra("module", module);

            startActivity(intent);
        });

        if(getIntent().hasExtra("module")){
            module = (Module) getIntent().getSerializableExtra("module");
            course = (Course) getIntent().getSerializableExtra("course");

            if(module != null){
                tvModuleName.setText(module.getTitle());
                displayTopic(course.getId(), module.getId());
            }
        }
    }
    public void displayTopic(String courseID, String moduleID){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(courseID).child("Module").child(moduleID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    for(DataSnapshot topicSnapshot : snapshot.child("Topic").getChildren()){
                        String topicID = topicSnapshot.getKey();
                        String topicTitle = topicSnapshot.child("Title").getValue(String.class);
                        String topicDescription = topicSnapshot.child("Description").getValue(String.class);

                        Topic topic = new Topic();
                        topic.setTopicID(topicID);
                        topic.setTopicTitle(topicTitle);
                        topic.setTopicDescription(topicDescription);

                        topicList.add(topic);
                    }
                    topicAdapter.notifyDataSetChanged();
                }catch (Exception ex){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}