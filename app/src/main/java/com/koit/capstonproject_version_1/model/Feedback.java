package com.koit.capstonproject_version_1.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Feedback {
    private String userID;
    private String fullName;
    private String phoneNumber;
    private String content;
    private long rating;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    public Feedback() {
    }

    public Feedback(String userID,String fullName, String phoneNumber, String content, long rating) {
        this.userID = userID;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.content = content;
        this.rating = rating;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    //add new Feed back to fire base
    public void addFeedbackToFireBase(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Feedbacks");
        databaseReference.push().setValue(this);
    }
}
