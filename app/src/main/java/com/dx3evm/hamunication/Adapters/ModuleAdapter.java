package com.dx3evm.hamunication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dx3evm.hamunication.Models.Module;
import com.dx3evm.hamunication.R;

import java.util.List;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleViewHolder>{

    Context context;
    List<Module> moduleList;

    private OnClickListener onClickListener;

    public ModuleAdapter(Context context, List<Module>  moduleList){
        this.context = context;
        this.moduleList = moduleList;
    }

    @NonNull
    @Override
    public ModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.module_item, parent, false);
        return new ModuleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleViewHolder holder, int position) {
        Module module = moduleList.get(position);
        holder.tvModuleTitle.setText(module.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(onClickListener != null){
                  onClickListener.onClick(holder.getAdapterPosition(), module);
              }
            }
        });
    }

    @Override
    public int getItemCount() {
        return moduleList.size();
    }

    public void setOnClickListener(ModuleAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, Module module);
    }

}

class ModuleViewHolder extends RecyclerView.ViewHolder{

    TextView tvModuleTitle;

    public ModuleViewHolder(@NonNull View itemView) {
        super(itemView);

        tvModuleTitle = itemView.findViewById(R.id.tvModuleTitle);
    }
}
