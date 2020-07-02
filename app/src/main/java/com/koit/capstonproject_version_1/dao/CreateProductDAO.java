package com.koit.capstonproject_version_1.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koit.capstonproject_version_1.Controller.Interface.IProduct;
import com.koit.capstonproject_version_1.Model.SuggestedProduct;

public class CreateProductDAO {
    private static CreateProductDAO mInstance;
    private DatabaseReference databaseReference;

    public CreateProductDAO() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public static CreateProductDAO getInstance() {
        if (mInstance == null) {
            mInstance = new CreateProductDAO();
        }
        return mInstance;
    }

    public void getSuggestedProduct(final String barcode, final IProduct iProduct) {
        databaseReference.child("SuggestedProducts").child(barcode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SuggestedProduct product = dataSnapshot.getValue(SuggestedProduct.class);
                iProduct.getSuggestedProduct(product);
                if(product != null){
                    Log.i("suggestedProduct", product.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", "Failed to read value.", databaseError.toException());
            }
        });
    }

}
