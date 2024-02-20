package com.sbd.gbd.Adapters;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sbd.gbd.Model.TravelsModel;
import com.sbd.gbd.R;
import com.sbd.gbd.Utilitys.Utilitys;

import java.util.ArrayList;
import java.util.List;

public class TravelsAdaptor extends RecyclerView.Adapter<TravelsAdaptor.ViewHolder> {
    private List<TravelsModel> productInfos;
    private Context context;
    private String number, name;
    Utilitys utilitys = new Utilitys();

    public TravelsAdaptor(ArrayList<TravelsModel> productInfos, Context context) {
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

        holder.iv_const_call_bottom.setOnClickListener(view -> {
            utilitys.navigateCall(context,productInfo.getContactDetails(),productInfo.getOwnerName());
        });

        holder.iv_const_whatsapp_bottom.setOnClickListener(view -> {
            utilitys.navigateWhatsapp(context,productInfo.getContactDetails(),productInfo.getOwnerName());
        });
    }

    @Override
    public int getItemCount() {
        return productInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_vehicle_image,iv_const_whatsapp_bottom,iv_const_call_bottom;
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
            iv_const_whatsapp_bottom = itemView.findViewById(R.id.iv_const_whatsapp_bottom);
            iv_const_call_bottom = itemView.findViewById(R.id.iv_const_call_bottom);
        }
    }

}
