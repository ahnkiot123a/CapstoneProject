package com.koit.capstonproject_version_1.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.koit.capstonproject_version_1.R;

public class CustomerActivity extends AppCompatActivity {
    private ImageView btnAddNewCustomer, btnSearchCustomer;
    private RecyclerView recyclerViewCustomers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        initView();

    }

    private void initView() {
        btnAddNewCustomer = findViewById(R.id.btnAddNewCustomer);
        btnSearchCustomer = findViewById(R.id.btnSearchCustomer);
        recyclerViewCustomers = findViewById(R.id.recyclerViewCustomers);
    }


}