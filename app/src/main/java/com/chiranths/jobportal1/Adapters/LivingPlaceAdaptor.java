package com.chiranths.jobportal1.Adapters;

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
import com.chiranths.jobportal1.Activities.HotDealsactivity.HotDealsDetailsActivity;
import com.chiranths.jobportal1.Model.LivingPlaceModel;
import com.chiranths.jobportal1.R;

import java.util.List;

public class LivingPlaceAdaptor extends RecyclerView.Adapter<LivingPlaceAdaptor.ViewHolder> {


    private List<LivingPlaceModel> productInfos;
    private Context context;


    public LivingPlaceAdaptor(List<LivingPlaceModel> productInfos, Context context) {
        this.productInfos = productInfos;
        this.context = context;
    }

    @NonNull
    @Override
    public LivingPlaceAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View listItem= layoutInflater.inflate(R.layout.recyclar_livingplace_layout, parent, false);
        LivingPlaceAdaptor.ViewHolder viewHolder = new LivingPlaceAdaptor.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LivingPlaceModel productInfo = productInfos.get(position);

        Glide.with(context)
                .load(productInfo.getImage())
                .into(holder.iv_living_image);

        holder.tv_living_title.setText(productInfo.getTitle());
        holder.tv_living_rent_amount.setText("Rent: "+productInfo.getRentamount());
        holder.tv_living_sqft.setText(productInfo.getSqft());
        holder.tv_number_bhk.setText("Info: "+productInfo.getNuBHK());
        holder.tv_loaction_living.setText(productInfo.getLocation());

        holder.cv_card_living.setOnClickListener(new View.OnClickListener() {
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

        ImageView iv_living_image,iv_call_bottom,iv_whatsapp_bottom;
        CardView cv_card_living;
        TextView tv_living_title,tv_living_rent_amount,tv_living_sqft,tv_loaction_living,tv_number_bhk;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_living_image = itemView.findViewById(R.id.iv_living_image);
            cv_card_living = itemView.findViewById(R.id.cv_card_living);
            tv_living_title = itemView.findViewById(R.id.tv_living_title);
            tv_living_rent_amount = itemView.findViewById(R.id.tv_living_rent_amount);
            tv_living_sqft = itemView.findViewById(R.id.tv_living_sqft);
            tv_loaction_living = itemView.findViewById(R.id.tv_loaction_living);
            tv_number_bhk = itemView.findViewById(R.id.tv_number_bhk);
        }
    }
}

