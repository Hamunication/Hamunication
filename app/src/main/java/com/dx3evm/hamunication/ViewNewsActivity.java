package com.dx3evm.hamunication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dx3evm.hamunication.Fragments.DashboardFragment;
import com.dx3evm.hamunication.Models.News;
import com.google.firebase.Firebase;
import com.google.firebase.database.FirebaseDatabase;

public class ViewNewsActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    News news;
    TextView tvNewsTitle, tvNewsDescription, tvNewsEditorAndDate, tvBack;
    ImageView ivNewsImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_news);
        firebaseDatabase = FirebaseDatabase.getInstance();
        tvNewsTitle = findViewById(R.id.tvNewsTitle);
        tvNewsDescription = findViewById(R.id.tvNewsDescription);
        tvNewsEditorAndDate = findViewById(R.id.tvNewsEditorAndDate);
        tvBack = findViewById(R.id.tvBack);
        ivNewsImg = findViewById(R.id.ivNewsImg);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(getIntent().hasExtra(DashboardFragment.NEXT_SCREEN)){
            news = (News) getIntent().getSerializableExtra(DashboardFragment.NEXT_SCREEN);

            if(news != null){
                tvNewsTitle.setText(news.getNewsTitle());
                tvNewsDescription.setText(news.getNewsDescription());
                tvNewsEditorAndDate.setText(news.getNewsEditor() + " • " + news.getNewsTime());
                Glide.with(ViewNewsActivity.this).load(news.getNewsImage()).into(ivNewsImg);
            }
        }

    }

}