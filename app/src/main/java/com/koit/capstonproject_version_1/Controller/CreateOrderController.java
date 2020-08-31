package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koit.capstonproject_version_1.Controller.Interface.IProduct;
import com.koit.capstonproject_version_1.Model.Invoice;
import com.koit.capstonproject_version_1.Model.InvoiceDetail;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.SuggestedProduct;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.dao.UserDAO;
import com.koit.capstonproject_version_1.helper.MoneyEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CreateOrderController {
    private Activity activity;
    private EditProductQuantityController editProductQuantityController;

    public CreateOrderController(Activity activity) {
        this.activity = activity;
        editProductQuantityController = new EditProductQuantityController();
    }

    public long calTotalProductQuantity(List<Product> listSelectedProductInOrder) {
        long totalProductQuantity = 0;
        for (int i = 0; i < listSelectedProductInOrder.size(); i++) {
            for (int j = 0; j < listSelectedProductInOrder.get(i).getUnits().size(); j++) {
                totalProductQuantity += listSelectedProductInOrder.get(i).getUnits().get(j).getUnitQuantity();
            }
        }
        return totalProductQuantity;
    }

    public long calTotalPrice(List<Product> listSelectedProductInOrder) {
        long totalPrice = 0;
        for (int i = 0; i < listSelectedProductInOrder.size(); i++) {
            for (int j = 0; j < listSelectedProductInOrder.get(i).getUnits().size(); j++) {
                totalPrice += listSelectedProductInOrder.get(i).getUnits().get(j).getUnitQuantity() *
                        listSelectedProductInOrder.get(i).getUnits().get(j).getUnitPrice();
            }
        }
        return totalPrice;
    }

    public void inputSaleMoney(final MoneyEditText etSaleMoney, final TextView tvCustomerPaid, final long totalPrice,
                               final MoneyEditText etPaidMoney) {
        etSaleMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String salePriceString = s.toString();
//                long totalSum = 100000; totalPrice
                long salePrice = 0;
                try {
                    salePrice = Money.getInstance().reFormatVN(etSaleMoney.getText().toString());
                } catch (Exception e) {
                    salePrice = 0;
                }
                if (salePriceString.startsWith("00")
                        || salePriceString.equals(""))
                    etSaleMoney.setText(0 + "");
                for (int i = 0; i <= 9; i++) {
                    if (salePriceString.equals("0" + i)) etSaleMoney.setText(i + "");
                }
                if (salePrice == 0 || salePriceString.length() == 1) {
                    etSaleMoney.setSelection(etSaleMoney.getText().length());
                }
                if (totalPrice - salePrice > 0) {
                    tvCustomerPaid.setText(Money.getInstance().formatVN(totalPrice - salePrice));
                    etPaidMoney.setText(Money.getInstance().formatVN(totalPrice - salePrice));
                } else {
                    tvCustomerPaid.setText("0");
                    etPaidMoney.setText("0");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void inputPaidMoney(final MoneyEditText etPaidMoney, final TextView tvCustomerPaid,
                               final TextView tvMoneyChange, final TextView tvCustomerDebit, final Button btnSubmitPaid) {
        etPaidMoney.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String paidMoneyString = s.toString();
                long customerPaid = 0;
                try {
                    customerPaid = Money.getInstance().reFormatVN(paidMoneyString);
//                            Long.parseLong(paidMoneyString);
                } catch (Exception e) {
                    customerPaid = 0;
                }
                if (paidMoneyString.startsWith("00")
                        || paidMoneyString.equals(""))
                    etPaidMoney.setText(0 + "");
                for (int i = 0; i <= 9; i++) {
                    if (paidMoneyString.equals("0" + i)) etPaidMoney.setText(i + "");
                }
                if (customerPaid == 0 || paidMoneyString.length() == 1) {
                    etPaidMoney.setSelection(etPaidMoney.getText().length());
                }
                long changeMoney = customerPaid - Money.getInstance().reFormatVN(tvCustomerPaid.getText().toString());
                if (changeMoney >= 0) {
                    tvMoneyChange.setText(Money.getInstance().formatVN(changeMoney));
                    tvCustomerDebit.setText("0");
                    btnSubmitPaid.setText("Hoàn Thành");
                } else {
                    tvCustomerDebit.setText(Money.getInstance().formatVN(-changeMoney));
                    tvMoneyChange.setText("0");
                    btnSubmitPaid.setText("Tiếp tục");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void addInvoiceToFirebase(Invoice invoice) {
//        Invoice invoice = new Invoice(invoiceId, "KH000000", invoiceDate, invoiceTime, "",
//                debitAmount, discount, firstPaid, totalPrice, false);
        invoice.addInvoiceToFirebase(invoice);
    }

    public void insertDraftOrder(List<Product> listSelectedProductInOrder) {
        listSelectedProductInOrder = formatListProductInOrder(listSelectedProductInOrder);
        String invoiceId = RandomStringController.getInstance().randomInvoiceId();
        long debitAmount = 0;
        long discount = 0;
        long firstPaid = 0;
        String invoiceDate = TimeController.getInstance().getCurrentDate();
        String invoiceTime = TimeController.getInstance().getCurrentTime();
        boolean isDrafted = true;
        long totalPrice = calTotalPrice(listSelectedProductInOrder);
        Invoice invoice = new Invoice(invoiceId, "", invoiceDate, invoiceTime, "", debitAmount, discount,
                firstPaid, totalPrice, isDrafted);
        InvoiceDetail invoiceDetail = new InvoiceDetail(invoiceId, listSelectedProductInOrder);
        addInvoiceToFirebase(invoice);
        addInvoiceDetailToFirebase(invoiceDetail);
    }

    public void addInvoiceDetailToFirebase(InvoiceDetail invoiceDetail) {
        //   InvoiceDetail invoiceDetail = new InvoiceDetail(invoiceId,listSelectedProductInOrder);
        invoiceDetail.addInvoiceDetailToFirebase(invoiceDetail);
    }

    public List<Product> formatListProductInOrder(List<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            Product proI = products.get(i);
            for (int j = 0; j < i; j++) {
                Product proJ = products.get(j);
                if (proI.getProductId().equals(proJ.getProductId())) {
                    proJ.addUnit(proI.getUnits().get(0));
                    products.remove(i);
                    i--;
                    break;
                }
            }
        }
        return products;
    }

    public List<Product> formatListProductWarehouse(List<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            Product proI = products.get(i);
            for (int j = 0; j < i; j++) {
                Product proJ = products.get(j);
                if (proI.getProductId().equals(proJ.getProductId())) {
                    products.remove(i);
                    i--;
                    break;
                }
            }
        }
        return products;
    }

    public void updateUnitQuantity(List<Product> listSelectedProductInOrder, List<Product> listProductWarehouse) {

        for (int i = 0; i < listProductWarehouse.size(); i++) {
            if (!listProductWarehouse.get(i).getProductId().startsWith("nonListedProduct")) {
//                int finalI = i;
                final Product productInOrder = listSelectedProductInOrder.get(i);
                IProduct iProduct = new IProduct() {
                    @Override
                    public void getSuggestedProduct(SuggestedProduct product) {

                    }

                    @Override
                    public void getProductById(Product product) {
                        if (product != null) {
                            Log.d("ktLuong", "check");
                            List<Unit> unitInWareHouse = product.getUnits();
                            sortUnitByPrice(unitInWareHouse);
                            // cal total warehouse product by smallest unit
                            long totalQuantityUnitInWarehouse = unitInWareHouse.get(unitInWareHouse.size() - 1).getUnitQuantity();
                            List<Unit> unitSelectedProductInOrder = productInOrder.getUnits();
                            // set convert rate to selected product;
                            // because list select product had convert = 0;
                            for (int j = 0; j < unitSelectedProductInOrder.size(); j++) {
                                Log.d("unitSelected", unitSelectedProductInOrder.get(j).toString());
                                for (int k = 0; k < unitInWareHouse.size(); k++) {
                                    if (unitSelectedProductInOrder.get(j).getUnitId()
                                            .equals(unitInWareHouse.get(k).getUnitId())) {
                                        unitSelectedProductInOrder.get(j).setConvertRate(unitInWareHouse.get(k).getConvertRate());
                                    }
                                }
                            }
                            for (int j = 0; j < unitInWareHouse.size(); j++) {
                                Log.d("unitInWareHouse", unitInWareHouse.get(j).toString());
                            }
                            for (int j = 0; j < unitSelectedProductInOrder.size(); j++) {
                                Log.d("unitSelected", unitSelectedProductInOrder.get(j).toString());
                            }
                            // cal total selected product by smallest unit
                            long totalQuantityUnitSelected = calTotalQuantityInUnitList(unitSelectedProductInOrder);
                            Log.d("QuantityUnitInWarehouse", totalQuantityUnitInWarehouse + "");
                            Log.d("QuantityUnitSelected", totalQuantityUnitSelected + "");
                            // cal remaining  product by smallest unit
                            totalQuantityUnitInWarehouse = (totalQuantityUnitInWarehouse - totalQuantityUnitSelected > 0) ?
                                    totalQuantityUnitInWarehouse - totalQuantityUnitSelected : 0;
                            for (int j = 0; j < unitInWareHouse.size(); j++) {
                                unitInWareHouse.get(j).setUnitQuantity(totalQuantityUnitInWarehouse / unitInWareHouse.get(j).getConvertRate());
                            }
                            for (int j = 0; j < unitInWareHouse.size(); j++) {
                                Log.d("unitInWareHouse", unitInWareHouse.get(j).toString());
                            }
                            // set remaining product to product
                            product.setUnits(unitInWareHouse);

                            // update unit to firebase
                            Unit.getInstance().updateUnitsToFirebase(product);
                        }
                    }
                };
                getProductByProductId(productInOrder.getProductId(), iProduct);


                /*List<Unit> unitInWareHouse = listProductWarehouse.get(i).getUnits();
                sortUnitByPrice(unitInWareHouse);

                long totalQuantityUnitInWarehouse = unitInWareHouse.get(unitInWareHouse.size() - 1).getUnitQuantity();
                List<Unit> unitSelectedProductInOrder = listSelectedProductInOrder.get(i).getUnits();
                for (int j = 0; j < unitSelectedProductInOrder.size(); j++) {
                    Log.d("unitSelected", unitSelectedProductInOrder.get(j).toString());
                    for (int k = 0; k < unitInWareHouse.size(); k++) {
                        if (unitSelectedProductInOrder.get(j).getUnitId()
                                .equals(unitInWareHouse.get(k).getUnitId())) {
                            unitSelectedProductInOrder.get(j).setConvertRate(unitInWareHouse.get(k).getConvertRate());
                        }
                    }
                }
                for (int j = 0; j < unitInWareHouse.size(); j++) {
                    Log.d("unitInWareHouse", unitInWareHouse.get(j).toString());
                }
                for (int j = 0; j < unitSelectedProductInOrder.size(); j++) {
                    Log.d("unitSelected", unitSelectedProductInOrder.get(j).toString());
                }
                long totalQuantityUnitSelected = calTotalQuantityInUnitList(unitSelectedProductInOrder);
                Log.d("QuantityUnitInWarehouse", totalQuantityUnitInWarehouse + "");
                Log.d("QuantityUnitSelected", totalQuantityUnitSelected + "");
                totalQuantityUnitInWarehouse = (totalQuantityUnitInWarehouse - totalQuantityUnitSelected > 0) ?
                        totalQuantityUnitInWarehouse - totalQuantityUnitSelected : 0;
                for (int j = 0; j < unitInWareHouse.size(); j++) {
                    unitInWareHouse.get(j).setUnitQuantity(totalQuantityUnitInWarehouse / unitInWareHouse.get(j).getConvertRate());
                }
                for (int j = 0; j < unitInWareHouse.size(); j++) {
                    Log.d("unitInWareHouse", unitInWareHouse.get(j).toString());
                }

                listProductWarehouse.get(i).setUnits(unitInWareHouse);
//                editProductQuantityController.addUnitsToFireBase(listProductWarehouse.get(i), listProductWarehouse.get(i).getUnits());
                updateUnitsToFireBase(listProductWarehouse.get(i));*/

            }

        }

    }

    public void updateUnitsToFireBase(Product product) {
        final Product productInWarehouse = product;
        IProduct iProduct = new IProduct() {
            @Override
            public void getSuggestedProduct(SuggestedProduct product) {

            }

            @Override
            public void getProductById(Product product) {
                if (product != null && product.isActive()) {
                    Unit unit = new Unit();
                    unit.updateUnitsToFirebase(productInWarehouse);
                }
            }
        };
        product.getProductById(product.getProductId(), iProduct);

    }

    public long calTotalQuantityInUnitList(List<Unit> units) {
        long quantity = 0;
        for (int i = 0; i < units.size(); i++) {
            quantity += units.get(i).getUnitQuantity() * units.get(i).getConvertRate();
        }
        return quantity;
    }

    public void sortUnitByPrice(List<Unit> unitList) {
        Collections.sort(unitList, new Comparator<Unit>() {
            @Override
            public int compare(Unit o1, Unit o2) {
                return (int) (o2.getUnitPrice() - o1.getUnitPrice());
            }
        });
    }

    public void getProductByProductId(final String id, final IProduct iProduct) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getProductByProductId(dataSnapshot, id, iProduct);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        DatabaseReference nodeRoot = FirebaseDatabase.getInstance().getReference();
        nodeRoot.keepSynced(true);
        nodeRoot.addListenerForSingleValueEvent(valueEventListener);

    }

    private void getProductByProductId(DataSnapshot snapshot, final String id, final IProduct iProduct) {
        DataSnapshot dataSnapshotProduct = snapshot.child("Products").child(UserDAO.getInstance().getUserID()).child(id);
        Product product = dataSnapshotProduct.getValue(Product.class);
        product.setProductId(dataSnapshotProduct.getKey());

        DataSnapshot dataSnapshotUnit = snapshot.child("Units").child(UserDAO.getInstance().getUserID()).child(id);
        List<Unit> unitList = new ArrayList<>();
        for (DataSnapshot valueUnit : dataSnapshotUnit.getChildren()) {
//                    Log.d("kiemtraUnit", valueUnit + "");
            Unit unit = valueUnit.getValue(Unit.class);
            unit.setUnitId(valueUnit.getKey());
            unitList.add(unit);
        }
        product.setUnits(unitList);
        iProduct.getProductById(product);

    }

}
