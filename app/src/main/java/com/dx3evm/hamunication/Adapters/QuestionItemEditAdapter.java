package com.dx3evm.hamunication.Adapters;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dx3evm.hamunication.Dialogs.InputDialog;
import com.dx3evm.hamunication.Models.Question;
import com.dx3evm.hamunication.Models.Quiz;
import com.dx3evm.hamunication.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class QuestionItemEditAdapter extends RecyclerView.Adapter<QuestionItemEditAdapter.QuestionItemEditViewHolder> {

    List<Question> questionList;

    private Activity activity;

    public QuestionItemEditAdapter(Activity activity, List<Question> questionList) {
        this.activity = activity;
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestionItemEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item_edit, parent, false);

        return new QuestionItemEditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionItemEditViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class QuestionItemEditViewHolder extends RecyclerView.ViewHolder {

        public EditText etQuestion;
        public EditText etCorrectAnswer;

        public MaterialButton mtrlBtnAddChoices;

        public RadioGroup radioGroup;

        public QuestionItemEditViewHolder(@NonNull View itemView) {
            super(itemView);
            etQuestion = itemView.findViewById(R.id.etQuestion);
            etCorrectAnswer = itemView.findViewById(R.id.etCorrectAnswer);
            mtrlBtnAddChoices = itemView.findViewById(R.id.mtrlBtnAddChoices);
            radioGroup = itemView.findViewById(R.id.radioGroup);

            mtrlBtnAddChoices.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle the click event
                    InputDialog inputDialog = new InputDialog();

                    inputDialog.showDialog(activity, itemView.getContext(), "choices", new InputDialog.OnDialogClickListener() {
                        @Override
                        public void onSave(String input) {
                            // Handle the save event
                            if (radioGroup.getChildCount() >= 4) {
                                mtrlBtnAddChoices.setEnabled(false);
                            } else {
                                RadioButton newRadioButton = new RadioButton(itemView.getContext());
                                newRadioButton.setText(input);

                                newRadioButton.setTextSize(18);

                                int marginInPixels = 10;
                                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                                        RadioGroup.LayoutParams.WRAP_CONTENT,
                                        RadioGroup.LayoutParams.WRAP_CONTENT
                                );
                                params.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels);

                                newRadioButton.setLayoutParams(params);

                                radioGroup.addView(newRadioButton);
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
        }
    }
}