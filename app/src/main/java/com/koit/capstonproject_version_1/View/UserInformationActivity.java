package com.koit.capstonproject_version_1.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.koit.capstonproject_version_1.Model.User;
import com.koit.capstonproject_version_1.R;

import java.util.Calendar;

public class UserInformationActivity extends AppCompatActivity {
    private EditText edDob,edEmail,edPhoneNumber,edFullname,edAddress,edStoreName;
    private RadioGroup radioGender;
    private RadioButton rbMale,rbFemale;
    private Button btnUpdateUserInfo;
    private DatabaseReference databaseReference;
    private User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        findViewById();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        Intent intent = getIntent();
         currentUser =(User)intent.getSerializableExtra("currentUser");
        setCurrentUserInfo();
        actionBtnUpdateUserInfo();

    }


    private  void actionBtnUpdateUserInfo(){
        btnUpdateUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = edFullname.getText().toString();
                String email = edEmail.getText().toString();
               // String phoneNumber = edPhoneNumber.getText().toString();
                String address = edAddress.getText().toString();
                String storeName = edStoreName.getText().toString();
                String dob = edDob.getText().toString();
                boolean gender = (rbMale.isChecked()) ? true : false;
                databaseReference.child(currentUser.getPhoneNumber()).child("fullName").setValue(fullName);
                databaseReference.child(currentUser.getPhoneNumber()).child("email").setValue(email);
                databaseReference.child(currentUser.getPhoneNumber()).child("gender").setValue(gender);
                databaseReference.child(currentUser.getPhoneNumber()).child("dateOfBirth").setValue(dob);
                databaseReference.child(currentUser.getPhoneNumber()).child("address").setValue(address);
                databaseReference.child(currentUser.getPhoneNumber()).child("storeName").setValue(storeName);
                currentUser.setFullName(fullName);
                currentUser.setEmail(email);
                currentUser.setGender(gender);
                currentUser.setDateOfBirth(dob);
                currentUser.setAddress(address);
                currentUser.setStoreName(storeName);


            }
        });
    }
    private void setCurrentUserInfo(){
        edFullname.setText(currentUser.getFullName());
        edEmail.setText(currentUser.getEmail());
        edPhoneNumber.setText(currentUser.getPhoneNumber());
        edDob.setText(currentUser.getDateOfBirth());
        edAddress.setText(currentUser.getAddress());
        edStoreName.setText(currentUser.getStoreName());
        if (currentUser.isGender())  rbMale.setChecked(true); else rbFemale.setChecked(true);


    }
    private void findViewById(){
        edFullname = findViewById(R.id.edFullname);
        edEmail = findViewById(R.id.edEmail);
        edPhoneNumber = findViewById(R.id.edPhoneNumber);
        radioGender = findViewById(R.id.radioGender);
        edDob = findViewById(R.id.edDob);
        edAddress = findViewById(R.id.edAddress);
        edStoreName = findViewById(R.id.edStoreName);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        btnUpdateUserInfo = findViewById(R.id.btnUpdateUserInfo);



    }


    public void getNewDate(View view) {
        DatePickerDialog.OnDateSetListener mListener = mListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1 = i1 + 1;
                String date = i1 < 10 ? i + "-0" + i1 + "-" + i2 : i + "-" + i1 + "-" + i2;
               String updateDate = i1 + "/" + i2 + "/" + i;
                edDob.setText(date);
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this, mListener, year, month, day);
        dialog.show();
    }
}