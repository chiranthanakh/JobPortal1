package com.sbd.gbd.Utilitys;

import androidx.annotation.NonNull;

import com.sbd.gbd.Activities.Dashboard.Calldetails;
import com.sbd.gbd.Utilitys.AppConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CalldetailsRecords implements Calldetails {

    private DatabaseReference CallerRef;
    private String productRandomKey;

    @Override
    public void callinfo(String number, String name, String caller_number, String caller_name) {
        CallerRef = FirebaseDatabase.getInstance().getReference().child("callDetails");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd");
        String saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm a");
        String saveCurrentTime = currentTime.format(calendar.getTime());
        productRandomKey = saveCurrentDate + saveCurrentTime;
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put(AppConstants.date, saveCurrentDate);
        productMap.put(AppConstants.time, saveCurrentTime);
        productMap.put("called_name",name);
        productMap.put("called_number",number);
        productMap.put("caller_name", caller_name);
        productMap.put("caller_number",caller_number);

        CallerRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {}
                        else
                        {}
                    }
                });
            }
        }
