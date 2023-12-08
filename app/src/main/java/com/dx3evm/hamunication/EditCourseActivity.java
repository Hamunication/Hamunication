package com.dx3evm.hamunication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dx3evm.hamunication.Adapters.ModuleAdapter;
import com.dx3evm.hamunication.Dialogs.InputDialog;
import com.dx3evm.hamunication.Models.Course;
import com.dx3evm.hamunication.Models.Module;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EditCourseActivity extends AppCompatActivity {

    ModuleAdapter moduleAdapter;
    TextView tvCourseName;
    List<Module> moduleList;
    MaterialButton mtrlBtnAddModule;
    RecyclerView rvModuleList;

    Course course = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        tvCourseName = findViewById(R.id.tvCourseName);
        mtrlBtnAddModule = findViewById(R.id.mtrlBtnAddModule);
        rvModuleList = findViewById(R.id.rvModuleList);

        moduleList = new ArrayList<>();
        moduleAdapter = new ModuleAdapter(this, moduleList);
        moduleAdapter.setOnClickListener(new ModuleAdapter.OnClickListener() {
            @Override
            public void onClick(int position, Module module) {
                Intent intent = new Intent(EditCourseActivity.this, EditModuleActivity.class);
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

        mtrlBtnAddModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputDialog inputDialog = new InputDialog();

                inputDialog.showDialog(EditCourseActivity.this, "module", new InputDialog.OnDialogClickListener() {
                    @Override
                    public void onSave(String input) {
                        createModule(course.getId(), input);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });

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

    public void createModule(String courseId, String moduleName){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(courseId);

        databaseReference.child("Module").push().child("Title").setValue(moduleName).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(EditCourseActivity.this, "Module added Successfully!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditCourseActivity.this, "Error adding module!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}