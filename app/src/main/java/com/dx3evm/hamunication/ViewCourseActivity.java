package com.dx3evm.hamunication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.dx3evm.hamunication.Adapters.ModuleAdapter;
import com.dx3evm.hamunication.Models.Course;
import com.dx3evm.hamunication.Models.Module;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewCourseActivity extends AppCompatActivity {

    ModuleAdapter moduleAdapter;
    TextView tvCourseName;
    List<Module> moduleList;
    RecyclerView rvModuleList;

    Course course = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_course);

        tvCourseName = findViewById(R.id.tvCourseName);
        rvModuleList = findViewById(R.id.rvModuleList);

        moduleList = new ArrayList<>();
        moduleAdapter = new ModuleAdapter(this, moduleList);
        moduleAdapter.setOnClickListener(new ModuleAdapter.OnClickListener() {
            @Override
            public void onClick(int position, Module module) {
                Intent intent = new Intent(ViewCourseActivity.this, ViewModuleActivity.class);
                intent.putExtra("course", course);
                intent.putExtra("module", module);

                startActivity(intent);
            }
        });

        rvModuleList.setLayoutManager(new LinearLayoutManager(this));
        rvModuleList.setAdapter(moduleAdapter);

        if(getIntent().hasExtra(CreateCourseActivity.NEXT_SCREEN)){
            course = (Course) getIntent().getSerializableExtra(CreateCourseActivity.NEXT_SCREEN);

            if(course != null){
                tvCourseName.setText(course.getTitle());
                displayModules(course.getId());
//                if(moduleList.isEmpty()){
//                    Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
//                }else{
//
//                }
            }
        }
    }
    public void displayModules(String courseID){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(courseID);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                moduleList.clear();
                try{
                    for(DataSnapshot moduleSnapshot : snapshot.child("Module").getChildren()){
                        String moduleId = moduleSnapshot.getKey();
                        String moduleTitle = moduleSnapshot.child("Title").getValue(String.class);

                        Module module = new Module();
                        module.setId(moduleId);
                        module.setTitle(moduleTitle);

                        moduleList.add(module);
                    }
                    moduleAdapter.notifyDataSetChanged();
                }catch(Exception ex){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}