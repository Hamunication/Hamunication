package com.dx3evm.hamunication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dx3evm.hamunication.Adapters.NewsAdapter;
import com.dx3evm.hamunication.Models.News;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllNewsActivity extends AppCompatActivity {

    List<News> newsList;
    NewsAdapter newsAdapter;
    TextView tvBack;
    RecyclerView rvNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_news);

        tvBack = findViewById(R.id.tvBack);

        rvNews = findViewById(R.id.rvNews);

        displayNews();

        newsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(newsList);

        rvNews.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rvNews.setAdapter(newsAdapter);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void displayNews(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("News");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newsList.clear();
                try{
                    for(DataSnapshot newsSnapshot : snapshot.getChildren()){
                        String newsId = newsSnapshot.getKey();
                        String newsImage = newsSnapshot.child("newsImage").getValue(String.class);
                        String newsTitle = newsSnapshot.child("newsTitle").getValue(String.class);
                        String newsEditor = newsSnapshot.child("newsEditor").getValue(String.class);
                        String newsTime = newsSnapshot.child("newsTime").getValue(String.class);

                        News news = new News();
                        news.setNewsId(newsId);
                        news.setNewsImage(newsImage);
                        news.setNewsTitle(newsTitle);
                        news.setNewsEditor(newsEditor);
                        news.setNewsTime(newsTime);

                        newsList.add(news);

                    }
                    newsAdapter.notifyDataSetChanged();
                }catch(Exception ex){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}