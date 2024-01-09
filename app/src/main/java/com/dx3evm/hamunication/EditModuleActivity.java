package com.dx3evm.hamunication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dx3evm.hamunication.Adapters.QuizAdapter;
import com.dx3evm.hamunication.Adapters.TopicAdapter;
import com.dx3evm.hamunication.Dialogs.InputDialog;
import com.dx3evm.hamunication.Models.Course;
import com.dx3evm.hamunication.Models.Module;
import com.dx3evm.hamunication.Models.Quiz;
import com.dx3evm.hamunication.Models.QuizModel;
import com.dx3evm.hamunication.Models.Score;
import com.dx3evm.hamunication.Models.Topic;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class EditModuleActivity extends AppCompatActivity {

    TopicAdapter topicAdapter;

    QuizAdapter quizAdapter;

    TextView tvModuleName;

    List<Topic> topicList;

    List<Quiz> quizList;

    List<Score> scoreList;

    MaterialButton mtrlBtnAddTopic, mtrlBtnAddQuiz;

    RecyclerView rvTopicList, rvQuizList;

    Course course = null;
    Module module = null;

    Quiz quiz = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_module);

        tvModuleName = findViewById(R.id.tvModuleName);
        mtrlBtnAddTopic = findViewById(R.id.mtrlBtnAddTopic);
        mtrlBtnAddQuiz = findViewById(R.id.mtrlBtnAddQuiz);
        rvTopicList = findViewById(R.id.rvTopicList);
        rvQuizList = findViewById(R.id.rvQuizList);


        topicList = new ArrayList<>();
        topicAdapter = new TopicAdapter(this, topicList);

        quizList = new ArrayList<>();
        quizAdapter = new QuizAdapter(this, quizList, scoreList);

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

        topicAdapter.setOnLongClickListener(new TopicAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(int position, Topic topic) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditModuleActivity.this);
                builder.setTitle("Delete Topic")
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteTopic(topic.getTopicID());
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

        quizAdapter.setOnClickListener(new QuizAdapter.OnClickListener() {
            @Override
            public void onClick(int position, Quiz quiz) {
                Intent intent = new Intent(EditModuleActivity.this, EditQuizActivity.class);
                intent.putExtra("course", course);
                intent.putExtra("module", module);
                intent.putExtra("quiz", quiz);

                startActivity(intent);
            }
        });

        quizAdapter.setOnLongClickListener(new QuizAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(int position, Quiz quiz) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditModuleActivity.this);
                builder.setTitle("Delete Quiz")
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteQuiz(quiz.getQuizID());
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

        rvTopicList.setLayoutManager(new LinearLayoutManager(this));
        rvTopicList.setAdapter(topicAdapter);

        rvQuizList.setLayoutManager(new LinearLayoutManager(this));
        rvQuizList.setAdapter(quizAdapter);

        mtrlBtnAddQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputDialog inputDialog = new InputDialog();

                inputDialog.showDialog(EditModuleActivity.this, EditModuleActivity.this, "Quiz", new InputDialog.OnDialogClickListener() {
                    @Override
                    public void onSave(String input) {
                        createQuiz(input);
                    }

                    @Override
                    public void OnSaveCourse(String courseName, String courseDescription, Uri imageUri) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
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
                    topicList.clear();
                    for (DataSnapshot topicSnapshot : snapshot.child("Topic").getChildren()) {
                        String topicID = topicSnapshot.getKey();
                        String topicTitle = topicSnapshot.child("Title").getValue(String.class);
                        String topicDescription = topicSnapshot.child("Description").getValue(String.class);

                        // Retrieve Media values
                        DataSnapshot mediaSnapshot = topicSnapshot.child("Media");

                        Map<String, Map<String, String>> mediaMap = new HashMap<>();

                        for (DataSnapshot mediaChild : mediaSnapshot.getChildren()) {
                            String mediaKey = mediaChild.getKey();
                            String mediaLink = mediaChild.child("link").getValue(String.class);
                            String mediaType = mediaChild.child("type").getValue(String.class);

                            Map<String, String> nestedMediaMap = new HashMap<>();
                            nestedMediaMap.put("link", mediaLink);
                            nestedMediaMap.put("type", mediaType);

                            mediaMap.put(mediaKey, nestedMediaMap);
                        }

                        // Set media values to your Topic object


                        Topic topic = new Topic();
                        topic.setTopicID(topicID);
                        topic.setTopicTitle(topicTitle);
                        topic.setTopicDescription(topicDescription);
                        topic.setUrlList(mediaMap);

                        topicList.add(topic);
                    }

                    topicAdapter.notifyDataSetChanged();
                } catch (Exception ex) {
                    // Handle exceptions
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });

    }

    public void displayQuiz(String courseID, String moduleID){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(courseID).child("Module").child(moduleID);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                quizList.clear();
                for (DataSnapshot quizSnapshot : snapshot.child("Quiz").getChildren()) {

                    String quizID = quizSnapshot.getKey();
                    String quizTitle = quizSnapshot.child("quizTitle").getValue(String.class);

                    Quiz newQuiz = new Quiz();

                    newQuiz.setQuizID(quizID);
                    newQuiz.setQuizTitle(quizTitle);

                    quizList.add(newQuiz);

                    quiz = newQuiz;

                }


                quizAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void createQuiz(String quizTitle){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(course.getId()).child("Module").child(module.getId()).child("Quiz");

        QuizModel quizModel = new QuizModel(quizTitle);

        databaseReference.push().setValue(quizModel);
    }

    public void deleteQuiz(String quizId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(course.getId()).child("Module").child(module.getId()).child("Quiz").child(quizId);

        databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(EditModuleActivity.this, "Quiz deleted Successfully!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditModuleActivity.this, "Error deleting Quiz!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteTopic(String topicId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(course.getId()).child("Module").child(module.getId()).child("Topic").child(topicId);

        databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(EditModuleActivity.this, "Topic deleted Successfully!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditModuleActivity.this, "Error deleting Topic!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}