package com.dx3evm.hamunication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dx3evm.hamunication.Models.Course;
import com.dx3evm.hamunication.Models.Topic;
import com.dx3evm.hamunication.R;

import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicViewHolder>{
    Context context;

    List<Topic> topicList;

    private OnLongClickListener onLongClickListener;

    private OnClickListener onClickListener;

    public TopicAdapter(Context context, List<Topic> topicList) {
        this.context = context;
        this.topicList = topicList;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_item, parent,false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        Topic topic = topicList.get(position);
        holder.tvTopicTitle.setText(topic.getTopicTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onClickListener != null){
                    onClickListener.onClick(holder.getAdapterPosition(), topic);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(onLongClickListener != null){
                    onLongClickListener.onLongClick(holder.getAdapterPosition(), topic);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    public void setOnClickListener(TopicAdapter.OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, Topic topic);
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public interface OnLongClickListener {
        void onLongClick(int position, Topic topic);
    }
}

class TopicViewHolder extends RecyclerView.ViewHolder{

    TextView tvTopicTitle;

    public TopicViewHolder(@NonNull View itemView) {
        super(itemView);

        tvTopicTitle = itemView.findViewById(R.id.tvTopicTitle);
    }
}
