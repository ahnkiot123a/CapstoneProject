package com.koit.capstonproject_version_1.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.airbnb.lottie.LottieAnimationView;
import com.koit.capstonproject_version_1.R;

public class MyDialog {

    //    private AlertDialog dialogConnection;
    private Activity activity;
    private AlertDialog dialog;
    private LottieAnimationView animationView;
    private Dialog dialogConnection;


    public MyDialog(Activity activity) {
        this.activity = activity;
        dialogConnection = new Dialog(activity);
    }

    public void showLoadingDialog(int resId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialog_loading, null);
        animationView = view.findViewById(R.id.animationView);
        animationView.setAnimation(resId);
        builder.setView(view);
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void dismissLoadingDialog() {
        dialog.dismiss();
    }

    public void showInternetError() {
        if (dialogConnection != null) {
            dialogConnection.setCancelable(false);
            dialogConnection.setContentView(R.layout.alert_dialog_network_checking);
            dialogConnection.setCanceledOnTouchOutside(false);
            dialogConnection.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialogConnection.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogConnection.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
            dialogConnection.show();
        }
    }

    public void cancelConnectionDialog() {
        dialogConnection.cancel();
    }

    public void showDefaultLoadingDialog(int resId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialog_default_loading, null);
        animationView = view.findViewById(R.id.animationView);
        animationView.setAnimation(resId);
        builder.setView(view);
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void dismissDefaultLoadingDialog() {
        dialog.dismiss();
    }


}
