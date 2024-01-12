    package com.dx3evm.hamunication.Dialogs;

    import android.app.Activity;
    import android.app.AlertDialog;
    import android.content.Context;
    import android.net.Uri;
    import android.text.Editable;
    import android.text.TextWatcher;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.TextView;

    import com.dx3evm.hamunication.R;
    import com.google.android.material.button.MaterialButton;
    import com.google.android.material.textfield.TextInputEditText;

public class DeleteAccountDialog {

    private AlertDialog alertDialog;
    private EditText etDialogEmail, etDialogPassword;
    private TextView tvErrorEmail, tvErrorPassword;

    Context context;

    private Activity activity;

    public void showDialog(Activity activity, Context context, String fullName, final OnDialogClickListener onDialogClickListener){
        this.activity = activity;
        this.context = context;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View deleteDialogView = inflater.inflate(R.layout.dialog_delete_account, null);
        builder.setTitle("Delete Account");
        builder.setMessage("Are you sure? This will delete " + fullName + " account.\n\n Verify your account to proceed.");
        builder.setView(deleteDialogView);

        etDialogEmail = deleteDialogView.findViewById(R.id.etDialogEmail);
        etDialogPassword = deleteDialogView.findViewById(R.id.etDialogPassword);
        tvErrorEmail = deleteDialogView.findViewById(R.id.tvErrorEmail);
        tvErrorPassword = deleteDialogView.findViewById(R.id.tvErrorPassword);
        MaterialButton btnCancel = deleteDialogView.findViewById(R.id.btnCancel);
        MaterialButton btnDelete = deleteDialogView.findViewById(R.id.btnDelete);


        etDialogEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvErrorEmail.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etDialogPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvErrorPassword.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDialogClickListener.onCancel();
                alertDialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etDialogEmail.getText().toString();
                String password = etDialogPassword.getText().toString();

                if(email.isEmpty()){
                    tvErrorEmail.setVisibility(View.VISIBLE);
                }
                if(password.isEmpty()){
                    tvErrorPassword.setVisibility(View.VISIBLE);
                }
                else{
                    onDialogClickListener.onDelete(email, password);
                    alertDialog.dismiss();
                }
            }
        });

        alertDialog = builder.create();
        alertDialog.show();

    }

    public interface OnDialogClickListener {
        void onDelete(String email, String password);
        void onCancel();
    }

}
