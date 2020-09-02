package com.koit.capstonproject_version_1;

import com.koit.capstonproject_version_1.controller.ValidateController;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

public class ValidateControllerUnitTest {
    ValidateController validateController = new ValidateController();
    @Test
    public void getMd5Password_isCorrect() {
        // check password and getMd5 password are difference or not
        String password = "123456";
        assertNotEquals(password,validateController.getMd5(password));
        // check length of getMd5 of password is 32
        assertEquals(32,validateController.getMd5(password).length());
        assertNotEquals(31,validateController.getMd5(password).length());
        assertNotEquals(33,validateController.getMd5(password).length());
    }
}
