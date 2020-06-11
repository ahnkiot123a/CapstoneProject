package com.koit.capstonproject_version_1;

import com.koit.capstonproject_version_1.Controller.InputController;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InputControllerUnitTest {
        InputController inputController = new InputController();
        //Test formartNumber() function
        @Test
        public void formatNumber_isCorrect() {
            assertEquals("0395106907", inputController.formatPhoneNumber("+84395106907"));
            assertEquals("0326797171", inputController.formatPhoneNumber("0326797171"));
        }
}
