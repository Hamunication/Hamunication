package com.dx3evm.hamunication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dx3evm.hamunication.Models.Course;
import com.dx3evm.hamunication.Models.Quiz;
import com.dx3evm.hamunication.Models.Score;
import com.dx3evm.hamunication.R;

import java.util.List;
import java.util.Map;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

    Context context;
    List<Quiz> quizList;

    List<Score> scoreList;

    private OnClickListener onClickListener;

    private OnLongClickListener onLongClickListener;

    public QuizAdapter(Context context, List<Quiz> quizList, List<Score> scoreList) {
        this.context = context;
        this.quizList = quizList;
        this.scoreList = scoreList;
    }


    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_item, parent, false);

        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Quiz quiz = quizList.get(position);
        holder.tvQuizTitle.setText(quiz.getQuizTitle());

        if(scoreList != null){
            if (!scoreList.isEmpty() && position < scoreList.size()) {
                holder.tvPercentage.setVisibility(View.VISIBLE);
                holder.tvTakeQuiz.setVisibility(View.GONE);
                Score score = scoreList.get(position);
                holder.tvPercentage.setText(score.getScore() + "/" + score.getTotalScore());
            } else {
                holder.tvTakeQuiz.setVisibility(View.VISIBLE);
                holder.tvPercentage.setVisibility(View.GONE);
            }
        }else{
            holder.tvTakeQuiz.setVisibility(View.VISIBLE);
            holder.tvPercentage.setVisibility(View.GONE);
        }


        holder.tvQuizTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onClickListener != null){
                    onClickListener.onClick(holder.getAdapterPosition(), quiz);
                }
            }
        });

        holder.tvQuizTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(onLongClickListener != null){
                    onLongClickListener.onLongClick(holder.getAdapterPosition(), quiz);
                }
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public void setOnClickListener(QuizAdapter.OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, Quiz quiz);

    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public interface OnLongClickListener {
        void onLongClick(int position, Quiz quiz);
    }

    public class QuizViewHolder extends RecyclerView.ViewHolder{

        TextView tvQuizTitle;
        TextView tvPercentage, tvTakeQuiz;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuizTitle = itemView.findViewById(R.id.tvQuizTitle);
            tvPercentage = itemView.findViewById(R.id.tvPercentage);
            tvTakeQuiz = itemView.findViewById(R.id.tvTakeQuiz);
        }
    }


}

