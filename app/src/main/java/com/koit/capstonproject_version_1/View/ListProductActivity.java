package com.koit.capstonproject_version_1.View;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.koit.capstonproject_version_1.Controller.ListProductController;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class ListProductActivity extends AppCompatActivity {
    private EditText etSearchField;
    private ImageView imageView;
    private ImageButton imgbtnBarcode;
    private Spinner category_Spinner;
    private TextView tvTotalQuantity;
    private RecyclerView recyclerViewListProduct;
    private ListProductController listProductController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);
        etSearchField = findViewById(R.id.etSeachField);
        imageView = findViewById(R.id.imageView);
        imgbtnBarcode = findViewById(R.id.imgbtnBarcode);
        category_Spinner = findViewById(R.id.category_Spinner);
        tvTotalQuantity = findViewById(R.id.tvTotalQuantity);
        recyclerViewListProduct = findViewById(R.id.recyclerViewListProduct);


        listProductController = new ListProductController(this);

        etSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listProductController.firebaseProductSearch();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


}