package com.koit.capstonproject_version_1;

import com.koit.capstonproject_version_1.controller.RandomStringController;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class RandomStringControllerUnitTest {
    RandomStringController randomStringController = new RandomStringController();
    @Test
    public void randomID_isCorrect() {
        // check IDs difference or not
        assertNotEquals(randomStringController.randomString(),randomStringController.randomString());
    }
    @Test
    public void randomDebtorID_isCorrect() {
        // check ID of debtor difference or not
        assertNotEquals(randomStringController.randomDebtorId(),randomStringController.randomDebtorId());
        // check length of debtor ID
        assertNotEquals(18,randomStringController.randomDebtorId().length());
        assertEquals(19,randomStringController.randomDebtorId().length());
        assertNotEquals(20,randomStringController.randomDebtorId().length());
    }
    @Test
    public void randomInvoiceID_isCorrect() {
        // check ID of invoice difference or not
        assertNotEquals(randomStringController.randomInvoiceId(),randomStringController.randomInvoiceId());
        // check length of invoice ID
        assertNotEquals(14,randomStringController.randomInvoiceId().length());
        assertEquals(15,randomStringController.randomInvoiceId().length());
        assertNotEquals(16,randomStringController.randomInvoiceId().length());

    }
    @Test
    public void randomDebtPaymentID_isCorrect() {
        // check ID of debt payment difference or not
        assertNotEquals(randomStringController.randomDebtPaymentId(),randomStringController.randomDebtPaymentId());
        // check length of debt payment ID
        assertNotEquals(18,randomStringController.randomDebtPaymentId().length());
        assertEquals(19,randomStringController.randomDebtPaymentId().length());
        assertNotEquals(20,randomStringController.randomDebtPaymentId().length());
    }
}
