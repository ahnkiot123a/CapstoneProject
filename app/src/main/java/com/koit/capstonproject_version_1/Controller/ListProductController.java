package com.koit.capstonproject_version_1.Controller;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.koit.capstonproject_version_1.Adapter.ItemAdapter;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.View.ListProductActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListProductController {
    private ListProductActivity listProductActivity;
    private DatabaseReference myRef;
    private ArrayList<Product> list;
    private RecyclerView recyclerView;
    public ListProductController() {
    }

    public ListProductController(ListProductActivity listProductActivity) {
        this.listProductActivity = listProductActivity;
    }

    public void firebaseProductSearch(final RecyclerView recyclerViewListProduct, String searchText) {
        Product product = new Product();
        myRef = product.getMyRef();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    list = new ArrayList<>();
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        list.add(ds.getValue(Product.class));
                    }
                    ItemAdapter adapter = new ItemAdapter(list);
                    recyclerViewListProduct.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
