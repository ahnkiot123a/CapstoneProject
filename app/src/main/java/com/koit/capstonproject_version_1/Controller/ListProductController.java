package com.koit.capstonproject_version_1.Controller;

import android.content.Context;
import android.util.Log;

import com.koit.capstonproject_version_1.Adapter.ItemAdapter;
import com.koit.capstonproject_version_1.Controller.Interface.ListProductInterface;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.R;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListProductController {
    //    private ListProductActivity listProductActivity;
//    private DatabaseReference myRef;
//    private ArrayList<Product> list;
    private Context context;
    private Product product;
    ItemAdapter itemAdapter;
    private int item_count;

    public ListProductController(Context context) {
        this.context = context;
        product = new Product();
    }

    public void getListProduct(Context context, RecyclerView recyclerViewListProduct) {
        final List<Product> listProduct = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerViewListProduct.setLayoutManager(layoutManager);
        itemAdapter = new ItemAdapter(context, listProduct, R.layout.item_layout);
        recyclerViewListProduct.setAdapter(itemAdapter);
        ListProductInterface listProductInterface = new ListProductInterface() {
            @Override
            public void getListProductModel(Product product) {
                Log.d("kiemtra", product.getProductName() + "");
                listProduct.add(product);
                itemAdapter.notifyDataSetChanged();
            }
        };
        product.getListProduct(listProductInterface);
        item_count = recyclerViewListProduct.getAdapter().getItemCount();
        Log.d("kiemtra1", item_count + "");
    }

    public int getItemInRecyclerView(RecyclerView recyclerView) {
        item_count = recyclerView.getAdapter().getItemCount();
        Log.d("kiemtra1", item_count + "");
        return item_count;
    }
//    public void firebaseProductSearch(final RecyclerView recyclerViewListProduct, String searchText) {
//        Product product = new Product();
//        myRef = product.getMyRef();
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    list = new ArrayList<>();
//                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                        list.add(ds.getValue(Product.class));
//                    }
//                    ItemAdapter adapter = new ItemAdapter(list);
//                    recyclerViewListProduct.setAdapter(adapter);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }


}
