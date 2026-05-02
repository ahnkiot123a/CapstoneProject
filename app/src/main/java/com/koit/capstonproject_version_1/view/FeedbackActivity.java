package com.koit.capstonproject_version_1.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.koit.capstonproject_version_1.controller.FeedbackController;
import com.koit.capstonproject_version_1.model.User;
import com.koit.capstonproject_version_1.R;

public class FeedbackActivity extends AppCompatActivity {
    private RatingBar smileyRating;
    private Button btnSendFeedback;
    private TextInputEditText etName;
    private TextInputEditText etPhoneNumber;
    private TextInputEditText etFeedback;
    private User currentUser;
    private FirebaseUser firebaseUser;

    FeedbackController feedbackController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Intent intent = getIntent();
        currentUser =(User)intent.getSerializableExtra("currentUser");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        initView();
        initial();
        feedbackController = new FeedbackController(this);
    }
    private void initView(){
        smileyRating = findViewById(R.id.smile_rating);
        btnSendFeedback = findViewById(R.id.btnSendFeedback);
        etName = findViewById(R.id.etName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etFeedback = findViewById(R.id.etFeedback);
    }
    private void initial(){
        //set initial value for input
        if (currentUser != null){
        etName.setText(currentUser.getFullName());
        etPhoneNumber.setText(currentUser.getPhoneNumber());
        } else  if (firebaseUser != null){
            etName.setText(firebaseUser.getDisplayName());
           // etPhoneNumber.setText(currentUser.getPhoneNumber());
        }
        smileyRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                etFeedback.setError(null);
            }
        });
    }

    public void sendFeedBack(View v){
            String fullname = etName.getText().toString();
            String phoneNumber = etPhoneNumber.getText().toString();
            String feebackContent = etFeedback.getText().toString();
            long rating = Math.round(smileyRating.getRating());
           if(currentUser != null) feedbackController.addNewFeedback(currentUser.getPhoneNumber(),fullname,phoneNumber,feebackContent,rating);
           else if (firebaseUser != null)
               feedbackController.addNewFeedback(firebaseUser.getUid(),fullname,phoneNumber,feebackContent,rating);

    }


    //Display message
    public void displayMess(String mess){
        Toast.makeText(this, mess, Toast.LENGTH_LONG).show();
    }
    //Display notification in TextInputEditText of Phone Number
    public void setErrorInputEditTxt(String mess,TextInputEditText textInputEditText){
        textInputEditText.requestFocus();
        textInputEditText.setError(mess);
    }

    public TextInputEditText getEtPhoneNumber() {
        return etPhoneNumber;
    }

    public TextInputEditText getEtFeedback() {
        return etFeedback;
    }
    public void back(View v){
        onBackPressed();
    }
}