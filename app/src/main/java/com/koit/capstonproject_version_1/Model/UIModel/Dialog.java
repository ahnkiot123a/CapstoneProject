package com.koit.capstonproject_version_1.Model.UIModel;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.koit.capstonproject_version_1.R;

public class Dialog {


    private Activity activity;
    private AlertDialog dialog;
    private LottieAnimationView animationView;

    public Dialog(Activity activity) {
        this.activity = activity;
    }

    public void showLoadingDialog(int resId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialog_loading, null);
        animationView = view.findViewById(R.id.animationView);
        animationView.setAnimation(resId);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }

    public void dismissLoadingDialog(){
        dialog.dismiss();
    }
}
