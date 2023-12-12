package com.dx3evm.hamunication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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

    List<Question> questionList;
    QuestionItemViewAdapter questionItemViewAdapter;
    MaterialButton mtrlBtnSubmit;
    TextView tvQuizName;
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


        questionList = new ArrayList<>();
        questionItemViewAdapter = new QuestionItemViewAdapter(this, questionList);
        questionItemViewAdapter.setSelectedQuizID(quiz.getQuizID());
        rvQuizList.setLayoutManager(new LinearLayoutManager(this));
        rvQuizList.setAdapter(questionItemViewAdapter);

        questionItemViewAdapter.notifyDataSetChanged();


        if(quiz != null){
            displayQuestionsForQuiz(quiz.getQuizID());
        }

    }

    public void displayQuestionsForQuiz(String quizID) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(course.getId()).child("Module").child(module.getId()).child("Quiz").child(quizID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                questionList.clear();

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

}