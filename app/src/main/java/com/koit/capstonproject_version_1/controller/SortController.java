package com.koit.capstonproject_version_1.controller;

import com.koit.capstonproject_version_1.model.DebtPayment;
import com.koit.capstonproject_version_1.model.Debtor;
import com.koit.capstonproject_version_1.model.Invoice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class SortController {

    private static SortController mInstance;

    public SortController() {
    }

    public static SortController getInstance() {
        if (mInstance == null) mInstance = new SortController();
        return mInstance;
    }

    public void sortInvoiceListByDate(ArrayList<Invoice> list) {
        Collections.sort(list, new Comparator<Invoice>() {
            @Override
            public int compare(Invoice o1, Invoice o2) {
                String sdate1 = o1.getInvoiceDate() + " " + o1.getInvoiceTime();
//                Log.d("sdate1", sdate1);
                String sdate2 = o2.getInvoiceDate() + " " + o2.getInvoiceTime();
                Date date1 = null, date2 = null;
                try {
                    date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(sdate1);
                    date2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(sdate2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (date1 != null && date2 != null) {
                    if (date1.before(date2)) return 1;
                }
                return -1;
            }
        });
    }

    public void sortDebtorListByDebitAmount(List<Debtor> list) {
        Collections.sort(list, new Comparator<Debtor>() {
            @Override
            public int compare(Debtor o1, Debtor o2) {
             return (int) (o2.getRemainingDebit() - o1.getRemainingDebit());
            }
        });
    }

    public void sortDebtPaymentListByDate(List<DebtPayment> list) {
        Collections.sort(list, new Comparator<DebtPayment>() {
            @Override
            public int compare(DebtPayment o1, DebtPayment o2) {
                String sdate1 = o1.getPayDate() + " " + o1.getPayTime();
//                Log.d("sdate1", sdate1);
                String sdate2 = o2.getPayDate() + " " + o2.getPayTime();
                Date date1 = null, date2 = null;
                try {
                    date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(sdate1);
                    date2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(sdate2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (date1 != null && date2 != null) {
                    if (date1.before(date2)) return 1;
                }
                return -1;
            }
        });
    }

}
