package com.dx3evm.hamunication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dx3evm.hamunication.Models.Course;
import com.dx3evm.hamunication.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseViewHolder> {

    Context context;
    List<Course> courseList;
    private OnClickListener onClickListener;

    private OnLongClickListener onLongClickListener;


    public CourseAdapter(Context context, List<Course> courseList){
        this.context = context;
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item_new, parent, false);
        return new CourseViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.tvCourseTitle.setText(course.getTitle());
        holder.tvCourseDescription.setText(course.getDescription());
        Glide.with(context).load(course.getImg()).into(holder.ivCourseImg);
        holder.tvCourseStatus.setText("Completed " + course.getProgress() + "%");
        holder.courseProgress.setProgress(Integer.parseInt(course.getProgress()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onClickListener != null){
                    onClickListener.onClick(holder.getAdapterPosition(), course);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(onLongClickListener != null){
                    onLongClickListener.onLongClick(holder.getAdapterPosition(), course);
                }
                return true;
            }
        });



    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, Course course);
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public interface OnLongClickListener {
        void onLongClick(int position, Course course);
    }

}

class CourseViewHolder extends RecyclerView.ViewHolder {

    TextView tvCourseTitle;
    TextView tvCourseDescription;

    TextView tvCourseStatus;

    LinearProgressIndicator courseProgress;
    ImageView ivCourseImg;


    public CourseViewHolder(@NonNull View itemView) {
        super(itemView);

        tvCourseTitle = itemView.findViewById(R.id.tvCourseTitle);
        tvCourseDescription = itemView.findViewById(R.id.tvCourseDescription);
        ivCourseImg = itemView.findViewById(R.id.ivCourseImg);
        courseProgress = itemView.findViewById(R.id.courseProgress);
        tvCourseStatus = itemView.findViewById(R.id.tvCourseStatus);

    }
}
