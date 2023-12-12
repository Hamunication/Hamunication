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
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseViewHolder> {
    Context context;
    List<Course> courseList;
    private OnClickListener onClickListener;


    public CourseAdapter(Context context, List<Course> courseList){
        this.context = context;
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item_new, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.tvCourseTitle.setText(course.getTitle());
        holder.tvCourseDescription.setText(course.getDescription());
        Glide.with(context).load(course.getImg()).into(holder.ivCourseImg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onClickListener != null){
                    onClickListener.onClick(holder.getAdapterPosition(), course);
                }
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
}

class CourseViewHolder extends RecyclerView.ViewHolder {

    TextView tvCourseTitle;
    TextView tvCourseDescription;
    ImageView ivCourseImg;

    public CourseViewHolder(@NonNull View itemView) {
        super(itemView);

        tvCourseTitle = itemView.findViewById(R.id.tvCourseTitle);
        tvCourseDescription = itemView.findViewById(R.id.tvCourseDescription);
        ivCourseImg = itemView.findViewById(R.id.ivCourseImg);

    }
}
