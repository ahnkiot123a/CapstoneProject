package com.koit.capstonproject_version_1.View;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.koit.capstonproject_version_1.R;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout etPhoneNumberRA;
    //ma vung Viet Nam la 84
    private Button btnContinue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnContinue = findViewById(R.id.btnContinue);
        //phone
        etPhoneNumberRA = findViewById(R.id.etPhoneNumberRA);

        //click button "Tiep tuc"
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = etPhoneNumberRA.getEditText().getText().toString().trim();
                if (!checkPhonenumber(number)) {
                    etPhoneNumberRA.setError("Số điện thoại không chính xác");
                    etPhoneNumberRA.requestFocus();
                    return;
                }
                Intent intent = new Intent(RegisterActivity.this, RegisterVerifyPhone.class);
                intent.putExtra("phonenumber", number);

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(etPhoneNumberRA, "phoneTran");
                pairs[1] = new Pair<View, String>(btnContinue, "btnDoneTran");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });
    }

    //check phone number is valid or not
    private boolean checkPhonenumber(String number) {
        //bat dau = 0 hoac NOT bat dau = 0
        //Gom 9 hoac 10 ki tu
        String regexStr = "^[0]{0,1}+[0-9]{9}$";
        if (number.matches(regexStr)) {
            return true;
        } else return false;
    }



    public void back(View view) {
        onBackPressed();
    }

}