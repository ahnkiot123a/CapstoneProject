package com.koit.capstonproject_version_1.controller;

import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.adapter.ItemInDetailRevenueAdapter;
import com.koit.capstonproject_version_1.model.Invoice;
import com.koit.capstonproject_version_1.helper.Money;
import com.koit.capstonproject_version_1.view.DetailReveneuActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailRevenueController {
    RecyclerView recyclerViewListInvoice;
    DetailReveneuActivity detailReveneuActivity;
    List<Invoice> listAllInvoice;
    ItemInDetailRevenueAdapter adapter;
    List<Map<String, Long>> listMap;

    public DetailRevenueController(RecyclerView recyclerViewListInvoice, DetailReveneuActivity detailReveneuActivity, List<Invoice> listAllInvoice) {
        this.recyclerViewListInvoice = recyclerViewListInvoice;
        this.detailReveneuActivity = detailReveneuActivity;
        this.listAllInvoice = listAllInvoice;

    }

    //isSearchByDay = 0: is true
    public void getListInvoice(Date dateFrom, Date dateTo, int isSearchByDay, TextView tvTotal) {
        listMap = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(detailReveneuActivity);
        recyclerViewListInvoice.setLayoutManager(layoutManager);
        adapter = new ItemInDetailRevenueAdapter(changeToListMap(listAllInvoice, dateFrom, dateTo, isSearchByDay));
        recyclerViewListInvoice.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        tvTotal.setText(Money.getInstance().formatVN(getTotalRevenue(listMap, dateFrom, dateTo, isSearchByDay)));
    }

    private List<Map<String, Long>> changeToListMap(List<Invoice> listAllInvoice, Date dateFrom,
                                                    Date dateTo, int isSearchByDay) {
        long y;
        //searh by day
        if (isSearchByDay == 0) {
            for (int i = TimeController.getInstance().getDateInYear(dateTo) + 1;
                 i > TimeController.getInstance().getDateInYear(dateFrom); i--) {
                String stringDateTo = TimeController.getInstance().changeDateToString(dateTo);
                y = 0;
                for (Invoice iv : listAllInvoice) {
                    if (stringDateTo.equals(iv.getInvoiceDate()))
                        y += iv.getTotal();
                }
                //add item to list
                Map<String, Long> map = new HashMap<>();
                map.put(TimeController.getInstance().changeDateToString(dateTo), y);
                listMap.add(map);
                //minus 1 day to dateTo
                Calendar c = Calendar.getInstance();
                c.setTime(dateTo);
                c.add(Calendar.DATE, -1);
                dateTo = c.getTime();
            }
        } else {
            //search by month
            //month between 2 months
            for (int i = TimeController.getInstance().getMonthFrom2020(dateTo) + 1; i > TimeController.getInstance().getMonthFrom2020(dateFrom); i--) {
                y = 0;
                for (Invoice iv : listAllInvoice) {
                    if (dateTo.equals(TimeController.getInstance().changeStringDayToFirstDayOfMonth(iv.getInvoiceDate())))
                        y += iv.getTotal();
                }
                //add item to list
                Map<String, Long> map = new HashMap<>();
                map.put(TimeController.getInstance().changeDateToMonthString(dateTo), y);
                listMap.add(map);
                //minus 1 month to dateTo
                Calendar c = Calendar.getInstance();
                c.setTime(dateTo);
                c.add(Calendar.MONTH, -1);
                dateTo = c.getTime();
            }

        }
        return listMap;
    }

    //get Total Revenue betweeen 2 dates
    private long getTotalRevenue(List<Map<String, Long>> listInvoice, Date dateFrom, Date dateTo, int searchByType) {
        long total = 0;
        for (Map<String, Long> map : listInvoice
        ) {
            Date dateInList;
            String sDateSearch = null;
            long totalSearch = 0;
            for (Map.Entry<String, Long> entry : map.entrySet()) {
                sDateSearch = entry.getKey();
                totalSearch = entry.getValue();
            }
            //search by day
            if (searchByType == 0) {
                dateInList = TimeController.getInstance().changeStringDayToDate(sDateSearch);
            } else {
                dateInList = TimeController.getInstance().changeStringMonthToFirstDayOfMonth(sDateSearch);
            }
            if ((dateFrom.before(dateInList) || (dateFrom.equals(dateInList))) &&
                    (dateTo.after(dateInList) || dateTo.equals(dateInList))) {
                total += totalSearch;
            }
        }
        return total;

    }
}
