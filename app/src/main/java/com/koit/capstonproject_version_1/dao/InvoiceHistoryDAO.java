package com.koit.capstonproject_version_1.dao;

import android.util.Log;

import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koit.capstonproject_version_1.Controller.Interface.IInvoice;
import com.koit.capstonproject_version_1.Model.Invoice;

public class InvoiceHistoryDAO {

    private DatabaseReference nodeRoot;

    public InvoiceHistoryDAO() {

    }

    public void getInvoiceList(final IInvoice iInvoice) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getInvoiceList(dataSnapshot, iInvoice);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        nodeRoot = FirebaseDatabase.getInstance().getReference();
        nodeRoot.keepSynced(true);
        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
    }

    private void getInvoiceList(DataSnapshot dataSnapshot, IInvoice iInvoice) {
        DataSnapshot data = dataSnapshot.child("Invoices").child(UserDAO.getInstance().getUserID());
        for (DataSnapshot value : data.getChildren()) {
            Invoice invoice = value.getValue(Invoice.class);
            if(invoice != null){
                Log.i("invoice", invoice.toString());
                invoice.setInvoiceId(value.getKey());
                iInvoice.getInvoice(invoice);
            }

        }
    }


}
