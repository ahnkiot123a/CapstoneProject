package com.koit.capstonproject_version_1;

import com.koit.capstonproject_version_1.controller.FeedbackController;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FeedbackControllerUnitTest {
    FeedbackController feedbackController = new FeedbackController();


    @Test
    public void checkInputFeedback_isCorrect(){
        assertTrue(feedbackController.checkInputFeedback("",1));
        assertTrue(feedbackController.checkInputFeedback("app rat hay",1));
        assertTrue(feedbackController.checkInputFeedback("app rat hay",-1));
        assertFalse(feedbackController.checkInputFeedback(null,-1));
        assertFalse(feedbackController.checkInputFeedback("",-1));
    }

    @Test
    public void checkPhoneNumber_isCorrect(){
        //Check empty
        assertTrue(feedbackController.checkPhoneNumber(""));
        //Check format
        assertTrue(feedbackController.checkPhoneNumber("+84395106907"));
        //Check number of digits in phone number
        assertFalse(feedbackController.checkPhoneNumber("039510")); //6 digits
        assertFalse(feedbackController.checkPhoneNumber("034523659")); // 9 digits
        assertFalse(feedbackController.checkPhoneNumber("03452365918")); // 11 digits
        assertFalse(feedbackController.checkPhoneNumber("0333302351842")); //13 digits
        //Check phone number must start with 03/05/07/08/09
        assertFalse(feedbackController.checkPhoneNumber("0214856578"));// start with 02
        //Check correct format phone number
        assertTrue(feedbackController.checkPhoneNumber("0326797171")); //start with 03
        assertTrue(feedbackController.checkPhoneNumber("0526797171")); //start with 05
        assertTrue(feedbackController.checkPhoneNumber("0726797171")); //start with 07
        assertTrue(feedbackController.checkPhoneNumber("0826797171")); //start with 08
        assertTrue(feedbackController.checkPhoneNumber("0926797171")); //start with 09
    }


}
