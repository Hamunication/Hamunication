package com.dx3evm.hamunication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateCourseActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseDatabase firebaseDatabase;

    boolean isAdmin = false;
    InputDialog inputDialog;
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
        fAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

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
        
        courseAdapter.setOnLongClickListener(new CourseAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(int position, Course course) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateCourseActivity.this);
                builder.setTitle("Delete Course")
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteCourse(course.getId());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // User clicked the Cancel button, do nothing or handle accordingly
                            }
                        }).show();
            }
        });

        rvCourseList.setLayoutManager(new LinearLayoutManager(this));
        rvCourseList.setAdapter(courseAdapter);

        displayCourses();



        mtrlBtnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputDialog = new InputDialog();

                inputDialog.showDialog(CreateCourseActivity.this, CreateCourseActivity.this, "course", new InputDialog.OnDialogClickListener() {
                    @Override
                    public void onSave(String input) {

                    }

                    @Override
                    public void OnSaveCourse(String courseName, String courseDescription, Uri imageUri) {
                        createCourse(courseName, courseDescription, String.valueOf(imageUri));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            inputDialog.setImage(selectedImage);
        }
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

                            int totalScore = 0;
                            int myScore = 0;
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
                                    if (moduleSnapshot.hasChild("Quiz")) {
                                        for (DataSnapshot quizSnapshot : moduleSnapshot.child("Quiz").getChildren()) {
                                            // Assuming each child under "Quiz" is a quiz
                                            if (quizSnapshot.hasChild("usersScore")) {
                                                if (quizSnapshot.hasChild("usersScore")) {
                                                    DataSnapshot userScoreSnapshot = quizSnapshot.child("usersScore").child(UID);

                                                    if (userScoreSnapshot.hasChild("TotalScore") && userScoreSnapshot.child("TotalScore").getValue() != null) {
                                                        totalScore += Integer.parseInt(userScoreSnapshot.child("TotalScore").getValue(String.class));
                                                        myScore += Integer.parseInt(userScoreSnapshot.child("Score").getValue(String.class));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            float totalPercentage = 0.0f;
                            if (totalScore == 0.0f || totalScore > 0 && myScore != totalScore) {
                                totalPercentage = ((float) myScore / (float) totalScore) * 100;
                                course.setProgress(String.valueOf(Math.round(totalPercentage)));
                                courseList.add(course);
                            } else {
                                totalPercentage = 0.0f;
                            }
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

    public void createCourse(String courseName, String courseDescription, String imageURI){
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference imageReference = storage.getReference().child("course_images/" + System.currentTimeMillis() + ".png");

        UploadTask uploadTask = imageReference.putFile(Uri.parse(imageURI));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Courses");

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUrl = uri.toString();

                        Map<String, Object> courseList = new HashMap<>();

                        courseList.put("Title", courseName);
                        courseList.put("Description", courseDescription);
                        courseList.put("Image", downloadUrl);

                        databaseReference.push().setValue(courseList).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                });
            }
        });
    }

    public void deleteCourse(String courseId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(courseId);

        databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(CreateCourseActivity.this, "Course deleted Successfully!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateCourseActivity.this, "Error deleting course!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}