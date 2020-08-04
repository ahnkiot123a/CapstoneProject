package com.koit.capstonproject_version_1.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.koit.capstonproject_version_1.Controller.DebtorController;
import com.koit.capstonproject_version_1.R;

public class SelectDebtorActivity extends AppCompatActivity {
    private RecyclerView recyclerViewDebitors;
    private DebtorController debtorController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debitor);
        initView();
        buildRecyclerviewCustomers();
    }

    private void initView() {
        recyclerViewDebitors = findViewById(R.id.recyclerViewDebitors);
    }
    private void buildRecyclerviewCustomers() {
      debtorController = new DebtorController(this.getApplicationContext());
      debtorController.getListDebtor(recyclerViewDebitors);
    }
    public  void addNewCustomer(View view){
        Intent intent = new Intent(SelectDebtorActivity.this, AddNewDebtorActivity.class);
        startActivity(intent);
    }
    public void searchCustomer(View view){

    }
    public void back(View view){
        onBackPressed();
    }


}