package com.chiranths.jobportal1.Adapters;

import static android.Manifest.permission.CALL_PHONE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chiranths.jobportal1.Activities.BasicActivitys.AdsDetailsActivity;
import com.chiranths.jobportal1.Activities.BasicActivitys.ProductInfo;
import com.chiranths.jobportal1.Activities.BasicActivitys.UserDetailsActivity;
import com.chiranths.jobportal1.Activities.HotDealsactivity.HotDealsDetailsActivity;
import com.chiranths.jobportal1.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BottomhomeRecyclarviewAdaptor extends RecyclerView.Adapter<BottomhomeRecyclarviewAdaptor.ViewHolder> {


    private List<ProductInfo> productInfos;
    private Context context;


    public BottomhomeRecyclarviewAdaptor(List<ProductInfo> productInfos,Context context) {
        this.productInfos = productInfos;
        this.context = context;
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

        Picasso.get()
                .load(productInfo.getImage())
                //.centerCrop()
                //.resize(120,140)
                .into(holder.iv_image);

        holder.tv_name_hot.setText(productInfo.getPname());
        holder.tv_final_price_hot.setText(productInfo.getPrice());
        holder.tv_hot_sqft.setText(productInfo.getPropertysize());
        holder.tv_loaction_hot.setText(productInfo.getLocation());
        //holder.tv_btn_call_hot.setText("");

        holder.cv_deals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(context, HotDealsDetailsActivity.class);
                intent.putExtra("pid",productInfo.getPid());
                context.startActivity(intent);
            }
        });

        holder.iv_call_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+productInfo.getNumber()));
                context.startActivity(callIntent);*/

                Intent intent = new Intent(context, UserDetailsActivity.class);
                context.startActivity(intent);

            }
        });

        holder.iv_whatsapp_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "https://api.whatsapp.com/send?phone="+"91"+productInfo.getNumber();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return productInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_image,iv_call_bottom,iv_whatsapp_bottom;
        CardView cv_deals;
        TextView tv_name_hot,tv_final_price_hot,tv_hot_sqft,tv_loaction_hot,tv_btn_call_hot;

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
            //tv_btn_call_hot = itemView.findViewById(R.id.tv_btn_call_hot);


        }
    }

}
