package com.dx3evm.hamunication.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dx3evm.hamunication.Models.ProfileMenu;
import com.dx3evm.hamunication.R;

import java.util.List;

public class ProfileMenuAdapter extends RecyclerView.Adapter<ProfileMenuItemViewHolder>{

    List<ProfileMenu> profileMenuItems;

    public ProfileMenuAdapter(List<ProfileMenu> profileMenuItems) {
        this.profileMenuItems = profileMenuItems;
    }

    @NonNull
    @Override
    public ProfileMenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_menu_item, parent,false);
        return new ProfileMenuItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileMenuItemViewHolder holder, int position) {
        ProfileMenu profileMenu = profileMenuItems.get(position);
        holder.tvMenuTitle.setText(profileMenu.getMenuTitle());
        holder.tvMenuIcon.setText(profileMenu.getMenuIcon());
    }

    @Override
    public int getItemCount() {
        return profileMenuItems.size();
    }
}

class ProfileMenuItemViewHolder extends RecyclerView.ViewHolder{

    TextView tvMenuIcon, tvMenuTitle;
    public ProfileMenuItemViewHolder(@NonNull View itemView) {
        super(itemView);
        tvMenuIcon = itemView.findViewById(R.id.tvMenuIcon);
        tvMenuTitle = itemView.findViewById(R.id.tvMenuTitle);
    }
}