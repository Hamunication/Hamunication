package com.dx3evm.hamunication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dx3evm.hamunication.Models.Question;
import com.dx3evm.hamunication.Models.Quiz;
import com.dx3evm.hamunication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionItemViewAdapter extends RecyclerView.Adapter<QuestionItemViewAdapter.QuestionItemViewViewHolder> {

    Context context;
    List<Question> questionList;
    private String selectedQuizID;  // Add this field


    public QuestionItemViewAdapter(Context context, List<Question> questionList) {
        this.context = context;
        this.questionList = questionList;
    }

    public void setSelectedQuizID(String selectedQuizID) {
        this.selectedQuizID = selectedQuizID;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public QuestionItemViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item_view, parent, false);

        return new QuestionItemViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionItemViewViewHolder holder, int position) {
        Question currentQuestion = questionList.get(position);
        holder.tvQuestion.setText(currentQuestion.getQuestionText());

        holder.radioGroup.removeAllViews();

        Map<String, String> choicesMap = currentQuestion.getChoices();

        for(Map.Entry<String, String> entry : choicesMap.entrySet()){

            String key = entry.getKey();
            String choice = entry.getValue();

            RadioButton rbChoices = new RadioButton(context);
            rbChoices.setId(Integer.parseInt(key));
            rbChoices.setText(choice);

            holder.radioGroup.addView(rbChoices);

        }



    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class QuestionItemViewViewHolder extends RecyclerView.ViewHolder{
        public TextView tvQuestion;

        public RadioGroup radioGroup;


        public QuestionItemViewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            radioGroup = itemView.findViewById(R.id.radioGroup);
        }
    }
}
