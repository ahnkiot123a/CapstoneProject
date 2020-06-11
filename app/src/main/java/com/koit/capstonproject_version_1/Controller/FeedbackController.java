package com.koit.capstonproject_version_1.Controller;

import com.hsalf.smileyrating.SmileyRating;
import com.koit.capstonproject_version_1.Model.Feedback;
import com.koit.capstonproject_version_1.View.FeedbackActivity;

public class FeedbackController {
    Feedback feedback;
    FeedbackActivity feedbackActivity;
    public FeedbackController(){

    }
    public FeedbackController(FeedbackActivity feedbackActivity){
        this.feedbackActivity = feedbackActivity;
    }
    //add the feedback
    public void addNewFeedback(String userID, String fullname, String phoneNumber, String feedbackContent, long rating){
        if(!checkPhoneNumber(phoneNumber))
            feedbackActivity.setErrorInputEditTxt("Số điện thoại không đúng",feedbackActivity.getEtPhoneNumber());
        else if(!checkInputFeedback(feedbackContent,rating)){
                feedbackActivity.setErrorInputEditTxt("Hãy nhập nội dung phản hồi hoặc cảm " +
                        "nhận của bạn về ứng dụng ",feedbackActivity.getEtFeedback());
        }else {
            feedback = new Feedback(userID,fullname,phoneNumber,feedbackContent,rating);
            feedback.addFeedbackToFireBase();
            feedbackActivity.displayMess("Cảm ơn bạn đã gửi phản hồi cho chúng tôi");
            feedbackActivity.onBackPressed();
        }
    }
    //Check whether user input feedback and rating
    public boolean checkInputFeedback(String content, long rating) {
        if ((content == null || content.equals("")) && rating == -1) return false;
        return true;
    }
    //check that phone number satisfy the format
    public boolean checkPhoneNumber(String phoneNumber){
        InputController inputController = new InputController();
        String afterFormat = inputController.formatPhoneNumber(phoneNumber);
        if(phoneNumber!=null&&!phoneNumber.equals(""))
            return inputController.isPhoneNumber(afterFormat);
        return true;
    }
}
