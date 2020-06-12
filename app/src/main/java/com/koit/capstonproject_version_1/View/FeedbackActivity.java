package com.koit.capstonproject_version_1.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.hsalf.smileyrating.SmileyRating;
import com.koit.capstonproject_version_1.Controller.FeedbackController;
import com.koit.capstonproject_version_1.Model.User;
import com.koit.capstonproject_version_1.R;

public class FeedbackActivity extends AppCompatActivity {
    private SmileyRating smileyRating;
    private Button btnSendFeedback;
    private TextInputEditText etName;
    private TextInputEditText etPhoneNumber;
    private TextInputEditText etFeedback;
    private User currentUser;

    FeedbackController feedbackController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Intent intent = getIntent();
        currentUser =(User)intent.getSerializableExtra("currentUser");
        initView();
        initial();
        feedbackController = new FeedbackController(this);
    }
    private void initView(){
        smileyRating = (SmileyRating) findViewById(R.id.smile_rating);
        btnSendFeedback = findViewById(R.id.btnSendFeedback);
        etName = findViewById(R.id.etName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etFeedback = findViewById(R.id.etFeedback);
    }
    private void initial(){
        //set initial value for input

        etName.setText(currentUser.getFullName());
        etPhoneNumber.setText(currentUser.getPhoneNumber());
        //Set Title for smiley
        smileyRating.setTitle(SmileyRating.Type.TERRIBLE,"Rất tệ");
        smileyRating.setTitle(SmileyRating.Type.BAD,"Tệ");
        smileyRating.setTitle(SmileyRating.Type.OKAY, "Tốt");
        smileyRating.setTitle(SmileyRating.Type.GOOD, "Tuyệt");
        smileyRating.setTitle(SmileyRating.Type.GREAT, "Rất tuyệt");
        smileyRating.setSmileySelectedListener(new SmileyRating.OnSmileySelectedListener() {
            @Override
            public void onSmileySelected(SmileyRating.Type type) {
                etFeedback.setError(null);
            }
        });
    }

    public void sendFeedBack(View v){
            String fullname = etName.getText().toString();
            String phoneNumber = etPhoneNumber.getText().toString();
            String feebackContent = etFeedback.getText().toString();
            long rating = (long)smileyRating.getSelectedSmiley().getRating();
            feedbackController.addNewFeedback(currentUser.getPhoneNumber(),fullname,phoneNumber,feebackContent,rating);
    }


    //Display message
    public void displayMess(String mess){
        Toast.makeText(this, mess, Toast.LENGTH_LONG).show();
    }
    //Display notification in TextInputEditText of Phone Number
    public void setErrorInputEditTxt(String mess,TextInputEditText textInputEditText){
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