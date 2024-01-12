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
import com.chiranths.jobportal1.Activities.Construction.ConstructionDetailsActivity;
import com.chiranths.jobportal1.Model.ConstructionModel;
import com.chiranths.jobportal1.R;
import com.chiranths.jobportal1.Utilitys.AppConstants;
import com.chiranths.jobportal1.Utilitys.Utilitys;

import java.util.List;

public class ConstructorAdaptor extends RecyclerView.Adapter<ConstructorAdaptor.ViewHolder> {

    private List<ConstructionModel> productInfos;
    private Context context;
    private String number, name;
    Utilitys utilitys = new Utilitys();

    public ConstructorAdaptor(List<ConstructionModel> productInfos, Context context) {
        this.productInfos = productInfos;
        this.context = context;
        this.number = number;
        this.name = name;
    }

    @NonNull
    @Override
    public ConstructorAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View listItem= layoutInflater.inflate(R.layout.activity_construction_adaptor, parent, false);
        ConstructorAdaptor.ViewHolder viewHolder = new ConstructorAdaptor.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ConstructorAdaptor.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        ConstructionModel productInfo = productInfos.get(position);
        Glide.with(context)
                .load(productInfo.getImage())
                .into(holder.iv_vehicle_image);

        holder.travel_vehicle_name.setText(productInfo.getName());
        holder.tv_travel_category.setText(productInfo.getCategory());
        holder.tv_cost_km.setText(productInfo.getCost());
        holder.tv_vehicle_number.setText(productInfo.getExperience());

        holder.iv_const_call_bottom.setOnClickListener(view -> {
            utilitys.navigateCall(context,productInfo.getContactDetails(),productInfo.getName());
        });

        holder.iv_const_whatsapp_bottom.setOnClickListener(view -> {
            utilitys.navigateWhatsapp(context,productInfo.getContactDetails(),productInfo.getName());

        });

        holder.cv_card_const.setOnClickListener(view -> {
            Intent intent =new Intent(context, ConstructionDetailsActivity.class);
            intent.putExtra(AppConstants.pid,productInfo.getPid());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_vehicle_image,iv_const_whatsapp_bottom,iv_const_call_bottom;
        TextView travel_vehicle_name,tv_travel_category,tv_cost_km,tv_vehicle_number,tv_discription;
        CardView cv_card_const;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            travel_vehicle_name = itemView.findViewById(R.id.travel_vehicle_name);
            tv_travel_category = itemView.findViewById(R.id.tv_travel_category);
            tv_cost_km = itemView.findViewById(R.id.tv_cost_km);
            tv_vehicle_number = itemView.findViewById(R.id.tv_vehicle_number);
            iv_vehicle_image = itemView.findViewById(R.id.iv_vehicle_image);
            iv_const_whatsapp_bottom = itemView.findViewById(R.id.iv_const_whatsapp_bottom);
            iv_const_call_bottom = itemView.findViewById(R.id.iv_const_call_bottom);
            cv_card_const = itemView.findViewById(R.id.cv_card_const);
        }
    }

}
