package com.dx3evm.hamunication.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dx3evm.hamunication.Models.News;
import com.dx3evm.hamunication.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder>{

    List<News> newsItems;

    public NewsAdapter(List<News> newsItems){
        this.newsItems = newsItems;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);

        return new NewsViewHolder(view).newsViewHolderAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        String newsImage = newsItems.get(position).getNewsImage();
        String newsTitle = newsItems.get(position).getNewsTitle();
        String newsEditor = newsItems.get(position).getNewsEditor();
        String newsTime = newsItems.get(position).getNewsTime();

        holder.setData(newsImage, newsTitle, newsEditor, newsTime);
    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }
}

class NewsViewHolder extends RecyclerView.ViewHolder{

    ImageView ivNews;
    TextView tvTitle, tvEditor, tvTime;

    private NewsAdapter newsAdapter;

    public NewsViewHolder(@NonNull View itemView) {
        super(itemView);
        ivNews = itemView.findViewById(R.id.ivNews);
        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvEditor = itemView.findViewById(R.id.tvEditor);
        tvTime = itemView.findViewById(R.id.tvTime);
    }

    public void setData(String newsImage, String newsTitle, String newsEditor, String newsTime) {
        tvTitle.setText(newsTitle);
        tvEditor.setText(newsEditor);
        tvTime.setText(newsTime);
        Glide.with(itemView).load(newsImage).into(ivNews);
    }

    public NewsViewHolder newsViewHolderAdapter(NewsAdapter newsAdapter){
        this.newsAdapter = newsAdapter;
        return this;
    }

}
