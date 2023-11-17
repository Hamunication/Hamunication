package com.dx3evm.hamunication.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dx3evm.hamunication.Adapters.CourseAdapter;
import com.dx3evm.hamunication.AdminMainActivity;
import com.dx3evm.hamunication.EditCourseActivity;
import com.dx3evm.hamunication.Models.Course;
import com.dx3evm.hamunication.R;
import com.dx3evm.hamunication.ViewCourseActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseFragment extends Fragment {

    CourseAdapter courseAdapter;
    List<Course> courseList;
    RecyclerView rvCourseList;
    public static final String NEXT_SCREEN = "details_screen";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseFragment newInstance(String param1, String param2) {
        CourseFragment fragment = new CourseFragment();
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
        View fragmentView = inflater.inflate(R.layout.fragment_course, container, false);

        rvCourseList = fragmentView.findViewById(R.id.rvCourseList);
        courseList = new ArrayList<>();
        courseAdapter = new CourseAdapter(getActivity(), courseList);
        courseAdapter.setOnClickListener(new CourseAdapter.OnClickListener() {
            @Override
            public void onClick(int position, Course course) {
                Intent intent = new Intent(getActivity(), ViewCourseActivity.class);

                intent.putExtra(NEXT_SCREEN, course);
                startActivity(intent);
            }
        });

        rvCourseList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCourseList.setAdapter(courseAdapter);

        displayCourses();

        return fragmentView;
    }

    public void displayCourses(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Courses");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                courseList.clear();
                try {
                    for(DataSnapshot courseSnapshot : snapshot.getChildren()){
                        String courseId = courseSnapshot.getKey();
                        String courseTitle = courseSnapshot.child("Title").getValue(String.class);

                        Course course = new Course();
                        course.setId(courseId);
                        course.setTitle(courseTitle);

                        courseList.add(course);
                    }
                    courseAdapter.notifyDataSetChanged();

                } catch (Exception ex) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("")
                            .setMessage(ex.getMessage())
                            .setPositiveButton("Ok", null).show();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}