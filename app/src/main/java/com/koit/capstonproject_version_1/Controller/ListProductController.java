package com.koit.capstonproject_version_1.Controller;

import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.View.ListProductActivity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListProductController {
    private ListProductActivity listProductActivity;

    public ListProductController() {
    }

    public ListProductController(ListProductActivity listProductActivity) {
        this.listProductActivity = listProductActivity;
    }

    public void firebaseProductSearch() {
        Product product = new Product();
        DatabaseReference myRef = product.getMyRef();
        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(myRef, Product.class)
                        .build();
        FirebaseRecyclerAdapter<Product, ProductViewHoler> firebaseDatabaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<Product, ListProductController.ProductViewHoler>(options) {
            @NonNull
            @Override
            public ListProductController.ProductViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            protected void onBindViewHolder(@NonNull ListProductController.ProductViewHoler holder, int position, @NonNull Product model) {

            }
        };

    }

    //View Holder class
    public class ProductViewHoler extends RecyclerView.ViewHolder {

        public ProductViewHoler(@NonNull View itemView) {
            super(itemView);
        }

    }
}
