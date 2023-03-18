package com.chiranths.jobportal1.Activities.BasicActivitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chiranths.jobportal1.R;
import com.google.firebase.auth.FirebaseAuth;

public class UserDetailsActivity extends AppCompatActivity {

    // variable for FirebaseAuth class
    private FirebaseAuth mAuth;

    // variable for our text input
    // field for phone and OTP.
    private EditText edtPhone, edtOTP,edtEmail,edtName;

    // buttons for generating OTP and verifying OTP
    private Button verifyOTPBtn, generateOTPBtn;

    // string for storing our verification ID
    private String verificationId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        // below line is for getting instance
        // of our FirebaseAuth.
        mAuth = FirebaseAuth.getInstance();

        initilize();

    }

    private void initilize() {

        edtPhone = findViewById(R.id.edt_phone_login);
        edtName = findViewById(R.id.edt_name_login);
        edtEmail = findViewById(R.id.edt_email_login);
       // verifyOTPBtn = findViewById(R.id.btn_submit_login);
        generateOTPBtn = findViewById(R.id.btn_submit_login);

        generateOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);

                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("name", edtName.getText().toString());
                myEdit.putString("number", edtPhone.getText().toString());
                myEdit.commit();

                finish();

            }
        });
    }



}