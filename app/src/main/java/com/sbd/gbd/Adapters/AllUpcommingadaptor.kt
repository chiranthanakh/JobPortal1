package com.sbd.gbd.Adapters;

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
import com.sbd.gbd.Activities.Dashboard.AdsDetailsActivity;
import com.sbd.gbd.Model.PropertytModel;
import com.sbd.gbd.R;
import com.sbd.gbd.Utilitys.AppConstants;

import java.util.List;

public class AllUpcommingadaptor extends RecyclerView.Adapter<AllUpcommingadaptor.ViewHolder>{

    private List<PropertytModel> productInfos;
    private Context context;

    public AllUpcommingadaptor(List<PropertytModel> productInfos, Context context) {
        this.productInfos = productInfos;
        this.context = context;
    }

    @NonNull
    @Override
    public AllUpcommingadaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View listItem= layoutInflater.inflate(R.layout.product_items_layout, parent, false);
        AllUpcommingadaptor.ViewHolder viewHolder = new AllUpcommingadaptor.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AllUpcommingadaptor.ViewHolder holder, int position) {
        PropertytModel propertyinfo = productInfos.get(position);

        String imagesdata = propertyinfo.getImage();

        Glide.with(context)
                .load(imagesdata)
                .into(holder.iv_image);

        holder.product_type1.setText(propertyinfo.getType());
        holder.product_location1.setText(propertyinfo.getLocation());
        holder.product_price.setText(propertyinfo.getPrice());
        holder.product_size1.setText(propertyinfo.getPropertysize());
        holder.product_name.setText(propertyinfo.getPname());

        holder.cv_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(context, AdsDetailsActivity.class);
                intent.putExtra(AppConstants.pid,propertyinfo.getPid());
                intent.putExtra("page","1");
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
        CardView cv_layout;
        TextView product_name,product_price,product_size1,product_location1,product_type1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_image = itemView.findViewById(R.id.product_image);
            product_name = itemView.findViewById(R.id.product_name1);
            product_price = itemView.findViewById(R.id.product_price);
            product_size1 = itemView.findViewById(R.id.product_size1);
            product_location1 = itemView.findViewById(R.id.product_location1);
            product_type1 = itemView.findViewById(R.id.product_type1);
            cv_layout = itemView.findViewById(R.id.cv_layout);
        }
    }


}

