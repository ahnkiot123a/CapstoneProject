package com.koit.capstonproject_version_1.Controller;

import android.util.Log;

import com.koit.capstonproject_version_1.Model.Invoice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

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
                Log.d("sdate1", sdate1);
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

}
