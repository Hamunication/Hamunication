package com.dx3evm.hamunication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.dx3evm.hamunication.Adapters.QuestionItemEditAdapter;
import com.dx3evm.hamunication.Models.Course;
import com.dx3evm.hamunication.Models.Module;
import com.dx3evm.hamunication.Models.Quiz;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditQuizActivity extends AppCompatActivity {


    List<Quiz> quizList;
    RecyclerView rvQuizList;
    QuestionItemEditAdapter quizItemAdapter;

    Course course = null;
    Module module = null;
    Quiz quiz = null;
    MaterialButton mtrlBtnAddQuestion, mtrlBtnSaveQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_quiz);
        rvQuizList = findViewById(R.id.rvQuizList);
        mtrlBtnAddQuestion = findViewById(R.id.mtrlBtnAddQuestion);
        mtrlBtnSaveQuiz = findViewById(R.id.mtrlBtnSaveQuiz);

        course = (Course) getIntent().getSerializableExtra("course");
        module = (Module) getIntent().getSerializableExtra("module");
        quiz = (Quiz) getIntent().getSerializableExtra("quiz");

        quizList = new ArrayList<>();
        quizItemAdapter = new QuestionItemEditAdapter(quizList);

        rvQuizList.setLayoutManager(new LinearLayoutManager(this));
        rvQuizList.setAdapter(quizItemAdapter);

        mtrlBtnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quizList.add(new Quiz());
                quizItemAdapter.notifyItemInserted(quizList.size() - 1);
            }
        });

        mtrlBtnSaveQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemCount = quizItemAdapter.getItemCount();

                Map<String, Object> quizData = new HashMap<>();
                quizData.put("quizTitle", "Test"); // Set your quiz title here
                Map<String, Object> questionsMap = new HashMap<>();

                for (int i = 0; i < itemCount; i++) {
                    RecyclerView.ViewHolder viewHolder = rvQuizList.findViewHolderForAdapterPosition(i);
                    if (viewHolder instanceof QuestionItemEditAdapter.QuestionItemEditViewHolder) {
                        QuestionItemEditAdapter.QuestionItemEditViewHolder quizViewHolder = (QuestionItemEditAdapter.QuestionItemEditViewHolder) viewHolder;

                        Map<String, Object> questionMap = new HashMap<>();
                        Map<String, String> choices = new HashMap<>();

                        String questionTitle = quizViewHolder.etQuestion.getText().toString();
                        String correctAnswer = quizViewHolder.etCorrectAnswer.getText().toString();

                        questionMap.put("QuestionTitle", questionTitle);
                        questionMap.put("CorrectAnswer", correctAnswer);

                        // Iterate through RadioGroup's child views
                        for (int j = 0; j < quizViewHolder.radioGroup.getChildCount(); j++) {
                            View radioChild = quizViewHolder.radioGroup.getChildAt(j);

                            if (radioChild instanceof RadioButton) {
                                RadioButton radioButton = (RadioButton) radioChild;
                                String choice = radioButton.getText().toString();
                                choices.put(String.valueOf(j), choice);
                            }
                        }

                        questionMap.put("Choices", choices);
                        questionsMap.put("question" + i, questionMap);
                    } else {
                        // Handle the case where the view holder is null or not an instance of QuizViewHolder
                    }
                }

                quizData.put("questions", questionsMap);
                createQuiz(quizData);
            }
        });
    }

    public void createQuiz(Map<String, Object> quizData){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(course.getId()).child("Module").child(module.getId()).child("Quiz");

        String quizId = databaseReference.push().getKey();
        Toast.makeText(this, "ID: " + quizId, Toast.LENGTH_SHORT).show();

        // Set the quiz data in the database
        databaseReference.child(quizId).setValue(quizData)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(EditQuizActivity.this, "Passed", Toast.LENGTH_SHORT).show();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditQuizActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
    }

}