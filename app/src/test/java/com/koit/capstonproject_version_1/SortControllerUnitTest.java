package com.koit.capstonproject_version_1;

import com.koit.capstonproject_version_1.controller.SortController;
import com.koit.capstonproject_version_1.model.DebtPayment;
import com.koit.capstonproject_version_1.model.Debtor;
import com.koit.capstonproject_version_1.model.Invoice;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SortControllerUnitTest {
    ArrayList<Invoice> invoices = new ArrayList<>();
    List<Debtor> debtors = new ArrayList<>();
    List<DebtPayment> debtPayments = new ArrayList<>();
    SortController sortController = new SortController();

    @Before
    public void beforeTest() {

    }

    @Test
    public void sortInvoiceListByDate() {
        Invoice invoice1 = new Invoice("200831093526373", "KH20200825160857", "31-08-2020",
                "09:35:26", "", 273000, 0, 30000, 303000, false);
        Invoice invoice2 = new Invoice("200831093604163", "KH20200731102932", "31-08-2020",
                "09:36:04", "", 171000, 0, 0, 171000, false);
        Invoice invoice3 = new Invoice("200831093615577", "KH20200804100001", "31-08-2020",
                "09:36:15", "", 1156000, 0, 0, 1156000, false);
        Invoice invoice4 = new Invoice("200831093806843", "KH20200818195114", "31-08-2020",
                "09:38:06", "", 63000, 0, 0, 63000, false);
        Invoice invoice5 = new Invoice("200831093840715", "KH20200818195114", "31-08-2020",
                "09:38:40", "", 7000, 0, 0, 7000, false);
        invoices.add(invoice1);
        invoices.add(invoice2);
        invoices.add(invoice3);
        invoices.add(invoice4);
        invoices.add(invoice5);
        sortController.sortInvoiceListByDate(invoices);
        ArrayList<Invoice> expectedInvoices = new ArrayList<>();
        expectedInvoices.add(invoice5);
        expectedInvoices.add(invoice4);
        expectedInvoices.add(invoice3);
        expectedInvoices.add(invoice2);
        expectedInvoices.add(invoice1);
        assertEquals(invoices, expectedInvoices);
    }

    @Test
    public void sortDebtorListByDebitAmount_isCorrect() {
        Debtor debtor1 = new Debtor("KH20200731102932", "Que vo", "24-11-1996", "quannd@fpt.edu.vn",
                "Nguyen Dang Quan", "0395106907", false, 317000);
        Debtor debtor2 = new Debtor("KH20200804100001", "Que vo", "24-11-1996", "quannd@fpt.edu.vn",
                "Nguyen Duc Luat", "0395106907", false, 1258000);
        Debtor debtor3 = new Debtor("KH20200805155201", "Que vo", "24-11-1996", "quannd@fpt.edu.vn",
                "Nguyen Quang Anh", "0395106907", false, 832000);
        Debtor debtor4 = new Debtor("KH20200812143456", "Que vo", "24-11-1996", "quannd@fpt.edu.vn",
                "Vũ quyết thắng", "0395106907", false, 1016000);
        Debtor debtor5 = new Debtor("KH20200818195114", "Que vo", "24-11-1996", "quannd@fpt.edu.vn",
                "ha quy anh", "0395106907", false, 467000);
        Debtor debtor6 = new Debtor("KH20200825160857", "Que vo", "24-11-1996", "quannd@fpt.edu.vn",
                "Vũ Thị Thanh", "0395106907", false, 273000);
        debtors.add(debtor1);
        debtors.add(debtor2);
        debtors.add(debtor3);
        debtors.add(debtor4);
        debtors.add(debtor5);
        debtors.add(debtor6);
        sortController.sortDebtorListByDebitAmount(debtors);
        List<Debtor> expectedDebtors = new ArrayList<>();
        expectedDebtors.add(debtor2);
        expectedDebtors.add(debtor4);
        expectedDebtors.add(debtor3);
        expectedDebtors.add(debtor5);
        expectedDebtors.add(debtor1);
        expectedDebtors.add(debtor6);
        assertEquals(debtors, expectedDebtors);
    }

    @Test
    public void sortDebtPaymentListByDate_isCorrect() {
        DebtPayment debtPayment1= new DebtPayment("TN20200831160433591","KH20200831155915557",
                100000,"31-08-2020","16:04:33",1107000);
        DebtPayment debtPayment2= new DebtPayment("TN20200831160438603","KH20200831155915557",
                300000,"31-08-2020","16:04:38",1007000);
        DebtPayment debtPayment3= new DebtPayment("TN20200831160444485","KH20200831155915557",
                400000,"31-08-2020","16:04:44",707000);
        debtPayments.add(debtPayment1);
        debtPayments.add(debtPayment2);
        debtPayments.add(debtPayment3);
        sortController.sortDebtPaymentListByDate(debtPayments);
        List<DebtPayment> expectedDebtPayments = new ArrayList<>();
        expectedDebtPayments.add(debtPayment3);
        expectedDebtPayments.add(debtPayment2);
        expectedDebtPayments.add(debtPayment1);
        assertEquals(debtPayments, expectedDebtPayments);

    }


}
