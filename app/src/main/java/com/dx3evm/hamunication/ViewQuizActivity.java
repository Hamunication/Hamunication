package com.dx3evm.hamunication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dx3evm.hamunication.Adapters.QuestionItemEditAdapter;
import com.dx3evm.hamunication.Adapters.QuestionItemViewAdapter;
import com.dx3evm.hamunication.Models.Course;
import com.dx3evm.hamunication.Models.Module;
import com.dx3evm.hamunication.Models.Question;
import com.dx3evm.hamunication.Models.Quiz;
import com.dx3evm.hamunication.Models.Topic;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewQuizActivity extends AppCompatActivity {
    int score = 0;
    Map<String, String> correctAnswersMap = new HashMap<>();
    List<Question> questionList;
    QuestionItemViewAdapter questionItemViewAdapter;
    MaterialButton mtrlBtnSubmit;
    TextView tvQuizName, tvBack;
    RecyclerView rvQuizList;

    Course course = null;
    Module module = null;
    Quiz quiz = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_quiz);

        course = (Course) getIntent().getSerializableExtra("course");
        module = (Module) getIntent().getSerializableExtra("module");
        quiz = (Quiz) getIntent().getSerializableExtra("quiz");

        tvQuizName = findViewById(R.id.tvQuizName);
        rvQuizList = findViewById(R.id.rvQuizList);
        mtrlBtnSubmit = findViewById(R.id.mtrlBtnSubmit);
        tvBack = findViewById(R.id.tvBack);

        questionList = new ArrayList<>();
        questionItemViewAdapter = new QuestionItemViewAdapter(this, questionList);
        rvQuizList.setLayoutManager(new LinearLayoutManager(this));
        rvQuizList.setAdapter(questionItemViewAdapter);

        questionItemViewAdapter.notifyDataSetChanged();


        getQuizScore(quiz.getQuizID());

        if(quiz != null){
            displayQuestionsForQuiz(quiz.getQuizID());
        }

        mtrlBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printResults();
            }
        });

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void displayQuestionsForQuiz(String quizID) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(course.getId()).child("Module").child(module.getId()).child("Quiz").child(quizID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                questionList.clear();
                correctAnswersMap.clear();

                for (DataSnapshot questionSnapshot : snapshot.child("questions").getChildren()) {
                    String questionID = questionSnapshot.getKey();
                    String questionTitle = questionSnapshot.child("QuestionTitle").getValue(String.class);
                    String correctAnswer = questionSnapshot.child("CorrectAnswer").getValue(String.class);

                    Map<String, String> choices = new HashMap<>();

                    for (DataSnapshot choiceSnapshot : questionSnapshot.child("Choices").getChildren()) {
                        String choiceKey = choiceSnapshot.getKey();
                        String choiceValue = choiceSnapshot.getValue(String.class);
                        choices.put(choiceKey, choiceValue);
                    }

                    correctAnswersMap.put(questionID, correctAnswer);

                    Question question = new Question();
                    question.setQuestionID(questionID);
                    question.setQuestionText(questionTitle);
                    question.setCorrectAnswer(correctAnswer);
                    question.setChoices(choices);

                    questionList.add(question);
                }

                tvQuizName.setText(snapshot.child("quizTitle").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void getQuizScore(String quizID){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(course.getId()).child("Module").child(module.getId()).child("Quiz").child(quizID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot scoresSnapshot : snapshot.child("usersScore").getChildren()){
                    String id = scoresSnapshot.getKey();
                    String score = scoresSnapshot.child("Score").getValue(String.class);
                    String totalScore = scoresSnapshot.child("TotalScore").getValue(String.class);
                    if(id.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        Toast.makeText(ViewQuizActivity.this, "Score: " + score + "/" + totalScore, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void printResults(){
        Map<String, String> selectedChoicesMap = questionItemViewAdapter.getSelectedChoicesMap();
        if(selectedChoicesMap.size() == 0){
            Toast.makeText(this, "Please take your time to answer.", Toast.LENGTH_SHORT).show();
        }
        else{
            for (Map.Entry<String, String> entry : selectedChoicesMap.entrySet()) {
                String questionID = entry.getKey();
                String selectedChoice = entry.getValue();
                String correctAnswer = correctAnswersMap.get(questionID);

                if (selectedChoice.equals(correctAnswer)) {
                    if (score != correctAnswersMap.size()) {
                        score++;
                    }
                }
            }

            Intent resultIntent = new Intent(ViewQuizActivity.this, QuizResultActivity.class);
            resultIntent.putExtra("Score", score);
            resultIntent.putExtra("TotalScore", correctAnswersMap.size());
            resultIntent.putExtra("QuizTitle", tvQuizName.getText().toString());
            resultIntent.putExtra("course", course);
            resultIntent.putExtra("module", module);
            resultIntent.putExtra("quiz", quiz);
            startActivity(resultIntent);
        }
    }

}