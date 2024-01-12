package com.dx3evm.hamunication.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dx3evm.hamunication.Adapters.CourseAdapter;
import com.dx3evm.hamunication.Adapters.NewsAdapter;
import com.dx3evm.hamunication.AllNewsActivity;
import com.dx3evm.hamunication.Models.Course;
import com.dx3evm.hamunication.Models.Module;
import com.dx3evm.hamunication.Models.News;
import com.dx3evm.hamunication.R;
import com.dx3evm.hamunication.ViewCourseActivity;
import com.dx3evm.hamunication.ViewNewsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    public static final String NEXT_SCREEN = "details_screen";
    FirebaseDatabase firebaseDatabase;

    FirebaseAuth fAuth;
    List<News> newsList;
    List<Course> courseList;

    TextView tvUserName, tvNewsSeeAll;
    RecyclerView rvNews, rvCourses;

    NewsAdapter newsAdapter;

    CourseAdapter courseAdapter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public DashboardFragment() {
    }

    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();

        newsList = new ArrayList<>();
        courseList = new ArrayList<>();

        displayNews();
        displayCourses();

        newsAdapter = new NewsAdapter(newsList);

        newsAdapter.setOnClickListener(new NewsAdapter.OnClickListener() {
            @Override
            public void onClick(int position, News news) {
                Intent intent = new Intent(getActivity(), ViewNewsActivity.class);
                intent.putExtra(NEXT_SCREEN, news);
                startActivity(intent);
            }
        });

        courseAdapter = new CourseAdapter(getContext().getApplicationContext(), courseList);

        courseAdapter.setOnClickListener(new CourseAdapter.OnClickListener() {
            @Override
            public void onClick(int position, Course course) {
                Intent intent = new Intent(getActivity(), ViewCourseActivity.class);

                intent.putExtra(NEXT_SCREEN, course);
                startActivity(intent);
            }
        });

        rvNews = fragmentView.findViewById(R.id.rvNews);
        rvCourses = fragmentView.findViewById(R.id.rvCourses);

        rvNews.setLayoutManager(new LinearLayoutManager(fragmentView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvNews.setAdapter(newsAdapter);

        rvCourses.setLayoutManager(new LinearLayoutManager(fragmentView.getContext()));
        rvCourses.setAdapter(courseAdapter);

        tvUserName = fragmentView.findViewById(R.id.tvUserName);

        tvNewsSeeAll = fragmentView.findViewById(R.id.tvNewsSeeAll);
        tvNewsSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(fragmentView.getContext(), AllNewsActivity.class));
            }
        });

        getUserDetails();

        return fragmentView;
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
                        String newsDescription = newsSnapshot.child("newsDescription").getValue(String.class);
                        String newsEditor = newsSnapshot.child("newsEditor").getValue(String.class);
                        String newsTime = newsSnapshot.child("newsTime").getValue(String.class);

                        News news = new News();
                        news.setNewsId(newsId);
                        news.setNewsImage(newsImage);
                        news.setNewsTitle(newsTitle);
                        news.setNewsDescription(newsDescription);
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

    public void displayCourses() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            String UID = firebaseAuth.getCurrentUser().getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Courses");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    courseList.clear();
                    try {
                        for (DataSnapshot courseSnapshot : snapshot.getChildren()) {

                            float totalTopics = 0.0f;
                            float totalQuiz = 0.0f;
                            float topicCompleted = 0.0f;
                            float quizCompleted = 0.0f;
                            String courseId = courseSnapshot.getKey();
                            String courseTitle = courseSnapshot.child("Title").getValue(String.class);
                            String courseDesc = courseSnapshot.child("Description").getValue(String.class);
                            String courseImg = courseSnapshot.child("Image").getValue(String.class);

                            Course course = new Course();
                            course.setId(courseId);
                            course.setTitle(courseTitle);
                            course.setDescription(courseDesc);
                            course.setImg(courseImg);

                            if (courseSnapshot.hasChild("Module")) {
                                for (DataSnapshot moduleSnapshot : courseSnapshot.child("Module").getChildren()) {
                                    if (moduleSnapshot.hasChild("Topic")) {
                                        totalTopics += moduleSnapshot.child("Topic").getChildrenCount();
                                        for (DataSnapshot topicSnapshot : moduleSnapshot.child("Topic").getChildren()) {
                                            if (topicSnapshot.hasChild("CompletedUsers")) {

                                                for (DataSnapshot userSnapshot : topicSnapshot.child("CompletedUsers").getChildren()) {
                                                    String userId = userSnapshot.getKey();

                                                    if (userId != null && userId.equals(firebaseAuth.getCurrentUser().getUid())) {
                                                        topicCompleted++;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if(moduleSnapshot.hasChild("Quiz")){
                                        totalQuiz += moduleSnapshot.child("Quiz").getChildrenCount();
                                        for(DataSnapshot quizSnapshot : moduleSnapshot.child("Quiz").getChildren()){
                                            if (quizSnapshot.hasChild("CompletedUsers")) {

                                                for (DataSnapshot userSnapshot : quizSnapshot.child("CompletedUsers").getChildren()) {
                                                    String userId = userSnapshot.getKey();

                                                    if (userId != null && userId.equals(firebaseAuth.getCurrentUser().getUid())) {
                                                        quizCompleted++;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            float totalPercentage = 0.0f;
                            if(topicCompleted > 0){
                                totalPercentage = ((topicCompleted + quizCompleted) / (totalTopics + totalQuiz)) * 100;
                            }else{
                                totalPercentage = 0.0f;
                            }

                            course.setProgress(String.valueOf(Math.round(totalPercentage)));
                            courseList.add(course);
                        }
                        courseAdapter.notifyDataSetChanged();
                    } catch (Exception ex) {
                        // Handle exceptions
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle onCancelled
                }
            });

        }
    }

    public void getUserDetails() {
        if(fAuth.getCurrentUser() != null){
            DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(fAuth.getCurrentUser().getUid());

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String fullName = snapshot.child("FullName").getValue(String.class);

                        tvUserName.setText(fullName + "!");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}