package com.chiranths.jobportal1.Activities.LoanActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chiranths.jobportal1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class LoanForm extends AppCompatActivity {

    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private EditText et_name,et_address,edt_email,et_number,et_dob;
    private Button btn_next;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_form2);
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Loans");
        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        initilize();

    }

    private void initilize() {
        et_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
       // et_residence = findViewById(R.id.et_address);
       // et_monthly_income = findViewById(R.id.edt_monthly_income);
        btn_next = findViewById(R.id.btn_next);
        et_number = findViewById(R.id.edt_number);
        et_dob = findViewById(R.id.edt_dob);
        et_address =findViewById(R.id.edt_address);

        btn_next.setOnClickListener(view -> {
            //SaveProductInfoToDatabase();
            if (TextUtils.isEmpty(et_number.getText().toString())) {
                // when mobile number text field is empty
                // displaying a toast message.
                Toast.makeText(LoanForm.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
            } else {
                // if the text field is not empty we are calling our
                // send OTP method for getting OTP from Firebase.
                String phone = "+91" + et_number.getText().toString();
                //sendVerificationCode(phone);
                SaveUserDetails();
            }
        });

    }

    private void SaveUserDetails()
    {

        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Bundle bundle = getIntent().getExtras();
        String amount = bundle.getString("amount", "0");
        String income = bundle.getString("income", "0");
        String emptype = bundle.getString("emptype", "");
        String pancard = bundle.getString("pancard", "");

        HashMap<String, Object> LoanMap = new HashMap<>();
        LoanMap.put("name", et_name.getText().toString());
        LoanMap.put("number",et_number.getText().toString());
        LoanMap.put("email",edt_email.getText().toString());
        LoanMap.put("dob", et_dob.getText().toString());
        LoanMap.put("address", et_address.getText().toString());
        LoanMap.put("reqAmount", amount);
        LoanMap.put("monthincome", income);
        LoanMap.put("emptype", emptype);
        LoanMap.put("pancard",pancard);

        ProductsRef.child(et_number.getText().toString()).updateChildren(LoanMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            loadingBar.dismiss();
                            Toast.makeText(LoanForm.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(LoanForm.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}