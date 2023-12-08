package com.dx3evm.hamunication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dx3evm.hamunication.Adapters.CourseAdapter;
import com.dx3evm.hamunication.Dialogs.InputDialog;
import com.dx3evm.hamunication.Models.Course;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class CreateCourseActivity extends AppCompatActivity {

    CourseAdapter courseAdapter;
    List<Course> courseList;
    MaterialButton mtrlBtnAddCourse;
    MaterialButton imgBtnLogout;
    RecyclerView rvCourseList;
    public static final String NEXT_SCREEN = "details_screen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);

        rvCourseList = findViewById(R.id.rvCourseList);
        mtrlBtnAddCourse = findViewById(R.id.mtrlBtnAddCourse);
        imgBtnLogout = findViewById(R.id.imgBtnLogout);

        courseList = new ArrayList<>();
        courseAdapter = new CourseAdapter(this, courseList);
        courseAdapter.setOnClickListener(new CourseAdapter.OnClickListener() {
            @Override
            public void onClick(int position, Course course) {
                Intent intent = new Intent(CreateCourseActivity.this, EditCourseActivity.class);

                intent.putExtra(NEXT_SCREEN, course);
                startActivity(intent);
            }
        });

        rvCourseList.setLayoutManager(new LinearLayoutManager(this));
        rvCourseList.setAdapter(courseAdapter);

        displayCourses();



        mtrlBtnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputDialog inputDialog = new InputDialog();

                inputDialog.showDialog(CreateCourseActivity.this, "course", new InputDialog.OnDialogClickListener() {
                    @Override
                    public void onSave(String input) {
                        createCourse(input);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });

        imgBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(CreateCourseActivity.this, LoginActivity.class));
            }
        });

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
                    new AlertDialog.Builder(CreateCourseActivity.this)
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

    public void createCourse(String courseName){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Courses");

        Course course = new Course();
            course.setTitle(courseName);

        DatabaseReference newCourseRef = databaseReference.push();
        newCourseRef.child("Title").setValue(courseName).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(CreateCourseActivity.this, "Course added Successfully!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateCourseActivity.this, "Error adding course!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}