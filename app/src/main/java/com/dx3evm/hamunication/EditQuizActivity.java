package com.dx3evm.hamunication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.dx3evm.hamunication.Adapters.QuizAdapter;
import com.dx3evm.hamunication.Dialogs.InputDialog;
import com.dx3evm.hamunication.Models.Quiz;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class EditQuizActivity extends AppCompatActivity {

    ArrayList<String> choices = new ArrayList<>();
    List<Quiz> quizList;
    RecyclerView rvQuizList;
    QuizAdapter quizAdapter;

    MaterialButton mtrlBtnAddQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_quiz);
        rvQuizList = findViewById(R.id.rvQuizList);
        mtrlBtnAddQuestion = findViewById(R.id.mtrlBtnAddQuestion);

        quizList = new ArrayList<>();

        mtrlBtnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                quizList.add(new Quiz());

                quizAdapter = new QuizAdapter(quizList);

                rvQuizList.setLayoutManager(new LinearLayoutManager(EditQuizActivity.this));

                rvQuizList.setAdapter(quizAdapter);
            }
        });
    }
}