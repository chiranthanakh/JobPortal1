package com.chiranths.jobportal1.Activities.LoanActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chiranths.jobportal1.Activities.BasicActivitys.StartingActivity;
import com.chiranths.jobportal1.R;

public class LoanActivity extends AppCompatActivity implements View.OnClickListener {

    CardView cv_PL,cv_BL,cv_HL,cv_ML,cv_LAP,cv_VL;
    Button btn_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);

        initilize();
    }

    private void initilize() {

        cv_PL = findViewById(R.id.cv_personal_loan);
        cv_BL = findViewById(R.id.cv_business_loan);
        cv_HL = findViewById(R.id.cv_home_loan);
        cv_ML = findViewById(R.id.cv_martgage_loan);
        cv_LAP = findViewById(R.id.cv_lap);
        cv_VL = findViewById(R.id.cv_vehicle);
        //btn_next = findViewById(R.id.add_new_product);
        //btn_next.setOnClickListener(this);
        cv_PL.setOnClickListener(this);
        cv_BL.setOnClickListener(this);
        cv_HL.setOnClickListener(this);
        cv_LAP.setOnClickListener(this);
        cv_ML.setOnClickListener(this);
        cv_VL.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.cv_personal_loan:

                Intent intent = new Intent(LoanActivity.this, LoanForm.class);
                startActivity(intent);

                break;

            case R.id.cv_business_loan:
                Intent intent1 = new Intent(LoanActivity.this, LoanForm.class);
                startActivity(intent1);

                break;

            case R.id.cv_home_loan:
                Intent intent2 = new Intent(LoanActivity.this, LoanForm.class);
                startActivity(intent2);

                break;

            case R.id.cv_martgage_loan:
                Intent intent3 = new Intent(LoanActivity.this, LoanForm.class);
                startActivity(intent3);

                break;

            case R.id.cv_lap:
                Intent intent4 = new Intent(LoanActivity.this, LoanForm.class);
                startActivity(intent4);

                break;

            case R.id.cv_vehicle:
                Intent intent5 = new Intent(LoanActivity.this, LoanForm.class);
                startActivity(intent5);

                break;


        }
    }
}