package com.koit.capstonproject_version_1.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.koit.capstonproject_version_1.controller.TimeController;
import com.koit.capstonproject_version_1.controller.UserController;
import com.koit.capstonproject_version_1.model.User;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.helper.Helper;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInformationActivity extends AppCompatActivity {
    private EditText edEmail, edPhoneNumber, edFullname, edAddress, edStoreName;
    private TextView tvFirstName, tvDob;
    private RadioButton rbMale, rbFemale;
    private CircleImageView profile_img;

    private UserController userController;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_user_information);

        findViewById();

        //set title in toolbar
        Toolbar toolbar = findViewById(R.id.toolbarGeneral);
        TextView tvToolbarTitle = toolbar.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Thông tin cá nhân");

        Intent intent = getIntent();
        currentUser = (User) intent.getSerializableExtra("currentUser");
        setCurrentUserInfo();
        userController = new UserController(this);
    }


    private void setCurrentUserInfo() {
        if (currentUser.getFullName().length()>0){
//            tvFirstName.setText(currentUser.getFullName().charAt(0) + "");
            Helper.getInstance().setImage(profile_img,tvFirstName,currentUser.getFullName().charAt(0));

        } else {
            tvFirstName.setText("");
            profile_img.setImageResource(R.drawable.ic_account_circle_black_24dp);
            profile_img.setBackground(null);
        }

        edFullname.setText(currentUser.getFullName());
        edEmail.setText(currentUser.getEmail());
        edPhoneNumber.setText(currentUser.getPhoneNumber());
        tvDob.setText(currentUser.getDateOfBirth());
        edAddress.setText(currentUser.getAddress());
        edStoreName.setText(currentUser.getStoreName());
        if (currentUser.isGender()) rbMale.setChecked(true);
        else rbFemale.setChecked(true);


    }

    private void findViewById() {
        edFullname = findViewById(R.id.edFullname);
        edEmail = findViewById(R.id.edEmail);
        edPhoneNumber = findViewById(R.id.edPhoneNumber);
        // radioGender = findViewById(R.id.radioGender);
        edAddress = findViewById(R.id.edAddress);
        edStoreName = findViewById(R.id.edStoreName);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        tvFirstName = findViewById(R.id.tvFirstName);
        tvDob = findViewById(R.id.tvDob);
        profile_img = findViewById(R.id.profile_img);
        //btnUpdateUserInfo = findViewById(R.id.btnUpdateUserInfo);


    }

    public void updateUserInfo(View view) {
        userController.updateUserInformation(edFullname, edEmail, edPhoneNumber, tvDob, rbMale, edAddress, edStoreName, currentUser);

    }

    public void getNewDate(View view) {
        Date dob = new Date();
        dob = TimeController.getInstance().getDateAndMonthFromText(tvDob.getText().toString(), dob);
        TimeController.getInstance().chooseDayDialog(tvDob, dob, this);

    }

    public void back(View v) {
        onBackPressed();
    }

    public void setErrorEditTxt(String mess, EditText textInputEditText) {
        textInputEditText.requestFocus();
        textInputEditText.setError(mess);
    }

    public TextView getTvDob() {
        return tvDob;
    }
//    public EditText getEdDob() {
//        return edDob;
//    }


    public EditText getEdEmail() {
        return edEmail;
    }

    public void setEdEmail(EditText edEmail) {
        this.edEmail = edEmail;
    }

    public EditText getEdPhoneNumber() {
        return edPhoneNumber;
    }

    public void setEdPhoneNumber(EditText edPhoneNumber) {
        this.edPhoneNumber = edPhoneNumber;
    }

    public EditText getEdFullname() {
        return edFullname;
    }

    public void setEdFullname(EditText edFullname) {
        this.edFullname = edFullname;
    }

    public EditText getEdAddress() {
        return edAddress;
    }

    public void setEdAddress(EditText edAddress) {
        this.edAddress = edAddress;
    }

    public EditText getEdStoreName() {
        return edStoreName;
    }

    public void setEdStoreName(EditText edStoreName) {
        this.edStoreName = edStoreName;
    }
}