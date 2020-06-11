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
            assertTrue(inputController.isPhoneNumber("+84365106907"));
            //Check number of digits in phone number
            assertTrue(inputController.isPhoneNumber("039510")); //6 digits
            assertFalse(inputController.isPhoneNumber("034523659")); // 9 digits
            assertFalse(inputController.isPhoneNumber("03452365918")); // 11 digits
            assertFalse(inputController.isPhoneNumber("0333302351842")); //13 digits
            //Check phone number must start with 03/05/07/08/09
            assertFalse(inputController.isPhoneNumber("0214856578"));// start with 02
            //Check correct format phone number
            assertTrue(inputController.isPhoneNumber("0326797171"));
            assertTrue(inputController.isPhoneNumber("0526797171"));
            assertTrue(inputController.isPhoneNumber("0726797171"));
            assertTrue(inputController.isPhoneNumber("0826797171"));
            assertTrue(inputController.isPhoneNumber("0926797171"));
        }
}
