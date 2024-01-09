package com.dx3evm.hamunication.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dx3evm.hamunication.Models.Score;
import com.dx3evm.hamunication.R;

import java.util.List;

public class ScoreBoardAdapter extends RecyclerView.Adapter<ScoreBoardViewHolder>{

    List<Score> scoreList;

    public ScoreBoardAdapter() {}

    public ScoreBoardAdapter(List<Score> scoreList) {
        this.scoreList = scoreList;
    }

    @NonNull
    @Override
    public ScoreBoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scoreboard_item, parent, false);
        return new ScoreBoardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreBoardViewHolder holder, int position) {
        Score score = scoreList.get(position);

        holder.tvRank.setText(String.valueOf(position + 1));
        holder.tvUserFullName.setText(score.getUserFullName());
        holder.tvScore.setText(score.getScore());
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }
}

class ScoreBoardViewHolder extends RecyclerView.ViewHolder{

    TextView tvRank, tvUserFullName, tvScore;

    public ScoreBoardViewHolder(@NonNull View itemView) {
        super(itemView);

        tvRank = itemView.findViewById(R.id.tvRank);
        tvUserFullName = itemView.findViewById(R.id.tvUserFullName);
        tvScore = itemView.findViewById(R.id.tvScore);
    }
}