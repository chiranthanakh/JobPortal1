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
import com.chiranths.jobportal1.Activities.OtpLoginActivity;
import com.chiranths.jobportal1.Model.ProductInfo;
import com.chiranths.jobportal1.Activities.BasicActivitys.UserDetailsActivity;
import com.chiranths.jobportal1.Activities.HotDealsactivity.HotDealsDetailsActivity;
import com.chiranths.jobportal1.CalldetailsRecords;
import com.chiranths.jobportal1.R;
import com.chiranths.jobportal1.Utilitys;

import java.util.List;

public class BottomhomeRecyclarviewAdaptor extends RecyclerView.Adapter<BottomhomeRecyclarviewAdaptor.ViewHolder> {
    private List<ProductInfo> productInfos;
    private Context context;
    private String number, name;
    CalldetailsRecords calldetails = new CalldetailsRecords() ;
    Utilitys utilitys = new Utilitys();
    public BottomhomeRecyclarviewAdaptor(List<ProductInfo> productInfos,Context context, String number, String name) {
        this.productInfos = productInfos;
        this.context = context;
        this.number = number;
        this.name = name;
    }

    @NonNull
    @Override
    public BottomhomeRecyclarviewAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View listItem= layoutInflater.inflate(R.layout.home_event_layout, parent, false);
        BottomhomeRecyclarviewAdaptor.ViewHolder viewHolder = new BottomhomeRecyclarviewAdaptor.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        ProductInfo productInfo = productInfos.get(position);

        Glide.with(context)
                .load(productInfo.getImage())
                .into(holder.iv_image);

        holder.tv_name_hot.setText(productInfo.getPname());
        holder.tv_final_price_hot.setText(productInfo.getPrice());
        holder.tv_hot_sqft.setText(productInfo.getPropertysize());
        holder.tv_loaction_hot.setText(productInfo.getLocation());
        holder.tv_contect_whome.setText("Contact: "+productInfo.getPosted());

        //holder.tv_btn_call_hot.setText("");

        holder.cv_deals.setOnClickListener(view -> {
            Intent intent =new Intent(context, HotDealsDetailsActivity.class);
            intent.putExtra("pid",productInfo.getPid());
            context.startActivity(intent);
        });

        //calling function
        holder.iv_call_bottom.setOnClickListener(view -> utilitys.navigateCall(context,productInfo.getNumber(),productInfo.getPname()));

        //whatsapp function
        holder.iv_whatsapp_bottom.setOnClickListener(view ->
                utilitys.navigateWhatsapp(context,productInfo.getNumber(),productInfo.getPname()));
    }

    @Override
    public int getItemCount() {
        return productInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_image,iv_call_bottom,iv_whatsapp_bottom;
        CardView cv_deals;
        TextView tv_name_hot,tv_final_price_hot,tv_hot_sqft,tv_loaction_hot,tv_btn_call_hot,tv_contect_whome;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_image = itemView.findViewById(R.id.iv_location_image);
            cv_deals = itemView.findViewById(R.id.cv_card_deals);
            tv_name_hot = itemView.findViewById(R.id.tv_name_hot);
            tv_final_price_hot = itemView.findViewById(R.id.tv_final_price_hot);
            tv_hot_sqft = itemView.findViewById(R.id.tv_hot_sqft);
            tv_loaction_hot = itemView.findViewById(R.id.tv_loaction_hot);
            iv_call_bottom = itemView.findViewById(R.id.iv_call_bottom);
            iv_whatsapp_bottom = itemView.findViewById(R.id.iv_whatsapp_bottom);
            tv_contect_whome = itemView.findViewById(R.id.tv_contect_whome);
            //tv_btn_call_hot = itemView.findViewById(R.id.tv_btn_call_hot);
        }
    }

}
