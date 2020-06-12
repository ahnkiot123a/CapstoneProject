package com.koit.capstonproject_version_1;

import com.koit.capstonproject_version_1.Controller.InputController;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InputControllerUnitTest {
    InputController inputController = new InputController();
    //Test formartNumber() function
    @Test
    public void formatNumber_isCorrect() {
        //If phone number start with +84
        assertEquals("0395106907", inputController.formatPhoneNumber("+84395106907"));
        //else
        assertEquals("0326797171", inputController.formatPhoneNumber("0326797171"));
    }
    // Test isPhoneNumber function
    @Test
    public void isPhoneNumber_isCorrect() {
        //Check empty
        assertFalse(inputController.isPhoneNumber(""));
        //Check format
        assertTrue(inputController.isPhoneNumber("+84395106907"));
        //Check number of digits in phone number
        assertFalse(inputController.isPhoneNumber("039510")); //6 digits
        assertFalse(inputController.isPhoneNumber("034523659")); // 9 digits
        assertFalse(inputController.isPhoneNumber("03452365918")); // 11 digits
        assertFalse(inputController.isPhoneNumber("0333302351842")); //13 digits
        //Check phone number must start with 03/05/07/08/09
        assertFalse(inputController.isPhoneNumber("0214856578"));// start with 02
        //Check correct format phone number
        assertTrue(inputController.isPhoneNumber("0326797171")); //start with 03
        assertTrue(inputController.isPhoneNumber("0526797171")); //start with 05
        assertTrue(inputController.isPhoneNumber("0726797171")); //start with 07
        assertTrue(inputController.isPhoneNumber("0826797171")); //start with 08
        assertTrue(inputController.isPhoneNumber("0926797171")); //start with 09
    }
    // Test isEmail() function
    @Test
    public void isEmail_isCorrect() {
        // Test empty Email
        assertFalse(inputController.isEmail(""));
        // Test email not include special characters
        assertFalse( inputController.isEmail("anhtunb98"));
        // Test email include only 1 "@"
        assertFalse( inputController.isEmail("anhtunb98@gmail"));
        // Test email inlcude 1 "@", 1 "."
        assertTrue( inputController.isEmail("anhtunb98@gmail.vn"));
        // Test email include 2 "@"
        assertFalse( inputController.isEmail("anhtunb98@gmail@vn"));
        // Test email inlude 1 "@" and many "."
        assertTrue( inputController.isEmail("tubase06155@fpt.edu.vn"));
        // test email include "@,." and a special character
        assertFalse( inputController.isEmail("anht!unb98@gmail.com"));
    }

    // Test isDate() function
    @Test
    public void isDate_isCorrect() {
        // Test empty date
        assertTrue(inputController.isDate(""));
        // Test date inlude only letters
        assertFalse( inputController.isDate("dfhdfhdfhdfhdfhxbxc"));
        // Test date inlude only digits
        assertFalse( inputController.isDate("334252345346545745457475"));
        // Test date inlcude special characters
        assertFalse( inputController.isDate("anhtunb98@gmail.vn"));
        // Test date format by "dd/MM/yyyy"
        assertFalse( inputController.isDate("11/06/2020"));
        // Test email formated by "yyyy-MM-dd"
        assertTrue( inputController.isDate("2020-06-11"));
    }
    // Test isPassword() function
    @Test
    public void isPassword_isCorrect() {
        // Test empty password
        assertFalse(inputController.isPassword(""));
        // Test password inlude letters
        assertFalse( inputController.isPassword("4435cgcb44"));
        // Test password inlude 5 digits
        assertFalse( inputController.isDate("12345"));
        // Test password inlcude 6 digits
        assertFalse( inputController.isDate("123456"));
        // Test password include 7 digits
        assertFalse( inputController.isDate("1234567"));

    }
}