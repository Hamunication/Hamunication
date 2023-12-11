package com.dx3evm.hamunication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dx3evm.hamunication.Models.Quiz;
import com.dx3evm.hamunication.R;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

    Context context;
    List<Quiz> quizList;

    private OnClickListener onClickListener;

    public QuizAdapter(Context context, List<Quiz> quizList) {
        this.context = context;
        this.quizList = quizList;
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
        holder.tvQuizTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onClickListener != null){
                    onClickListener.onClick(holder.getAdapterPosition(), quiz);
                }
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

    public class QuizViewHolder extends RecyclerView.ViewHolder{

        TextView tvQuizTitle;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuizTitle = itemView.findViewById(R.id.tvQuizTitle);
        }
    }


}

