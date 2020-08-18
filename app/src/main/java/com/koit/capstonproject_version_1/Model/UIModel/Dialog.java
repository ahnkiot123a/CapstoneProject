package com.koit.capstonproject_version_1.Model.UIModel;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.koit.capstonproject_version_1.R;

public class Dialog {


    private Activity activity;
    private AlertDialog dialog;

    public Dialog(Activity activity) {
        this.activity = activity;
    }

    public void showLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog_loading, null));
        dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }

    public void dismissLoadingDialog(){
        dialog.dismiss();
    }
}
