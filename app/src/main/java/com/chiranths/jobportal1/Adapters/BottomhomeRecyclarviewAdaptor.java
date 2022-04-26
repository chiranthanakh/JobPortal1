package com.chiranths.jobportal1.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.RecyclerView;

import com.chiranths.jobportal1.Activities.BasicActivitys.AdsDetailsActivity;
import com.chiranths.jobportal1.Activities.BasicActivitys.ProductInfo;
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

        Picasso.get().load(productInfo.getImage()).into(holder.iv_image);

        holder.cv_deals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(context, HotDealsDetailsActivity.class);
                intent.putExtra("pid",productInfo.getPid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_image;
        CardView cv_deals;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_image = itemView.findViewById(R.id.iv_location_image);
            cv_deals = itemView.findViewById(R.id.cv_card_deals);
        }
    }
}
