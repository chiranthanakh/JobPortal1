package com.chiranths.jobportal1.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chiranths.jobportal1.Activities.BasicActivitys.HotelsDetails;
import com.chiranths.jobportal1.Model.HotelsModel;
import com.chiranths.jobportal1.Model.ProductInfo;
import com.chiranths.jobportal1.Activities.HotDealsactivity.HotDealsDetailsActivity;
import com.chiranths.jobportal1.R;

import java.util.List;

public class CenterHomeadaptor extends RecyclerView.Adapter<CenterHomeadaptor.ViewHolder> {


    private List<HotelsModel> productInfos;
    private Context context;


    public CenterHomeadaptor(List<HotelsModel> productInfos, Context context) {
        this.productInfos = productInfos;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View listItem= layoutInflater.inflate(R.layout.recyclar_center_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        HotelsModel productInfo = productInfos.get(position);

        Glide.with(context)
                .load(productInfo.getImage())
                .into(holder.iv_image);

        holder.tv_name_hot.setText(productInfo.getName());
        holder.tv_final_price_hot.setText("Rent: "+productInfo.getPrice());
        holder.tv_hotel_website.setText(productInfo.getWebsite());
        holder.tv_advance.setText("Advance: ");
        holder.tv_loaction_hot.setText(productInfo.getAddress());

        holder.cv_deals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(context, HotelsDetails.class);
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

        ImageView iv_image,iv_call_bottom,iv_whatsapp_bottom;
        CardView cv_deals;
        TextView tv_name_hot,tv_final_price_hot,tv_hotel_website,tv_loaction_hot,tv_advance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_image = itemView.findViewById(R.id.iv_location_image_center);
            cv_deals = itemView.findViewById(R.id.cv_card_deals_center);
            tv_name_hot = itemView.findViewById(R.id.tv_name_hot_center);
            tv_final_price_hot = itemView.findViewById(R.id.tv_final_price_hot_center);
            tv_hotel_website = itemView.findViewById(R.id.tv_hotel_website);
            tv_loaction_hot = itemView.findViewById(R.id.tv_loaction_hot_center);
            tv_advance = itemView.findViewById(R.id.tv_home_advance);
        }
    }
}
