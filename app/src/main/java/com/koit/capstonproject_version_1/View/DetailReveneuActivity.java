package com.koit.capstonproject_version_1.View;

import android.os.Bundle;
import android.view.View;

import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.R;

import androidx.appcompat.app.AppCompatActivity;

public class DetailReveneuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_detail_reveneu);
    }

    public void back(View view) {
        onBackPressed();
    }
}