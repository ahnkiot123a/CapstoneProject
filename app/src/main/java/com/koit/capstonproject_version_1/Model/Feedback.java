package com.koit.capstonproject_version_1.Model;

public class Feedback {
    private String name;
    private String phone;
    private String feedbackContent;
    private int rating;

    public Feedback() {
    }

    public Feedback(String name, String phone, String feedbackContent, int rating) {
        this.name = name;
        this.phone = phone;
        this.feedbackContent = feedbackContent;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
