package com.dx3evm.hamunication.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dx3evm.hamunication.R;

public class InputDialog {
    private AlertDialog alertDialog;
    private com.google.android.material.textfield.TextInputEditText editText;
    private TextView tvError;


    public void showDialog(Context context, String title, final OnDialogClickListener onDialogClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_input, null);
        builder.setTitle("Add New " + title);
        builder.setView(dialogView);

        editText = dialogView.findViewById(R.id.etInput);
        tvError = dialogView.findViewById(R.id.tvError);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        editText.setHint("Enter " + title + " name");
        tvError.setText("Please enter " + title + " name");

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = editText.getText().toString();
                if(input.isEmpty()){
                    tvError.setVisibility(View.VISIBLE);
                }else{
                    tvError.setVisibility(View.GONE);
                    onDialogClickListener.onSave(input);
                    alertDialog.dismiss();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDialogClickListener.onCancel();
                alertDialog.dismiss();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();
    }

    public interface OnDialogClickListener {
        void onSave(String input);
        void onCancel();
    }
}
