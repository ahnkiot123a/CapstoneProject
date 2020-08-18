package com.koit.capstonproject_version_1.dao;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koit.capstonproject_version_1.Controller.Interface.IDebtor;
import com.koit.capstonproject_version_1.Controller.Interface.IInvoice;
import com.koit.capstonproject_version_1.Model.Debtor;
import com.koit.capstonproject_version_1.Model.Invoice;

public class InvoiceHistoryDAO {

    private DatabaseReference nodeRoot;

    private String debtorName = "";

    public InvoiceHistoryDAO() {

    }

    public void getInvoiceList(final IInvoice iInvoice, final RecyclerView recyclerViewListProduct, final ConstraintLayout layoutNotFound) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getInvoiceList(dataSnapshot, iInvoice, recyclerViewListProduct, layoutNotFound);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        nodeRoot = FirebaseDatabase.getInstance().getReference();
        nodeRoot.keepSynced(true);
        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
    }

    private void getInvoiceList(DataSnapshot dataSnapshot, IInvoice iInvoice, RecyclerView recyclerViewListProduct, ConstraintLayout layoutNotFound) {
        DataSnapshot data = dataSnapshot.child("Invoices").child(UserDAO.getInstance().getUserID());
        boolean hasInvoice = false;
        for (DataSnapshot value : data.getChildren()) {
            final Invoice invoice = value.getValue(Invoice.class);
            if (invoice != null) {
                hasInvoice = true;
                invoice.setInvoiceId(value.getKey());
                if (invoice.getDebtorId().isEmpty()) {
                    invoice.setDebtorName("Khách lẻ");
                } else {
                    getDebtorName(invoice.getDebtorId());
                    IDebtor iDebtor = new IDebtor() {
                        @Override
                        public void getDebtor(Debtor debtor) {
                            if (debtor != null) {
                                invoice.setDebtorName(debtor.getFullName());
                            }
                        }
                    };
                    getDebtorById(invoice.getDebtorId(), iDebtor);

                }
                Log.i("invoice", invoice.toString());
                iInvoice.getInvoice(invoice);
            }

        }
        if (!hasInvoice) {
            recyclerViewListProduct.setVisibility(View.GONE);
            layoutNotFound.setVisibility(View.VISIBLE);
        }
    }

    //set debtor name in invoice
    public void getDebtorName(String id) {
        IDebtor iDebtor = new IDebtor() {
            @Override
            public void getDebtor(Debtor debtor) {
                if (debtor != null) {
                    debtorName = debtor.getFullName();
                }
            }
        };
        getDebtorById(id, iDebtor);
    }


    public void getDebtorById(String id, final IDebtor iDebtor) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Debtors").child(UserDAO.getInstance().getUserID()).child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Debtor debtor = snapshot.getValue(Debtor.class);
                debtor.setDebtorId(snapshot.getKey());
                iDebtor.getDebtor(debtor);
                if (debtor != null) {
                    Log.d("debtor", debtor.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());

            }
        });
    }

    public void getInvoiceById(String id, final IInvoice iInvoice) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Invoices").child(UserDAO.getInstance().getUserID()).child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Invoice invoice = snapshot.getValue(Invoice.class);
                invoice.setInvoiceId(snapshot.getKey());
                iInvoice.getInvoice(invoice);
                if (invoice != null) {
                    Log.d("invoice", invoice.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    public void deleteDraftOrder(String oid) {
        DatabaseReference invoiceRef = FirebaseDatabase.getInstance().getReference();
        invoiceRef = invoiceRef.child("Invoices").child(UserDAO.getInstance().getUserID()).child(oid);
        invoiceRef.removeValue();

        DatabaseReference invoiceDetailRef = FirebaseDatabase.getInstance().getReference();
        invoiceDetailRef = invoiceDetailRef.child("InvoiceDetail").child(UserDAO.getInstance().getUserID()).child(oid);
        invoiceDetailRef.removeValue();

    }

//    public void getProductList(String oid){
//            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//            reference.child("InvoiceDetail").child(UserDAO.getInstance().getUserID()).child(oid).child("products");
//            reference.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    GenericTypeIndicator<ArrayList<Product>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<Product>>() {};
//                    ArrayList<Product> pList = snapshot.getValue(genericTypeIndicator);
//                    for (Product p : pList){
//                        Log.d("productInvoice", p.toString());
//                    }
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//    }


}
