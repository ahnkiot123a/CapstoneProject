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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.koit.capstonproject_version_1.Controller.InputController;
import com.koit.capstonproject_version_1.Controller.UserController;
import com.koit.capstonproject_version_1.Model.User;
import com.koit.capstonproject_version_1.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UserInformationActivity extends AppCompatActivity {
    private EditText edDob,edEmail,edPhoneNumber,edFullname,edAddress,edStoreName;
    private RadioGroup radioGender;
    private RadioButton rbMale,rbFemale;
    private Button btnUpdateUserInfo;
    private DatabaseReference databaseReference;
    private UserController userController;

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

                if (fullName.trim().equals("")){
                    Toast.makeText(UserInformationActivity.this,"Vui lòng nhập họ và tên",Toast.LENGTH_SHORT).show();
                } else if (email.trim().equals("")){
                    Toast.makeText(UserInformationActivity.this,"Vui lòng nhập Email",Toast.LENGTH_SHORT).show();
                } else if (!isEmail(email)){
                    Toast.makeText(UserInformationActivity.this,"Email không hợp lệ, vui lòng nhập lại Email",Toast.LENGTH_SHORT).show();
                } else if (dob.trim().equals("")){
                    Toast.makeText(UserInformationActivity.this,"Vui lòng nhập ngày sinh",Toast.LENGTH_SHORT).show();
                }else if (!isDate(dob)){
                    Toast.makeText(UserInformationActivity.this,"Ngày sinh không hợp lệ, vui lòng nhập lại ngày sinh",Toast.LENGTH_SHORT).show();
                } else if (address.trim().equals("")){
                    Toast.makeText(UserInformationActivity.this,"Vui lòng nhập địa chỉ",Toast.LENGTH_SHORT).show();
                } else if (storeName.trim().equals("")){
                    Toast.makeText(UserInformationActivity.this,"Vui lòng nhập tên cửa hàng",Toast.LENGTH_SHORT).show();
                }   else {


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
                    Toast.makeText(UserInformationActivity.this,"Cập nhật thông tin thành công",Toast.LENGTH_SHORT).show();

                }

            }
        });
    }
    private void setCurrentUserInfo(){
        edFullname.setText(currentUser.getFullName());
      if(!currentUser.getEmail().trim().isEmpty())  {
          edEmail.setEnabled(false);
        edEmail.setText(currentUser.getEmail());}
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
    public boolean isEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
    public boolean isDate(String strDate)
    {
        /* Check if date is 'null' */
        if (strDate.trim().equals(""))
        {
            return true;
        }
        /* Date is not 'null' */
        else
        {
            /*
             * Set preferred date format,
             * For example MM-dd-yyyy, MM.dd.yyyy,dd.MM.yyyy etc.*/
            SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd");
            sdfrmt.setLenient(false);
            /* Create Date object
             * parse the string into date
             */
            try
            {
                Date javaDate = sdfrmt.parse(strDate);
                System.out.println(strDate+" is valid date format");
            }
            /* Date format is invalid */
            catch (ParseException e)
            {
                System.out.println(strDate+" is Invalid Date format");
                return false;
            }
            /* Return true if date format is valid */
            return true;
        }
    }
}