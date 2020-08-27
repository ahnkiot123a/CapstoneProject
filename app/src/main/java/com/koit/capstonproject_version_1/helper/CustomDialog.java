package com.koit.capstonproject_version_1.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.koit.capstonproject_version_1.R;

public class CustomDialog {

    Activity activity;

    public CustomDialog(Activity activity) {
        this.activity = activity;
    }

    public void showWarningDialog(String message, String buttonName){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_warning_dialog,
                (ConstraintLayout) activity.findViewById(R.id.layoutDialog));
        builder.setView(view);
        TextView tvMessage = view.findViewById(R.id.tvMessage);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnConfirm = view.findViewById(R.id.btnConfirm);
        tvMessage.setText(message);
        btnConfirm.setText(buttonName);

        final AlertDialog dialog = builder.create();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if(dialog.getWindow() != null){
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.show();
    }
}
