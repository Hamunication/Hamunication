package com.dx3evm.hamunication;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dx3evm.hamunication.Models.Course;
import com.dx3evm.hamunication.Models.Module;
import com.dx3evm.hamunication.Models.Quiz;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class QuizResultActivity extends AppCompatActivity {

    TextView tvScoreText, tvQuizName, tvBack;

    Course course = null;
    Module module = null;
    Quiz quiz = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);
        Map<String, String> usersScore = new HashMap<>();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        course = (Course) getIntent().getSerializableExtra("course");
        module = (Module) getIntent().getSerializableExtra("module");
        quiz = (Quiz) getIntent().getSerializableExtra("quiz");

        tvScoreText = findViewById(R.id.tvScoreText);
        tvQuizName = findViewById(R.id.tvQuizName);
        tvBack = findViewById(R.id.tvBack);

        Intent intent = getIntent();
        String quizTitle = intent.getStringExtra("QuizTitle");
        int score = intent.getIntExtra("Score", 0);
        int totalScore = intent.getIntExtra("TotalScore", 0);

        usersScore.put("Score", String.valueOf(score));
        usersScore.put("TotalScore", String.valueOf(totalScore));


        tvScoreText.setText(score + " / " + totalScore);
        tvQuizName.setText(quizTitle);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(course.getId()).child("Module").child(module.getId()).child("Quiz").child(quiz.getQuizID());

        databaseReference.child("usersScore").child(userID).setValue(usersScore).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(QuizResultActivity.this, "Saving Score...", Toast.LENGTH_SHORT).show();
            }
        });

//        Toast.makeText(this, "Course: " + course.getId(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "Module: " + module.getId(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "Quiz: " + quiz.getQuizID(), Toast.LENGTH_SHORT).show();
    }
}