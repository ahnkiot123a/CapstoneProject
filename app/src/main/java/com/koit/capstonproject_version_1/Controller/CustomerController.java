package com.koit.capstonproject_version_1.Controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.Adapter.CustomerAdapter;
import com.koit.capstonproject_version_1.Adapter.ItemAdapter;
import com.koit.capstonproject_version_1.Controller.Interface.ICustomer;
import com.koit.capstonproject_version_1.Controller.Interface.ListProductInterface;
import com.koit.capstonproject_version_1.Model.Customer;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.View.ConvertRateActivity;
import com.koit.capstonproject_version_1.View.CreateProductActivity;

import java.util.ArrayList;
import java.util.List;

public class CustomerController {
    private Context context;
    private Customer customer;
    private List<Customer> customerList;
    private CustomerAdapter customerAdapter;

    public CustomerController(Context context) {
        this.context = context;
        customer = new Customer();
    }

    public void getListCustomer(RecyclerView recyclerViewCustomer) {
        customerList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerViewCustomer.setLayoutManager(layoutManager);
        customerAdapter = new CustomerAdapter(customerList,context);
        recyclerViewCustomer.setAdapter(customerAdapter);
        ICustomer iCustomer = new ICustomer() {
            @Override
            public void getCustomer(Customer customer) {
                customerList.add(customer);
                customerAdapter.notifyDataSetChanged();
            }



        };
        customer.getListCustomer(iCustomer);

    }

    public void createCustomer(TextInputEditText edFullname, TextInputEditText edEmail, TextInputEditText edPhoneNumber,
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
            String customerId = RandomStringController.getInstance().randomCustomerId();

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
