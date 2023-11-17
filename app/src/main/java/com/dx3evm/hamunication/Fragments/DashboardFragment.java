package com.dx3evm.hamunication.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dx3evm.hamunication.Adapters.NewsAdapter;
import com.dx3evm.hamunication.Models.News;
import com.dx3evm.hamunication.R;


import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    List<News> newsItems;
    //List<CourseModel> courseItems;

    RecyclerView rvNews, rvCourses;

    NewsAdapter newsAdapter;

    //CourseAdapter courseAdapter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        newsItems = new ArrayList<>();
        //courseItems = new ArrayList<>();

        newsItems.add(new News(R.drawable.img_news_ph_gold, "Asian Games medalists from the Philippines", "Fred Villanueva", "1 hour ago"));
        newsItems.add(new News(R.drawable.img_news_cyclone, "Signal No. 3: Tropical Cyclone Jenny will hit some area of the Philippines", "Fred Villanueva", "1 hour ago"));
        newsItems.add(new News(R.drawable.img_news_ph_ayungin, "PCG to China Coast Guard, militia: Stop ‘illegal’ acts within PH waters", "Fred Villanueva", "1 hour ago"));
        newsAdapter = new NewsAdapter(newsItems);

        // courseItems.add(new CourseModel(R.drawable.class_a, "Class A", "Fred Villanueva", 45, "45% complete"));
        // courseItems.add(new CourseModel(R.drawable.class_b, "Class B", "Fred Villanueva", 0, "0% complete"));
        // courseItems.add(new CourseModel(R.drawable.class_c, "Class C", "Fred Villanueva", 0, "0% complete"));
        // courseItems.add(new CourseModel(R.drawable.class_d, "Class D", "Fred Villanueva", 0, "0% complete"));
        // courseAdapter = new CourseAdapter(courseItems);

        rvNews = fragmentView.findViewById(R.id.rvNews);
        // rvCourses = fragmentView.findViewById(R.id.rvCourses);

        rvNews.setLayoutManager(new LinearLayoutManager(fragmentView.getContext(), LinearLayoutManager.VERTICAL, false));
        rvNews.setAdapter(newsAdapter);

        // rvCourses.setLayoutManager(new LinearLayoutManager(fragmentView.getContext()));
        // rvCourses.setAdapter(courseAdapter);

        return fragmentView;
    }
}