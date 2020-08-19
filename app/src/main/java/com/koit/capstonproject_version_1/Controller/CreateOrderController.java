package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.Controller.Interface.IProduct;
import com.koit.capstonproject_version_1.Model.Invoice;
import com.koit.capstonproject_version_1.Model.InvoiceDetail;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.SuggestedProduct;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.dao.UserDAO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CreateOrderController {
    private Activity activity;
    private AddProductQuantityController addProductQuantityController;

    public CreateOrderController(Activity activity) {
        this.activity = activity;
        addProductQuantityController = new AddProductQuantityController();
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

    public void inputSaleMoney(final TextInputEditText etSaleMoney, final TextView tvCustomerPaid, final long totalPrice,
                               final TextInputEditText etPaidMoney) {
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
                    salePrice = Long.parseLong(salePriceString);
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
                    etPaidMoney.setText(totalPrice - salePrice + "");
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

    public void inputPaidMoney(final TextInputEditText etPaidMoney, final TextView tvCustomerPaid,
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
                    customerPaid = Long.parseLong(paidMoneyString);
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
                List<Unit> unitInWareHouse = listProductWarehouse.get(i).getUnits();
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
//                addProductQuantityController.addUnitsToFireBase(listProductWarehouse.get(i), listProductWarehouse.get(i).getUnits());
                updateUnitsToFireBase(listProductWarehouse.get(i));
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
            public void isExistBarcode(boolean existed) {

            }

            @Override
            public void getProductById(Product product) {
                if (product != null && product.isActive()){
                    Unit unit = new Unit();
                    unit.updateUnitsToFirebase(productInWarehouse);
                }
            }
        };
        product.getProductById(product.getProductId(),iProduct);

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

}
