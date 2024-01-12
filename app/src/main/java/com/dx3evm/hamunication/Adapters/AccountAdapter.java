package com.dx3evm.hamunication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dx3evm.hamunication.Models.Account;
import com.dx3evm.hamunication.R;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountViewHolder>{

    private List<Account> accountList;
    private Context context;

    private OnLongClickListener OnLongClickListener;

    private OnClickListener OnClickListener;

    public AccountAdapter(Context context, List<Account> accountList) {
        this.context = context;
        this.accountList = accountList;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_item, parent, false);

        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        Account account = accountList.get(position);
        holder.tvFullName.setText(account.getFullName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(OnClickListener != null){
                    OnClickListener.OnClick(holder.getAdapterPosition(), account);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(OnLongClickListener != null){
                    OnLongClickListener.onLongClick(holder.getAdapterPosition(), account);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.OnLongClickListener = onLongClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.OnClickListener = onClickListener;
    }

    public interface OnClickListener{
        void OnClick(int position, Account account);
    }

    public interface OnLongClickListener{
        void onLongClick(int position, Account account);
    }
}

class AccountViewHolder extends RecyclerView.ViewHolder{

    TextView tvFullName;

    public AccountViewHolder(@NonNull View itemView) {
        super(itemView);

        tvFullName = itemView.findViewById(R.id.tvFullName);
    }
}