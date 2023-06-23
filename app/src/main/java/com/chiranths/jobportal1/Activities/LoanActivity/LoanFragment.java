package com.chiranths.jobportal1.Activities.LoanActivity;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.chiranths.jobportal1.Adapters.LoanCoroselListAdaptor;
import com.chiranths.jobportal1.Adapters.LoanoffersAdaptor;
import com.chiranths.jobportal1.Model.LoanOffersModel;
import com.chiranths.jobportal1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class LoanFragment extends Fragment implements View.OnClickListener {

    CardView cv_PL,cv_BL,cv_HL,cv_ML,cv_LAP,cv_VL;
    Button btn_next;
    ArrayList coroselimagelist =new ArrayList();
    ArrayList<LoanOffersModel> bankadslist =new ArrayList<LoanOffersModel>();

    RecyclerView recyclerView,recyclarviewloanads;
    private LoanCoroselListAdaptor coroselListAdaptor;
    private LoanoffersAdaptor loanoffersAdaptor;
    Handler mHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_loan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initilize(view);

    }

    private void initilize(View view) {

        cv_PL = view.findViewById(R.id.cv_personal_loan);
        cv_BL = view.findViewById(R.id.cv_business_loan);
        cv_HL = view.findViewById(R.id.cv_home_loan);
        cv_ML = view.findViewById(R.id.cv_martgage_loan);
        cv_LAP = view.findViewById(R.id.cv_lap);
        cv_VL = view.findViewById(R.id.cv_vehicle);
        recyclerView =(RecyclerView)view.findViewById(R.id.rv_loan_event);
        recyclarviewloanads = view.findViewById(R.id.recycler_loanoffers);
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
                fetchbankads();
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

                    coroselListAdaptor =new LoanCoroselListAdaptor(coroselimagelist,getContext());
                    RecyclerView.LayoutManager nlayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
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

    private void fetchbankads() {
        DatabaseReference coroselimage = FirebaseDatabase.getInstance().getReference().child("banksadsforyou");

        coroselimage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    HashMap<String, Object> dataMap = (HashMap<String, Object>) snapshot.getValue();
                    for (String key : dataMap.keySet()){
                        Object data = dataMap.get(key);
                        try{

                            HashMap<String, Object> userData = (HashMap<String, Object>) data;
                            bankadslist.add(new LoanOffersModel(String.valueOf(userData.get("pid")),String.valueOf(userData.get("bankName")),String.valueOf(userData.get("intrestrate")),String.valueOf(userData.get("loanamount")),String.valueOf(userData.get("loantype")),String.valueOf(userData.get("description")),String.valueOf(userData.get("image"))));

                        }catch (ClassCastException cce){

                            try{
                                String mString = String.valueOf(dataMap.get(key));
                                //addTextToView(mString);
                            }catch (ClassCastException cce2){

                            }
                        }
                    }

                    loanoffersAdaptor =new LoanoffersAdaptor(bankadslist,getContext());
                    RecyclerView.LayoutManager nlayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                    recyclarviewloanads.setLayoutManager(nlayoutManager);
                    recyclarviewloanads.setItemAnimator(new DefaultItemAnimator());
                    SnapHelper snapHelper = new PagerSnapHelper();
                    snapHelper.attachToRecyclerView(recyclarviewloanads);
                    snapHelper.onFling(20,20);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            recyclarviewloanads.setAdapter(loanoffersAdaptor);
                        }
                    });
                    loanoffersAdaptor.notifyItemRangeInserted(0, bankadslist.size());

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
                Intent intent = new Intent(getContext(), LoanFormfirst.class);
                startActivity(intent);
                break;

            case R.id.cv_business_loan:
                Intent intent1 = new Intent(getContext(), LoanFormGeneral.class);
                startActivity(intent1);
                break;

            case R.id.cv_home_loan:
                Intent intent2 = new Intent(getContext(), LoanFormfirst.class);
                startActivity(intent2);
                break;

            case R.id.cv_martgage_loan:
                Intent intent3 = new Intent(getContext(), LoanFormfirst.class);
                startActivity(intent3);
                break;

            case R.id.cv_lap:
                Intent intent4 = new Intent(getContext(), LoanFormfirst.class);
                startActivity(intent4);
                break;

            case R.id.cv_vehicle:
                Intent intent5 = new Intent(getContext(), LoanFormfirst.class);
                startActivity(intent5);
                break;
        }
    }
}
