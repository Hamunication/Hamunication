package com.dx3evm.hamunication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dx3evm.hamunication.Dialogs.InputDialog;
import com.dx3evm.hamunication.Models.Quiz;
import com.dx3evm.hamunication.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizViewHolder>{

    List<Quiz> quizList;

    public QuizAdapter( List<Quiz> quizList){
        this.quizList = quizList;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_item, parent,false);

        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }
}

class QuizViewHolder extends RecyclerView.ViewHolder{

    EditText etQuestion, etCorrectAnswer;

    MaterialButton mtrlBtnAddChoices;

    RadioGroup radioGroup;

    public QuizViewHolder(@NonNull View itemView) {
        super(itemView);
        etQuestion = itemView.findViewById(R.id.etQuestion);
        etCorrectAnswer = itemView.findViewById(R.id.etCorrectAnswer);
        mtrlBtnAddChoices = itemView.findViewById(R.id.mtrlBtnAddChoices);
        radioGroup = itemView.findViewById(R.id.radioGroup);

        mtrlBtnAddChoices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputDialog inputDialog = new InputDialog();

                inputDialog.showDialog(itemView.getContext(), "choices", new InputDialog.OnDialogClickListener() {
                    @Override
                    public void onSave(String input) {
                        if (radioGroup.getChildCount() >= 4) {
                            mtrlBtnAddChoices.setEnabled(false);
                        }else {
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
                    public void onCancel() {

                    }
                });
            }
        });
    }


}
