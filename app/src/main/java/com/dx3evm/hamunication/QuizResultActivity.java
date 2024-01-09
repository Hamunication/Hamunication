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
import com.dx3evm.hamunication.Adapters.ScoreBoardAdapter;
import com.dx3evm.hamunication.Fragments.DashboardFragment;
import com.dx3evm.hamunication.Models.Course;
import com.dx3evm.hamunication.Models.Module;
import com.dx3evm.hamunication.Models.Quiz;
import com.dx3evm.hamunication.Models.Score;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class QuizResultActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseDatabase firebaseDatabase;
    TextView tvScoreText, tvQuizName;

    RecyclerView rvScoreBoard;

    ScoreBoardAdapter scoreBoardAdapter;

    List<Score> scoreList;

    MaterialButton mtrlBtnHome, mtrlBtnRetake;

    Course course = null;
    Module module = null;
    Quiz quiz = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);
        fAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        mtrlBtnHome = findViewById(R.id.mtrlBtnHome);
        mtrlBtnRetake = findViewById(R.id.mtrlBtnRetake);

        Map<String, String> usersScore = new HashMap<>();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        course = (Course) getIntent().getSerializableExtra("course");
        module = (Module) getIntent().getSerializableExtra("module");
        quiz = (Quiz) getIntent().getSerializableExtra("quiz");

        tvScoreText = findViewById(R.id.tvScoreText);
        tvQuizName = findViewById(R.id.tvQuizName);
        rvScoreBoard = findViewById(R.id.rvScoreBoard);

        scoreList = new ArrayList<>();
        scoreBoardAdapter = new ScoreBoardAdapter(scoreList);

        Intent intent = getIntent();
        String quizTitle = intent.getStringExtra("QuizTitle");
        int score = intent.getIntExtra("Score", 0);
        int totalScore = intent.getIntExtra("TotalScore", 0);
        String userFullName = intent.getStringExtra("UserFullName");

        usersScore.put("UserFullName", userFullName);
        usersScore.put("Score", String.valueOf(score));
        usersScore.put("TotalScore", String.valueOf(totalScore));


        tvScoreText.setText(score + " / " + totalScore);
        tvQuizName.setText(quizTitle);
        rvScoreBoard.setLayoutManager(new LinearLayoutManager(this));
        rvScoreBoard.setAdapter(scoreBoardAdapter);

        displayScoreboard();

        mtrlBtnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QuizResultActivity.this, UserMainActivity.class));
                finish();
            }
        });

        mtrlBtnRetake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizResultActivity.this, ViewQuizActivity.class);
                intent.putExtra("course", course);
                intent.putExtra("module", module);
                intent.putExtra("quiz", quiz);

                startActivity(intent);
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(QuizResultActivity.this, UserMainActivity.class));
        finish();
    }

    public void displayScoreboard(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(course.getId()).child("Module").child(module.getId()).child("Quiz").child(quiz.getQuizID());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    PriorityQueue<Score> priorityQueue = new PriorityQueue<>(new Comparator<Score>() {
                        @Override
                        public int compare(Score score1, Score score2) {
                            // Assuming the score is a String representing an integer value
                            int s1 = Integer.parseInt(score1.getScore());
                            int s2 = Integer.parseInt(score2.getScore());

                            // Sort in descending order
                            return Integer.compare(s2, s1);
                        }
                    });

                    for (DataSnapshot scoresSnapshot : snapshot.child("usersScore").getChildren()) {
                        String id = scoresSnapshot.getKey();
                        String score = scoresSnapshot.child("Score").getValue(String.class);
                        String totalScore = scoresSnapshot.child("TotalScore").getValue(String.class);
                        String userFullName = scoresSnapshot.child("UserFullName").getValue(String.class);

                        Score scoreModel = new Score();
                        scoreModel.setScoreId(id);
                        scoreModel.setScore(score);
                        scoreModel.setTotalScore(totalScore);
                        scoreModel.setUserFullName(userFullName);

                        // Add to the priorityQueue
                        priorityQueue.add(scoreModel);
                    }

                    scoreList.clear();

                    // Extract elements from the priorityQueue (sorted)
                    while (!priorityQueue.isEmpty()) {
                        scoreList.add(priorityQueue.poll());
                    }

                    scoreBoardAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
        });

    }

}