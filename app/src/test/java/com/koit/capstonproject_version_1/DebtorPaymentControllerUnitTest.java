package com.koit.capstonproject_version_1;

import com.koit.capstonproject_version_1.Controller.DebtorPaymentController;
import com.koit.capstonproject_version_1.Model.DebtPayment;
import com.koit.capstonproject_version_1.Model.DebtorPayment;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class DebtorPaymentControllerUnitTest {
    DebtorPaymentController debtorPaymentController = new DebtorPaymentController();
    List<DebtorPayment> debtorPayments = new ArrayList<>();
    @Test
    public void getTotalPayAmountByAllDebtors_isCorrect(){
        List<DebtPayment> debtPayments = new ArrayList<>();
        DebtPayment debtPayment1= new DebtPayment("TN20200831160433591","KH20200831155915557",
                100000,"31-08-2020","16:04:33",1107000);
        DebtPayment debtPayment2= new DebtPayment("TN20200831160438603","KH20200831155915557",
                300000,"31-08-2020","16:04:38",1007000);
        DebtPayment debtPayment3= new DebtPayment("TN20200831160444485","KH20200831155915557",
                400000,"31-08-2020","16:04:44",707000);
        debtPayments.add(debtPayment1);
        debtPayments.add(debtPayment2);
        debtPayments.add(debtPayment3);

        DebtorPayment debtorPayment = new DebtorPayment();
        debtorPayment.setDebtorId("KH20200831155915557");
        debtorPayment.setDebtPaymentList(debtPayments);
        debtorPayments.add(debtorPayment);

        assertEquals(800000, debtorPaymentController.getTotalPayAmountByAllDebtors(debtorPayments));
        assertNotEquals(800001, debtorPaymentController.getTotalPayAmountByAllDebtors(debtorPayments));
        assertNotEquals(799999, debtorPaymentController.getTotalPayAmountByAllDebtors(debtorPayments));


    }
}
