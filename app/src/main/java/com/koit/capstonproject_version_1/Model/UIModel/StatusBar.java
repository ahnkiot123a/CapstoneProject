package com.koit.capstonproject_version_1.Model.UIModel;

import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.koit.capstonproject_version_1.R;

import androidx.appcompat.app.AppCompatActivity;

public class StatusBar {
    public  static  void setStatusBar(AppCompatActivity activity){
        // make the main layout fill the screen
        Window window = activity.getWindow();
        final int layoutFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        window.getDecorView().setSystemUiVisibility(layoutFlags);
        activity.supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

        // make the status bar translucent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(0x00000000);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // N.B. on some (most?) KitKat devices the status bar has a pre-defined gradient that
            // cannot be overridden.
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        // set the height of the scrim to standard status bar height for this device (normally 26dp)
        View statusBarScrim = activity.findViewById(R.id.main_activityStatusBarScrim);
        int height;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = activity.getResources().getDimensionPixelSize(resourceId);
        } else {
            // belt and braces
            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 26f, activity.getResources().getDisplayMetrics());
        }
        if (statusBarScrim != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ViewGroup.LayoutParams lp = statusBarScrim.getLayoutParams();
                lp.height = height;
                statusBarScrim.setLayoutParams(lp);
                statusBarScrim.setVisibility(View.VISIBLE);
            }
        }
    }
}
