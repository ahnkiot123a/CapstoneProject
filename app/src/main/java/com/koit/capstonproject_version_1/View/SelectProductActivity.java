package com.koit.capstonproject_version_1.View;

import android.os.Bundle;
import android.view.View;

import com.koit.capstonproject_version_1.R;

import androidx.appcompat.app.AppCompatActivity;

public class SelectProductActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_product);
    }

    public void back(View view) {
        onBackPressed();
    }

    public void searchByBarcode(View view) {
    }
}