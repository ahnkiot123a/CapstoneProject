package com.koit.capstonproject_version_1.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.koit.capstonproject_version_1.Adapter.CustomerAdapter;
import com.koit.capstonproject_version_1.Adapter.EditConvertRateAdapter;
import com.koit.capstonproject_version_1.Controller.CustomerController;
import com.koit.capstonproject_version_1.R;

public class CustomerActivity extends AppCompatActivity {
    private ImageView btnAddNewCustomer, btnSearchCustomer;
    private RecyclerView recyclerViewCustomers;
    private CustomerController customerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        initView();
        buildRecyclerviewCustomers();
    }

    private void initView() {
        btnAddNewCustomer = findViewById(R.id.btnAddNewCustomer);
        btnSearchCustomer = findViewById(R.id.btnSearchCustomer);
        recyclerViewCustomers = findViewById(R.id.recyclerViewCustomers);
    }
    private void buildRecyclerviewCustomers() {
      customerController = new CustomerController(this.getApplicationContext());
      customerController.getListCustomer(recyclerViewCustomers);
    }
    public  void addNewCustomer(View view){
        Intent intent = new Intent(CustomerActivity.this,AddNewCustomerActivity.class);
        startActivity(intent);
    }
    public void searchCustomer(View view){

    }
    public void back(View view){
        onBackPressed();
    }


}