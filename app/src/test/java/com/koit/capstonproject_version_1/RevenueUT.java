package com.koit.capstonproject_version_1;

import com.koit.capstonproject_version_1.Controller.RevenueController;
import com.koit.capstonproject_version_1.Model.Invoice;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RevenueUT {

    private RevenueController revenueController;
    private List<Invoice> invoiceList;
    private Date dateFrom, dateTo;

    @Before
    public void beforeTest() {
        revenueController = new RevenueController();
        invoiceList = new ArrayList<>();
        Invoice i1 = new Invoice("i1", 1000, 0,"31-08-2020");
        Invoice i2 = new Invoice("i2", 2000, 0,"30-08-2020");
        Invoice i3 = new Invoice("i3", 3000, 0,"01-09-2020");
        Invoice i4 = new Invoice("i4", 4000, 0,"02-09-2020");
        Invoice i5 = new Invoice("i5", 5000, 0,"29-08-2020");
        Invoice i6 = new Invoice("i6", 6000, 0,"01-09-2020");

        invoiceList.add(i1);
        invoiceList.add(i2);
        invoiceList.add(i3);
        invoiceList.add(i4);
        invoiceList.add(i5);
        invoiceList.add(i6);

        dateFrom = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFrom);
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        dateFrom = calendar.getTime();

        dateTo = new Date();
        calendar.setTime(dateTo);
        calendar.add(Calendar.DAY_OF_YEAR, +7);
        dateTo = calendar.getTime();
    }

    @Test
    public void getTotalRevenue_inList() {
        assertEquals(21000, revenueController.getTotalRevenue(invoiceList,dateFrom,dateTo,0));
        assertEquals(13000, revenueController.getTotalRevenue(invoiceList,dateFrom,dateTo,1));
        dateFrom = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFrom);
        dateFrom = calendar.getTime();

        dateTo = new Date();
        calendar.setTime(dateTo);
        calendar.add(Calendar.DAY_OF_YEAR, +1);
        dateTo = calendar.getTime();
        assertEquals(4000, revenueController.getTotalRevenue(invoiceList,dateFrom,dateTo,0));
        assertEquals(0, revenueController.getTotalRevenue(invoiceList,dateFrom,dateTo,1));

    }
    @Test
    public void getTotalRevenue_notInList() {
        dateFrom = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFrom);
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        dateFrom = calendar.getTime();
        dateTo = new Date();
        calendar.setTime(dateTo);
        calendar.add(Calendar.DAY_OF_YEAR, -6);
        dateTo = calendar.getTime();
        assertEquals(0, revenueController.getTotalRevenue(invoiceList,dateFrom,dateTo,0));
        assertEquals(0, revenueController.getTotalRevenue(invoiceList,dateFrom,dateTo,1));

    }
}
