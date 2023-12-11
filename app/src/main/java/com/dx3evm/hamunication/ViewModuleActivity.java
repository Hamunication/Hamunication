package com.dx3evm.hamunication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dx3evm.hamunication.Adapters.QuizAdapter;
import com.dx3evm.hamunication.Adapters.TopicAdapter;
import com.dx3evm.hamunication.Models.Course;
import com.dx3evm.hamunication.Models.Module;
import com.dx3evm.hamunication.Models.Quiz;
import com.dx3evm.hamunication.Models.Topic;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewModuleActivity extends AppCompatActivity {

    TopicAdapter topicAdapter;

    QuizAdapter quizAdapter;

    TextView tvModuleName, tvBack;

    List<Topic> topicList;

    List<Quiz> quizList;

    RecyclerView rvTopicList, rvQuizList;

    Course course = null;
    Module module = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_module);
        tvModuleName = findViewById(R.id.tvModuleName);
        tvBack = findViewById(R.id.tvBack);

        rvTopicList = findViewById(R.id.rvTopicList);
        rvQuizList = findViewById(R.id.rvQuizList);

        topicList = new ArrayList<>();
        topicAdapter = new TopicAdapter(this, topicList);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        topicAdapter.setOnClickListener(new TopicAdapter.OnClickListener() {
            @Override
            public void onClick(int position, Topic topic) {
                Intent intent = new Intent(ViewModuleActivity.this, ViewTopicActivity.class);
                intent.putExtra("course", course);
                intent.putExtra("module", module);
                intent.putExtra("topic", topic);

                startActivity(intent);
            }
        });


        rvTopicList.setLayoutManager(new LinearLayoutManager(this));
        rvTopicList.setAdapter(topicAdapter);


        quizList = new ArrayList<>();
        quizAdapter = new QuizAdapter(this, quizList);
        quizAdapter.setOnClickListener(new QuizAdapter.OnClickListener() {
            @Override
            public void onClick(int position, Quiz quiz) {
                Intent intent = new Intent(ViewModuleActivity.this, ViewQuizActivity.class);
                intent.putExtra("course", course);
                intent.putExtra("module", module);
                intent.putExtra("quiz", quiz);

                startActivity(intent);
            }
        });

        rvQuizList.setLayoutManager(new LinearLayoutManager(this));
        rvQuizList.setAdapter(quizAdapter);


        if(getIntent().hasExtra("module")){
            module = (Module) getIntent().getSerializableExtra("module");
            course = (Course) getIntent().getSerializableExtra("course");

            if(module != null){
                tvModuleName.setText(module.getTitle());
                displayTopic(course.getId(), module.getId());
                displayQuiz(course.getId(), module.getId());
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

                        DataSnapshot mediaSnapshot = topicSnapshot.child("Media");

                        Map<String, Map<String, String>> mediaMap = new HashMap<>();

                        for(DataSnapshot mediaChild : mediaSnapshot.getChildren()){
                            String mediaKey = mediaChild.getKey();
                            String mediaLink = mediaChild.child("link").getValue(String.class);
                            String mediaType = mediaChild.child("type").getValue(String.class);

                            Map<String, String> nestedMediaMap = new HashMap<>();
                            nestedMediaMap.put("link", mediaLink);
                            nestedMediaMap.put("type", mediaType);

                            mediaMap.put(mediaKey, nestedMediaMap);
                        }

                        Topic topic = new Topic();
                        topic.setTopicID(topicID);
                        topic.setTopicTitle(topicTitle);
                        topic.setTopicDescription(topicDescription);
                        topic.setUrlList(mediaMap);

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

    public void displayQuiz(String courseID, String moduleID){
        quizList.clear();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(courseID).child("Module").child(moduleID);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot quizSnapshot : snapshot.child("Quiz").getChildren()){
                    String quizID = quizSnapshot.getKey();
                    String quizTitle = quizSnapshot.child("quizTitle").getValue(String.class);
                    Map<String, Object> questions = (Map<String, Object>) quizSnapshot.child("questions").getValue();

                    Quiz quiz = new Quiz();

                    quiz.setQuizID(quizID);
                    quiz.setQuizTitle(quizTitle);
                    quiz.setQuestions(questions);

                    quizList.add(quiz);
                }
                quizAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}