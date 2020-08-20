package com.koit.capstonproject_version_1.helper;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class Helper {
    public static Helper mInstance;

    public Helper() {
    }

    public static Helper getInstance() {
        if (mInstance == null) mInstance = new Helper();
        return mInstance;
    }

    public int getColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public void setImage(CircleImageView ivAvatar, TextView tvFirstName, char firstName){
        //set color background of image
        ivAvatar.setImageDrawable(new ColorDrawable(Helper.getInstance().getColor()));
        tvFirstName.setText(String.valueOf(firstName));
    }

}
