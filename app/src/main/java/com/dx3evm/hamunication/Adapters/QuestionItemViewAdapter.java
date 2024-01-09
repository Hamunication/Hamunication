package com.dx3evm.hamunication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dx3evm.hamunication.Models.Course;
import com.dx3evm.hamunication.Models.Question;
import com.dx3evm.hamunication.Models.QuestionModel;
import com.dx3evm.hamunication.Models.Quiz;
import com.dx3evm.hamunication.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionItemViewAdapter extends RecyclerView.Adapter<QuestionItemViewAdapter.QuestionItemViewViewHolder> {

    Context context;
    List<QuestionModel> questionList;
    private QuestionItemViewViewHolder questionItemViewViewHolder;

    private OnLongClickListener onLongClickListener;

    private Map<String, String> selectedChoicesMap = new HashMap<>();

    public QuestionItemViewAdapter(Context context, List<QuestionModel> questionList) {
        this.context = context;
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestionItemViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item_view, parent, false);

        return new QuestionItemViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionItemViewViewHolder holder, int position) {
        QuestionModel currentQuestion = questionList.get(position);
        holder.tvQuestion.setText(currentQuestion.getQuestionTitle());

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

        holder.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = (RadioButton) group.getChildAt(checkedId);
            if (radioButton != null) {
                String questionID = currentQuestion.getQuestionID();
                selectedChoicesMap.put(questionID, radioButton.getText().toString());
                currentQuestion.setAnswered(true);
                holder.tvError.setVisibility(View.GONE);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(onLongClickListener != null){
                    onLongClickListener.onLongClick(position, currentQuestion);
                }
                return true;
            }
        });

    }


    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public interface OnLongClickListener {
        void onLongClick(int position, QuestionModel questionModel);
    }

    public void checkBlankChoices(){
        for(QuestionModel question : questionList){
            if(!question.isAnswered()){
                questionItemViewViewHolder.tvError.setVisibility(View.VISIBLE);
            }
        }
    }


    public Map<String, String> getSelectedChoicesMap() {
        return selectedChoicesMap;
    }

    public class QuestionItemViewViewHolder extends RecyclerView.ViewHolder{
        public TextView tvQuestion, tvError;

        public RadioGroup radioGroup;


        public QuestionItemViewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            tvError = itemView.findViewById(R.id.tvError);
            radioGroup = itemView.findViewById(R.id.radioGroup);
        }
    }
}
