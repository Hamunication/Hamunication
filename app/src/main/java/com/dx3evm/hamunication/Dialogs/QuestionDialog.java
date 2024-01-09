package com.dx3evm.hamunication.Dialogs;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;

import com.dx3evm.hamunication.Models.QuestionModel;
import com.dx3evm.hamunication.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class QuestionDialog {

    private AlertDialog alertDialog;

    private EditText etQuestion, etCorrectAnswer;

    private MaterialButton mtrlBtnAddChoices, btnSave, btnCancel;

    private RadioGroup radioGroup;

    Context context;

    private Activity activity;


    public void showDialog(Activity activity, Context context, final OnDialogClickListener onDialogClickListener){
        this.activity = activity;
        this.context = context;

        Map<String, String> Choices = new HashMap<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_question, null);
        builder.setTitle("Add New Question");
        builder.setView(dialogView);

        etQuestion = dialogView.findViewById(R.id.etQuestion);
        etCorrectAnswer = dialogView.findViewById(R.id.etCorrectAnswer);
        radioGroup = dialogView.findViewById(R.id.radioGroup);
        mtrlBtnAddChoices = dialogView.findViewById(R.id.mtrlBtnAddChoices);
        btnSave = dialogView.findViewById(R.id.btnSave);
        btnCancel = dialogView.findViewById(R.id.btnCancel);

        mtrlBtnAddChoices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputDialog inputDialog = new InputDialog();

                inputDialog.showDialog(activity, context, "choices", new InputDialog.OnDialogClickListener() {
                    @Override
                    public void onSave(String input) {
                        // Handle the save event
                        if (radioGroup.getChildCount() <= 4) {
                            // Add the choice to the map
                            String choiceKey = String.valueOf(Choices.size());
                            Choices.put(choiceKey, input);

                            // Create a new RadioButton with the choice text
                            RadioButton newRadioButton = new RadioButton(view.getContext());
                            newRadioButton.setText(input);

                            newRadioButton.setTextSize(18);

                            int marginInPixels = 10;
                            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                                    RadioGroup.LayoutParams.WRAP_CONTENT,
                                    RadioGroup.LayoutParams.WRAP_CONTENT
                            );
                            params.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels);

                            newRadioButton.setLayoutParams(params);

                            // Add the RadioButton to the RadioGroup
                            radioGroup.addView(newRadioButton);
                        } else {
                            mtrlBtnAddChoices.setEnabled(false);
                        }
                    }

                    @Override
                    public void OnSaveCourse(String courseName, String courseDescription, Uri imageUri) {

                    }

                    @Override
                    public void onCancel() {
                        // Handle the cancel event
                    }
                });
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDialogClickListener.onSave(etQuestion.getText().toString(), etCorrectAnswer.getText().toString(), Choices);
                alertDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDialogClickListener.onCancel();
                alertDialog.dismiss();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();
    }

    public interface OnDialogClickListener{

        void onSave(String question, String correctAnswer, Map<String, String> choices);

        void onCancel();
    }


}
