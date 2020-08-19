package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.Adapter.ListDebtorAdapter;
import com.koit.capstonproject_version_1.Adapter.SelectDebtorAdapter;
import com.koit.capstonproject_version_1.Controller.Interface.IDebtor;
import com.koit.capstonproject_version_1.Model.Debtor;
import com.koit.capstonproject_version_1.Model.Invoice;
import com.koit.capstonproject_version_1.Model.InvoiceDetail;
import com.koit.capstonproject_version_1.Model.Product;

import java.util.ArrayList;
import java.util.List;

public class DebtorController {
    private Context context;
    private Debtor debtor;
    private Invoice invoice;
    private InvoiceDetail invoiceDetail;
    private List<Product> listSelectedProductWarehouse;
    private List<Debtor> debtorList;
    private SelectDebtorAdapter selectDebtorAdapter;
    private ListDebtorAdapter listDebtorAdapter;
    private InputController inputController;

    public DebtorController(Context context) {
        this.context = context;
        inputController = new InputController();
        debtor = new Debtor();

    }

    public DebtorController(Context context, Invoice invoice, InvoiceDetail invoiceDetail, List<Product> listSelectedProductWarehouse) {
        this.context = context;
        this.invoice = invoice;
        this.invoiceDetail = invoiceDetail;
        this.listSelectedProductWarehouse = listSelectedProductWarehouse;
        debtor = new Debtor();
        inputController = new InputController();

    }

    public void getListDebtor(RecyclerView recyclerViewDebtor, Invoice invoice, InvoiceDetail invoiceDetail) {
        debtorList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerViewDebtor.setLayoutManager(layoutManager);
        selectDebtorAdapter = new SelectDebtorAdapter(debtorList, context, invoice, invoiceDetail, listSelectedProductWarehouse);
        recyclerViewDebtor.setAdapter(selectDebtorAdapter);
        IDebtor iDebtor = new IDebtor() {
            @Override
            public void getDebtor(Debtor debtor) {
                debtorList.add(debtor);
                selectDebtorAdapter.notifyDataSetChanged();
            }


        };
        debtor.getListDebtor(iDebtor);

    }
    public void getListDebtor(RecyclerView recyclerViewDebtor) {
        debtorList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerViewDebtor.setLayoutManager(layoutManager);
        listDebtorAdapter = new ListDebtorAdapter(debtorList, context);
        recyclerViewDebtor.setAdapter(listDebtorAdapter);
        IDebtor iDebtor = new IDebtor() {
            @Override
            public void getDebtor(Debtor debtor) {
                debtorList.add(debtor);
                listDebtorAdapter.notifyDataSetChanged();
            }


        };
        debtor.getListDebtor(iDebtor);

    }

    public boolean createDebtor(TextInputEditText edFullname, TextInputEditText edEmail, TextInputEditText edPhoneNumber,
                                TextView tvDob, TextInputEditText edAddress, RadioButton rbMale) {
        boolean success = false;
        String fullName = edFullname.getText().toString().trim();
        String email = edEmail.getText().toString().trim();
        String phoneNumber = edPhoneNumber.getText().toString().trim();
        String dob = tvDob.getText().toString().trim();
        String address = edAddress.getText().toString().trim();
        boolean gender = (rbMale.isChecked()) ? true : false;
        if (fullName.isEmpty()) {
            edFullname.setError("Tên khách hàng không được để trống");
        } else if (!inputController.isEmail(email)) {
            edEmail.setError("Email không hợp lệ, vui lòng nhập lại Email");
        } else if (!inputController.isPhoneNumber(phoneNumber)) {
            edPhoneNumber.setError("Số điện thoại không hợp lệ, vui lòng nhập lại số điện thoại");
        } else {
            Debtor debtor = new Debtor();

            String debtorId = RandomStringController.getInstance().randomDebtorId();
            debtor.setDebtorId(debtorId);
            debtor.setFullName(fullName);
            debtor.setEmail(email);
            debtor.setPhoneNumber(phoneNumber);
            debtor.setDateOfBirth(dob);
            debtor.setAddress(address);
            debtor.setGender(gender);
            debtor.addDebtorToFirebase(debtor);
            debtor.setDebitTotal(0);

            success = true;
        }
        return success;

    }

    public boolean updateDebtor(TextInputEditText edFullname, TextInputEditText edEmail, TextInputEditText edPhoneNumber,
                             TextView tvDob, TextInputEditText edAddress, RadioButton rbMale, Debtor debtor) {
        boolean success = false;

        String fullName = edFullname.getText().toString().trim();
        String email = edEmail.getText().toString().trim();
        String phoneNumber = edPhoneNumber.getText().toString().trim();
        String dob = tvDob.getText().toString().trim();
        String address = edAddress.getText().toString().trim();
        boolean gender = (rbMale.isChecked()) ? true : false;
        if (fullName.isEmpty()) {
            edFullname.setError("Tên khách hàng không được để trống");
        } else if (!inputController.isEmail(email)) {
            edEmail.setError("Email không hợp lệ, vui lòng nhập lại Email");
        } else if (!inputController.isPhoneNumber(phoneNumber)) {
            edPhoneNumber.setError("Số điện thoại không hợp lệ, vui lòng nhập lại số điện thoại");
        } else {

            debtor.setFullName(fullName);
            debtor.setEmail(email);
            debtor.setPhoneNumber(phoneNumber);
            debtor.setDateOfBirth(dob);
            debtor.setAddress(address);
            debtor.setGender(gender);
            debtor.updateDebtorToFirebase(debtor);
            Toast.makeText(context, "Cập nhật thông tin khách hàng thành công!", Toast.LENGTH_SHORT).show();
            success = true;

        }
        return success;

    }
    public void tranIntent(Activity activity1, Class activity2) {
        Intent intent = new Intent(activity1, activity2);
        activity1.startActivity(intent);
        activity1.finish();
    }
    public void etSearchEvent(SearchView svDebtor) {
        svDebtor.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                selectDebtorAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }
    public void etSearchEventListDebtor(SearchView svDebtor) {
        svDebtor.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listDebtorAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }
}