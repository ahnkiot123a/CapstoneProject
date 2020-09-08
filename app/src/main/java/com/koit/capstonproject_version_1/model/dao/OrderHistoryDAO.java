package com.koit.capstonproject_version_1.model.dao;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koit.capstonproject_version_1.controller.Interface.IDebtor;
import com.koit.capstonproject_version_1.controller.Interface.IInvoice;
import com.koit.capstonproject_version_1.controller.TimeController;
import com.koit.capstonproject_version_1.model.Debtor;
import com.koit.capstonproject_version_1.model.Invoice;

import java.util.Date;

public class OrderHistoryDAO {

    private DatabaseReference nodeRoot;

    private String debtorName = "";

    public OrderHistoryDAO() {

    }

    public void getInvoiceList(final IInvoice iInvoice, final RecyclerView recyclerViewListProduct,
                               final ConstraintLayout layoutNotFound, LinearLayout layoutOrder) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getInvoiceList(dataSnapshot, iInvoice, recyclerViewListProduct, layoutNotFound, layoutOrder);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        nodeRoot = FirebaseDatabase.getInstance().getReference();
        nodeRoot.keepSynced(true);
        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
    }



    private void getInvoiceList(DataSnapshot dataSnapshot, IInvoice iInvoice, RecyclerView recyclerViewListProduct,
                                ConstraintLayout layoutNotFound, LinearLayout layoutOrder) {
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
            layoutOrder.setVisibility(View.GONE);
            layoutNotFound.setVisibility(View.VISIBLE);
        }else{
            layoutOrder.setVisibility(View.VISIBLE);
            layoutNotFound.setVisibility(View.GONE);
        }
    }

    //For revenue controller
    public void getInvoiceList(final IInvoice iInvoice, final LinearLayout layoutNotFound,
                               final LottieAnimationView animationView, final LinearLayout layoutChart
                              ) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getInvoiceList(dataSnapshot, iInvoice, layoutNotFound, animationView, layoutChart);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        nodeRoot = FirebaseDatabase.getInstance().getReference();
        nodeRoot.keepSynced(true);
        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
    }
    //for layout chart
    private void getInvoiceList(DataSnapshot dataSnapshot, IInvoice iInvoice, LinearLayout layoutNotFound,
                                LottieAnimationView animationView, LinearLayout layoutChart) {
        boolean hasInvoice = false;
        DataSnapshot data = dataSnapshot.child("Invoices").child(UserDAO.getInstance().getUserID());
        for (DataSnapshot value : data.getChildren()) {
            hasInvoice = true;
            final Invoice invoice = value.getValue(Invoice.class);
            if (invoice != null) {
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
            layoutNotFound.setVisibility(View.VISIBLE);
            animationView.setVisibility(View.GONE);
            layoutChart.setVisibility(View.GONE);
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


    public void deleteDraftOrder(String oid) {
        DatabaseReference invoiceRef = FirebaseDatabase.getInstance().getReference();
        invoiceRef = invoiceRef.child("Invoices").child(UserDAO.getInstance().getUserID()).child(oid);
        invoiceRef.removeValue();

        DatabaseReference invoiceDetailRef = FirebaseDatabase.getInstance().getReference();
        invoiceDetailRef = invoiceDetailRef.child("InvoiceDetail").child(UserDAO.getInstance().getUserID()).child(oid);
        invoiceDetailRef.removeValue();
    }



    public void getDraftOrderList(final IInvoice iInvoice) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getDraftOrderList(dataSnapshot, iInvoice);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        nodeRoot = FirebaseDatabase.getInstance().getReference();
        nodeRoot.keepSynced(true);
        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
    }

    private void getDraftOrderList(DataSnapshot dataSnapshot, IInvoice iInvoice) {
        DataSnapshot data = dataSnapshot.child("Invoices").child(UserDAO.getInstance().getUserID());
        for (DataSnapshot value : data.getChildren()) {
            final Invoice invoice = value.getValue(Invoice.class);
            if (invoice != null && invoice.isDrafted()) {
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
    }

    public void deleteDraftOrderBefore3Days() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                deleteDraftOrderBefore3Days(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        nodeRoot = FirebaseDatabase.getInstance().getReference();
        nodeRoot.keepSynced(true);
        nodeRoot.addListenerForSingleValueEvent(valueEventListener);

    }

    private void deleteDraftOrderBefore3Days(DataSnapshot dataSnapshot) {
        DataSnapshot data = dataSnapshot.child("Invoices").child(UserDAO.getInstance().getUserID());
        for (DataSnapshot value : data.getChildren()) {
            final Invoice invoice = value.getValue(Invoice.class);
            if (invoice != null && invoice.isDrafted()) {
                Date invoiceDate = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                Date threeDate = TimeController.getInstance().convertStrToDate(TimeController.getInstance().plusDate(-3));
                if (invoiceDate.before(threeDate)) {
                    FirebaseDatabase.getInstance().getReference().child("Invoices").child(UserDAO.getInstance()
                            .getUserID()).child(value.getKey()).removeValue();
                }
            }

        }
    }


    public void getDebitInvoiceList(final IInvoice iInvoice) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getDebitInvoiceList(dataSnapshot, iInvoice);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        nodeRoot = FirebaseDatabase.getInstance().getReference();
        nodeRoot.keepSynced(true);
        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
    }

    private void getDebitInvoiceList(DataSnapshot dataSnapshot, IInvoice iInvoice) {
        DataSnapshot data = dataSnapshot.child("Invoices").child(UserDAO.getInstance().getUserID());
        for (DataSnapshot value : data.getChildren()) {
            final Invoice invoice = value.getValue(Invoice.class);
            if (invoice != null && !invoice.isDrafted() && invoice.getDebitAmount() > 0) {
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
//                Log.i("invoice", invoice.toString());
                iInvoice.getInvoice(invoice);
            }

        }
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
