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
import com.chiranths.jobportal1.Activities.Businesthings.BusinessActivity;
import com.chiranths.jobportal1.Activities.Businesthings.BusinessDetailsActivity;
import com.chiranths.jobportal1.Activities.Businesthings.BusinessFilter;
import com.chiranths.jobportal1.Model.BusinessModel;
import com.chiranths.jobportal1.Model.Categorymmodel;
import com.chiranths.jobportal1.R;

import java.util.List;

public class BusinessCategoryAdaptor extends RecyclerView.Adapter<BusinessCategoryAdaptor.ViewHolder> {

    BusinessActivity businessActivity = new BusinessActivity();

    private List<Categorymmodel> productInfos;
    private Context context;

    public BusinessCategoryAdaptor(List<Categorymmodel> productInfos, Context context) {
        this.productInfos = productInfos;
        this.context = context;
    }

    @NonNull
    @Override
    public BusinessCategoryAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View listItem = layoutInflater.inflate(R.layout.business_gridlayout_adaptor, parent, false);
        BusinessCategoryAdaptor.ViewHolder viewHolder = new BusinessCategoryAdaptor.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Categorymmodel businessInfo = productInfos.get(position);

        Glide.with(context)
                .load(businessInfo.getImage())
                .into(holder.business_image);

        holder.tv_business_cat.setText(businessInfo.getCategory());


        holder.business_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BusinessFilter.class);
                context.startActivity(intent);
               // businessActivity.filter(holder.tv_business_cat.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return productInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView business_image;

        TextView tv_business_cat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_business_cat = itemView.findViewById(R.id.tv_business_category);
            business_image = itemView.findViewById(R.id.iv_grid_image);
        }
    }
}
