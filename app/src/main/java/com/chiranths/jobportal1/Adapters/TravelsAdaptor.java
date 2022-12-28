package com.chiranths.jobportal1.Adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chiranths.jobportal1.Activities.LoanActivity.LoanForm;
import com.chiranths.jobportal1.Model.LoanOffersModel;
import com.chiranths.jobportal1.Model.TravelsModel;
import com.chiranths.jobportal1.R;

import java.util.List;

public class TravelsAdaptor extends RecyclerView.Adapter<TravelsAdaptor.ViewHolder> {


    private List<TravelsModel> productInfos;
    private Context context;
    private String number, name;


    public TravelsAdaptor(List<TravelsModel> productInfos, Context context) {
        this.productInfos = productInfos;
        this.context = context;
        this.number = number;
        this.name = name;
    }

    @NonNull
    @Override
    public TravelsAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View listItem= layoutInflater.inflate(R.layout.activity_travels_adaptor, parent, false);
        TravelsAdaptor.ViewHolder viewHolder = new TravelsAdaptor.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TravelsAdaptor.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        TravelsModel productInfo = productInfos.get(position);

        Glide.with(context)
                .load(productInfo.getImage())
                .into(holder.iv_vehicle_image);

        holder.travel_vehicle_name.setText(productInfo.getVehicle());
        holder.tv_travel_category.setText(productInfo.getCategory());
        holder.tv_cost_km.setText(productInfo.getCostperKM());
        holder.tv_vehicle_number.setText(productInfo.getVehicleNumber());

        holder.btn_vehicle_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent intent = new Intent(context, LoanForm.class);
               // context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return productInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_vehicle_image;
        CardView cv_deals;
        private LinearLayout btn_applayLoan;
        LinearLayout btn_vehicle_contact;
        TextView travel_vehicle_name,tv_travel_category,tv_cost_km,tv_vehicle_number,tv_discription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            travel_vehicle_name = itemView.findViewById(R.id.travel_vehicle_name);
            tv_travel_category = itemView.findViewById(R.id.tv_travel_category);
            tv_cost_km = itemView.findViewById(R.id.tv_cost_km);
            tv_vehicle_number = itemView.findViewById(R.id.tv_vehicle_number);
            iv_vehicle_image = itemView.findViewById(R.id.iv_vehicle_image);
            btn_vehicle_contact = itemView.findViewById(R.id.btn_vehicle_contact);
        }
    }

}
