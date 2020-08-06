package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.Model.Invoice;
import com.koit.capstonproject_version_1.Model.InvoiceDetail;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.UIModel.Money;

import java.util.List;

public class PaymentController {
    private Activity activity;

    public PaymentController(Activity activity) {
        this.activity = activity;
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

    public void addInvoiceDetailToFirebase(InvoiceDetail invoiceDetail) {
     //   InvoiceDetail invoiceDetail = new InvoiceDetail(invoiceId,listSelectedProductInOrder);
        invoiceDetail.addInvoiceDetailToFirebase(invoiceDetail);
    }
}
