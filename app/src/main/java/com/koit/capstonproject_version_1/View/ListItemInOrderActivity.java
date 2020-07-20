package com.koit.capstonproject_version_1.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.koit.capstonproject_version_1.R;

public class ListItemInOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item_in_order);
    }

    public void back(View view) {
        onBackPressed();
    }

    public void searchByBarcode(View view) {
    }
}