package com.koit.capstonproject_version_1.View;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hsalf.smileyrating.SmileyRating;
import com.koit.capstonproject_version_1.R;

public class FeedbackActivity extends AppCompatActivity {
    private SmileyRating smileRating;
    private Button btnSendFeedback;
    private EditText etName;
    private EditText etPhoneNumber;
    private EditText etFeedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initView();
        initial();
    }
    private void initView(){
        smileRating = (SmileyRating) findViewById(R.id.smile_rating);
        btnSendFeedback = findViewById(R.id.btnSendFeedback);
        etName = findViewById(R.id.etName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etFeedback = findViewById(R.id.etFeedback);
    }
    private void initial(){
        //set initial value for input
        etName.setText("Đây là tên của người dùng");
        etPhoneNumber.setText("0395106907");
        //Set Title for smiley
        smileRating.setTitle(SmileyRating.Type.TERRIBLE,"Rất tệ");
        smileRating.setTitle(SmileyRating.Type.BAD,"Tệ");
        smileRating.setTitle(SmileyRating.Type.OKAY, "Tốt");
        smileRating.setTitle(SmileyRating.Type.GOOD, "Tuyệt");
        smileRating.setTitle(SmileyRating.Type.GREAT, "Rất tuyệt");

    }


    public void sendFeedBack(View v){

    }

}