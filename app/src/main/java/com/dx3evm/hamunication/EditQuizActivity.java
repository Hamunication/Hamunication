package com.dx3evm.hamunication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.dx3evm.hamunication.Adapters.QuestionItemEditAdapter;
import com.dx3evm.hamunication.Adapters.QuestionItemViewAdapter;
import com.dx3evm.hamunication.Dialogs.QuestionDialog;
import com.dx3evm.hamunication.Models.Course;
import com.dx3evm.hamunication.Models.Module;
import com.dx3evm.hamunication.Models.Question;
import com.dx3evm.hamunication.Models.QuestionModel;
import com.dx3evm.hamunication.Models.Quiz;
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

public class EditQuizActivity extends AppCompatActivity {


    List<QuestionModel> questionList;
    RecyclerView rvQuizList;
    QuestionItemViewAdapter questionItemViewAdapter;

    QuestionDialog questionDialog;

    Course course = null;
    Module module = null;
    Quiz quiz = null;

    Map<String, String> correctAnswersMap = new HashMap<>();

    EditText etQuizTitle;
    MaterialButton mtrlBtnAddQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_quiz);
        rvQuizList = findViewById(R.id.rvQuizList);
        etQuizTitle = findViewById(R.id.etQuizTitle);
        mtrlBtnAddQuestion = findViewById(R.id.mtrlBtnAddQuestion);

        course = (Course) getIntent().getSerializableExtra("course");
        module = (Module) getIntent().getSerializableExtra("module");
        quiz = (Quiz) getIntent().getSerializableExtra("quiz");

        questionList = new ArrayList<>();
        questionItemViewAdapter = new QuestionItemViewAdapter(this, questionList);

        questionItemViewAdapter.setOnLongClickListener(new QuestionItemViewAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(int position, QuestionModel questionModel) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditQuizActivity.this);
                builder.setTitle("Delete Question")
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteQuestion(questionModel.getQuestionID());
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

        rvQuizList.setLayoutManager(new LinearLayoutManager(this));
        rvQuizList.setAdapter(questionItemViewAdapter);

        if(quiz != null){
            displayQuestionsForQuiz(quiz.getQuizID());
        }


        mtrlBtnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionDialog = new QuestionDialog();

                questionDialog.showDialog(EditQuizActivity.this, EditQuizActivity.this, new QuestionDialog.OnDialogClickListener() {

                    @Override
                    public void onSave(String question, String correctAnswer, Map<String, String> choices) {
                            createQuestion(question, correctAnswer, choices);
                    }

                    @Override
                    public void onCancel() {

                    }
                });

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
                    String questionTitle = questionSnapshot.child("questionTitle").getValue(String.class);
                    String correctAnswer = questionSnapshot.child("correctAnswer").getValue(String.class);

                    Map<String, String> choices = new HashMap<>();

                    for (DataSnapshot choiceSnapshot : questionSnapshot.child("choices").getChildren()) {
                        String choiceKey = choiceSnapshot.getKey();
                        String choiceValue = choiceSnapshot.getValue(String.class);
                        choices.put(choiceKey, choiceValue);
                    }

                    correctAnswersMap.put(questionID, correctAnswer);

                    QuestionModel question = new QuestionModel();
                    question.setQuestionID(questionID);
                    question.setQuestionTitle(questionTitle);
                    question.setCorrectAnswer(correctAnswer);
                    question.setChoices(choices);

                    questionList.add(question);
                }

                etQuizTitle.setText(snapshot.child("quizTitle").getValue(String.class));
                questionItemViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void createQuestion(String question, String correctAnswer, Map<String, String> choices){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(course.getId()).child("Module").child(module.getId()).child("Quiz").child(quiz.getQuizID());

        QuestionModel questionModel = new QuestionModel(question, correctAnswer, choices);

        databaseReference.child("questions").push().setValue(questionModel);

    }

    public void deleteQuestion(String questionId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(course.getId()).child("Module").child(module.getId()).child("Quiz").child(quiz.getQuizID()).child("questions").child(questionId);

        databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(EditQuizActivity.this, "Question deleted Successfully!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditQuizActivity.this, "Error deleting question!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}