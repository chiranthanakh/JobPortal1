package com.chiranths.jobportal1.Activities.LoanActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.chiranths.jobportal1.Activities.BasicActivitys.StartingActivity;
import com.chiranths.jobportal1.Adapters.CoroselListAdaptor;
import com.chiranths.jobportal1.Adapters.LoanCoroselListAdaptor;
import com.chiranths.jobportal1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class LoanActivity extends AppCompatActivity implements View.OnClickListener {

    CardView cv_PL,cv_BL,cv_HL,cv_ML,cv_LAP,cv_VL;
    Button btn_next;
    ArrayList coroselimagelist =new ArrayList();

    RecyclerView recyclerView,recyclarviewads;
    private LoanCoroselListAdaptor coroselListAdaptor;
    Handler mHandler = new Handler();

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
        recyclerView =(RecyclerView)findViewById(R.id.rv_loan_event);
        //btn_next = findViewById(R.id.add_new_product);
        //btn_next.setOnClickListener(this);
        cv_PL.setOnClickListener(this);
        cv_BL.setOnClickListener(this);
        cv_HL.setOnClickListener(this);
        cv_LAP.setOnClickListener(this);
        cv_ML.setOnClickListener(this);
        cv_VL.setOnClickListener(this);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                fetchcorosel();
            }
        });

    }

    private void fetchcorosel() {
        DatabaseReference coroselimage = FirebaseDatabase.getInstance().getReference().child("Corosels");

        coroselimage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    HashMap<String, Object> dataMap = (HashMap<String, Object>) snapshot.getValue();
                    for (String key : dataMap.keySet()){
                        Object data = dataMap.get(key);
                        try{

                            HashMap<String, Object> userData = (HashMap<String, Object>) data;
                            coroselimagelist.add(userData.get("image"));

                        }catch (ClassCastException cce){

                            try{
                                String mString = String.valueOf(dataMap.get(key));
                                //addTextToView(mString);
                            }catch (ClassCastException cce2){

                            }
                        }
                    }

                    coroselListAdaptor =new LoanCoroselListAdaptor(coroselimagelist,LoanActivity.this);
                    RecyclerView.LayoutManager nlayoutManager = new LinearLayoutManager(LoanActivity.this, RecyclerView.HORIZONTAL, false);
                    recyclerView.setLayoutManager(nlayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    SnapHelper snapHelper = new PagerSnapHelper();
                    snapHelper.attachToRecyclerView(recyclerView);
                    snapHelper.onFling(20,20);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(coroselListAdaptor);

                        }
                    });
                    coroselListAdaptor.notifyItemRangeInserted(0, coroselimagelist.size());

                   /* recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                                mHandler2.removeCallbacks(SCROLLING_RUNNABLE);
                                Handler postHandler = new Handler();
                                postHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerView.setAdapter(coroselListAdaptor);
                                        recyclerView.smoothScrollBy(pixelsToMove, 0);

                                        mHandler2.postDelayed(SCROLLING_RUNNABLE, 200);
                                    }
                                }, 2000);

                        }
                    });

                    mHandler.postDelayed(SCROLLING_RUNNABLE, 200);*/
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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