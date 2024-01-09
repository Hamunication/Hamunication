package com.dx3evm.hamunication.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dx3evm.hamunication.Models.AdminMenu;
import com.dx3evm.hamunication.Models.Course;
import com.dx3evm.hamunication.R;

import java.util.List;

public class AdminMenuAdapter extends RecyclerView.Adapter<AdminMenuViewHolder>{

    List<AdminMenu> adminMenuList;

    OnClickListener onClickListener;

    public AdminMenuAdapter(List<AdminMenu> adminMenuList) {
        this.adminMenuList = adminMenuList;
    }

    @NonNull
    @Override
    public AdminMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_menu_item, parent,false);
        return new AdminMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminMenuViewHolder holder, int position) {
        AdminMenu adminMenu = adminMenuList.get(position);
        holder.ivOption.setImageResource(adminMenu.getImg());
        holder.tvOption.setText(adminMenu.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onClickListener != null){
                    onClickListener.onClick(holder.getAdapterPosition(), adminMenu);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return adminMenuList.size();
    }

    public void setOnClickListener(AdminMenuAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, AdminMenu adminMenu);
    }
}

class AdminMenuViewHolder extends RecyclerView.ViewHolder{

    ImageView ivOption;

    TextView tvOption;

    public AdminMenuViewHolder(@NonNull View itemView) {
        super(itemView);

        ivOption = itemView.findViewById(R.id.ivOption);
        tvOption = itemView.findViewById(R.id.tvOption);

    }
}
