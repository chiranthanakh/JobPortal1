package com.chiranths.jobportal1.Adapters;

import static android.Manifest.permission.CALL_PHONE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chiranths.jobportal1.Activities.BasicActivitys.UserDetailsActivity;
import com.chiranths.jobportal1.Activities.Businesthings.BusinessDetails;
import com.chiranths.jobportal1.CalldetailsRecords;
import com.chiranths.jobportal1.Model.BusinessModel;
import com.chiranths.jobportal1.R;
import com.chiranths.jobportal1.Utilitys.AppConstants;
import com.chiranths.jobportal1.Utilitys.Utilitys;

import java.util.List;

public class BusinessAdaptor extends RecyclerView.Adapter<BusinessAdaptor.ViewHolder> {

    private List<BusinessModel> productInfos;
    private Context context;
    CalldetailsRecords calldetailsRecords = new CalldetailsRecords();
    Utilitys utilitys = new Utilitys();

    public BusinessAdaptor(List<BusinessModel> productInfos, Context context) {
        this.productInfos = productInfos;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View listItem= layoutInflater.inflate(R.layout.business_items_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        BusinessModel propertyinfo = productInfos.get(position);

        Glide.with(context)
                .load(propertyinfo.getImage())
                .into(holder.business_image);

        holder.tv_business_type.setText(propertyinfo.getBusiness_category());
        holder.tv_business_locatin.setText(propertyinfo.getLocation());
        holder.tv_business_name.setText(propertyinfo.getBusinessname());
        holder.tv_business_servicess.setText(propertyinfo.getDescription());

        holder.cv_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(context, BusinessDetails.class);
                intent.putExtra(AppConstants.pid,propertyinfo.getPid());
                context.startActivity(intent);
            }
        });

        //calling function
       /* holder.iv_call_business.setOnClickListener(view ->
                utilitys.navigateCall(context,productInfo.getNumber(),productInfo.getPname())
        );

        //whatsapp function
        holder.iv_whatsup_business.setOnClickListener(view ->
                utilitys.navigateWhatsapp(context,productInfo.getNumber(),productInfo.getPname())
        );*/

        //calling function
        holder.iv_call_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check permition
                String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                int res = context.checkCallingOrSelfPermission(permission);
                if (res == PackageManager.PERMISSION_GRANTED) {
                    SharedPreferences sh = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                    String nameofuser = sh.getString("name", "");
                    String userNumber = sh.getString(AppConstants.number, "");
                    String useremail = sh.getString("email", "");

                    String cnumber = propertyinfo.getNumber();
                    String cname = propertyinfo.getPid();
                    if (!userNumber.equals("")) {
                        calldetailsRecords.callinfo(userNumber, nameofuser, cnumber, cname);
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + propertyinfo.getNumber()));
                        context.startActivity(callIntent);
                    } else {
                        Intent intent = new Intent(context, UserDetailsActivity.class);
                        context.startActivity(intent);
                    }
                }
            }
        });
        //whatsapp function
        holder.iv_whatsup_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                int res = context.checkCallingOrSelfPermission(permission);
                if (res == PackageManager.PERMISSION_GRANTED) {
                    SharedPreferences sh = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                    String nameofuser = sh.getString("name", "");
                    String userNumber = sh.getString(AppConstants.number, "");
                    String useremail = sh.getString("email", "");

                    if (!userNumber.equals("")) {
                        String url = "https://api.whatsapp.com/send?phone=" + "91" + propertyinfo.getNumber();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        context.startActivity(i);
                        calldetailsRecords.callinfo(userNumber, nameofuser, propertyinfo.getNumber(), propertyinfo.getPid());
                    } else {
                        if (ContextCompat.checkSelfPermission(context, CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            Intent intent = new Intent(context, UserDetailsActivity.class);
                            context.startActivity(intent);
                        } else {
                            ActivityCompat.requestPermissions((Activity) context,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    1);
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView business_image,iv_call_business,iv_whatsup_business;
        CardView cv_layout;
        TextView tv_business_locatin,tv_business_name,tv_business_type,tv_business_servicess;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_business_locatin = itemView.findViewById(R.id.tv_business_locatin);
            tv_business_name = itemView.findViewById(R.id.tv_business_name);
            tv_business_type = itemView.findViewById(R.id.tv_business_type);
            cv_layout = itemView.findViewById(R.id.cv_layout2);
            business_image = itemView.findViewById(R.id.business_image);
            tv_business_servicess = itemView.findViewById(R.id.business_servicess);
            iv_call_business = itemView.findViewById(R.id.iv_call_bottom_business);
            iv_whatsup_business = itemView.findViewById(R.id.iv_whatsapp_bottom_business);
        }
    }
}
