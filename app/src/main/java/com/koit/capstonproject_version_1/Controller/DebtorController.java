package com.koit.capstonproject_version_1.Controller;

import android.content.Context;
import android.widget.RadioButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.Adapter.DebtorAdapter;
import com.koit.capstonproject_version_1.Controller.Interface.ICustomer;
import com.koit.capstonproject_version_1.Model.Debtor;

import java.util.ArrayList;
import java.util.List;

public class DebtorController {
    private Context context;
    private Debtor debtor;
    private List<Debtor> debtorList;
    private DebtorAdapter debtorAdapter;

    public DebtorController(Context context) {
        this.context = context;
        debtor = new Debtor();
    }

    public void getListDebtor(RecyclerView recyclerViewDebtor) {
        debtorList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerViewDebtor.setLayoutManager(layoutManager);
        debtorAdapter = new DebtorAdapter(debtorList,context);
        recyclerViewDebtor.setAdapter(debtorAdapter);
        ICustomer iCustomer = new ICustomer() {
            @Override
            public void getCustomer(Debtor debtor) {
                debtorList.add(debtor);
                debtorAdapter.notifyDataSetChanged();
            }



        };
        debtor.getListDebtor(iCustomer);

    }

    public void createDebtor(TextInputEditText edFullname, TextInputEditText edEmail, TextInputEditText edPhoneNumber,
                             TextInputEditText edDob, TextInputEditText edAddress, RadioButton rbMale) {
        String fullName = edFullname.getText().toString().trim();
        String email = edEmail.getText().toString().trim();
        String phoneNumber = edPhoneNumber.getText().toString().trim();
        String dob = edDob.getText().toString().trim();
        String address = edAddress.getText().toString().trim();
        boolean gender = (rbMale.isChecked()) ? true : false;
        if (fullName.isEmpty()) {
            edFullname.setError("Tên khách hàng không được để trống");
        } else {
            String debtorId = RandomStringController.getInstance().randomDebtorId();
            Debtor debtor = new Debtor();
            debtor.setDebtorId(debtorId);
            debtor.setFullName(fullName);
            debtor.setEmail(email);
            debtor.setPhoneNumber(phoneNumber);
            debtor.setDateOfBirth(dob);
            debtor.setAddress(address);
            debtor.setGender(gender);
            debtor.addDebtorToFirebase(debtor);
           /* String category = tvCategory.getText().toString().trim();

            ArrayList<Unit> listUnit = CreateProductActivity.getUnitFromRv();
            String photoName = CreateProductActivity.photoName;
            String productID = RandomStringController.getInstance().randomString();
//                    Log.i("photoPath", photoName);
            Product product = new Product();
            product.setProductId(productID);
            product.setBarcode(barcode);
            product.setProductName(productName);
            product.setProductDescription(tetDescription.getText().toString().trim());
            product.setCategoryName(category);
            product.setProductImageUrl(photoName);
            product.setActive(checked);
            product.setUnits(listUnit);
            Log.i("addProduct", product.toString());

            Intent intent = new Intent(activity, ConvertRateActivity.class);
            intent.putExtra(CreateProductActivity.NEW_PRODUCT, product);
            activity.startActivity(intent);
*/
        }

    }
}
