package com.koit.capstonproject_version_1;

import com.koit.capstonproject_version_1.controller.DebtorController;
import com.koit.capstonproject_version_1.model.Debtor;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class DebtorControllerUnitTest {
    DebtorController debtorController = new DebtorController();

    @Test
    public  void getCurrentDebit_isCorrect(){
        List<Debtor> debtors = new ArrayList<>();
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
        long actualValue = debtorController.getCurrentDebit(debtors);
        long expectedValue = 4163000;
        assertEquals(expectedValue, actualValue);
        assertNotEquals(expectedValue+1, actualValue);
        assertNotEquals(expectedValue-1, actualValue);

    }
}
