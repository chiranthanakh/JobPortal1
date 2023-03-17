package com.chiranths.jobportal1.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chiranths.jobportal1.Activities.BasicActivitys.UserDetailsActivity;
import com.chiranths.jobportal1.Activities.Dashboard.StartingActivity;
import com.chiranths.jobportal1.Activities.jobs.MainActivity;
import com.chiranths.jobportal1.R;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpLoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtPhone,edt_otp;
    TextView tvSendOtp;
    CardView cvOtp;
    Button btn_continue;

    FirebaseAuth mAuth;
    String mVerificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_login);

        mAuth = FirebaseAuth.getInstance();

        edtPhone= findViewById(R.id.edt_phone);
        tvSendOtp= findViewById(R.id.tv_send_otp);
        cvOtp= findViewById(R.id.cv_otp);
        btn_continue= findViewById(R.id.btn_continue);
        edt_otp= findViewById(R.id.edt_otp);
        tvSendOtp.setOnClickListener(this);
        btn_continue.setOnClickListener(this);

        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 10) {
                    tvSendOtp.setVisibility(View.VISIBLE);
                } else {
                    tvSendOtp.setVisibility(View.GONE);
                    cvOtp.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {

            case R.id.tv_send_otp:
                cvOtp.setVisibility(View.VISIBLE);

                // OnVerificationStateChangedCallbacks

                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber("+91" + edtPhone.getText().toString())            // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(this)                 // Activity (for callback binding)
                                .setCallbacks(mCallbacks)           // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
                break;

            case R.id.btn_continue:

                if (edt_otp.length() == 0) {
                    Toast.makeText(getApplicationContext(),"Enter the otp", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, edt_otp.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }

                break;
        }
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d("TAG", "onVerificationCompleted:" + credential);

            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {


            Toast.makeText(getApplicationContext(),"failed", Toast.LENGTH_SHORT).show();


            Log.w("TAG", "onVerificationFailed", e);

            if (e instanceof FirebaseAuthInvalidCredentialsException) {

            } else if (e instanceof FirebaseTooManyRequestsException) {

            }

        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d("TAG", "onCodeSent:" + verificationId);

            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;

            // ...
        }


    };

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            startActivity(new Intent(OtpLoginActivity.this, StartingActivity.class));
                            finish();

                            // FirebaseUser user = task.getResult().getUser();

                        } else {

                            Toast.makeText(getApplicationContext(), "Invalid Otp", Toast.LENGTH_SHORT).show();

                           /* Log.w("TAG", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }*/
                        }
                    }
                });
    }
}