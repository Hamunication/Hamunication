package com.dx3evm.hamunication.Dialogs;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.dx3evm.hamunication.R;

import java.lang.reflect.Field;

public class InputDialog {
    private AlertDialog alertDialog;
    private com.google.android.material.textfield.TextInputEditText editText, editTextArea;
    private TextView tvError;
    private ImageView imageView;
    Context context;

    private Activity activity;


    public void showDialog(Activity activity, Context context, String title, final OnDialogClickListener onDialogClickListener){
        this.activity = activity;
        this.context = context;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_input, null);
        builder.setTitle("Add New " + title);
        builder.setView(dialogView);

        String dialogName = title;

        editText = dialogView.findViewById(R.id.etInput);
        editTextArea = dialogView.findViewById(R.id.etDescription);
        tvError = dialogView.findViewById(R.id.tvError);
        imageView = dialogView.findViewById(R.id.ivImgSelect);
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
                try{
                    Field mUriField = ImageView.class.getDeclaredField("mUri");
                    mUriField.setAccessible(true);
                    Uri imageUri = (Uri) mUriField.get(imageView);

                    String title = editText.getText().toString();
                    String description = editTextArea.getText().toString();


                    if(title.isEmpty()){
                        tvError.setVisibility(View.VISIBLE);
                    }else{
                        if(dialogName.equals("course")){
                            onDialogClickListener.OnSaveCourse(title, description, imageUri);
                        }
                        else{
                            onDialogClickListener.onSave(title);
                        }
                        tvError.setVisibility(View.GONE);
                        alertDialog.dismiss();
                    }
                }
                catch (Exception ex) {

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
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        if(!title.equals("course")){
            imageView.setVisibility(View.GONE);
            editTextArea.setVisibility(View.GONE);
        }

        alertDialog = builder.create();
        alertDialog.show();
    }

    public interface OnDialogClickListener {
        void onSave(String input);
        void OnSaveCourse(String courseName, String courseDescription, Uri imageUri);
        void onCancel();
    }

    public void setImage(Uri uri){
        imageView.setImageURI(uri);
    }

    private void openGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(galleryIntent, 1);
    }


}
