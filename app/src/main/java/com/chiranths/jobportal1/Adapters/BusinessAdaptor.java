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

import com.chiranths.jobportal1.Activities.Propertys.PropertyDetailsActivity;
import com.chiranths.jobportal1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BusinessAdaptor extends RecyclerView.Adapter<BusinessAdaptor.ViewHolder> {


    private List productInfos;
    private Context context;

    public BusinessAdaptor(List productInfos, Context context) {
        this.productInfos = productInfos;
        this.context = context;
    }

    @NonNull
    @Override
    public BusinessAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View listItem= layoutInflater.inflate(R.layout.business_items_layout, parent, false);
        BusinessAdaptor.ViewHolder viewHolder = new BusinessAdaptor.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String propertyinfo = String.valueOf(productInfos.get(position));

        String[] imagesdata = propertyinfo.split("!!");
        String[] data = imagesdata[1].split("---");
        String[] imageurl = imagesdata[0].split("---");

        Picasso.get()
                .load(imageurl[0])
                .into(holder.business_image);

        holder.tv_business_type.setText(data[0]);
        holder.tv_business_locatin.setText(data[5]);
        holder.tv_business_name.setText(data[1]);
        holder.tv_business_servicess.setText(data[3]);

        holder.cv_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(context, PropertyDetailsActivity.class);
                intent.putExtra("pid",data[7]);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView business_image;
        CardView cv_layout;
        TextView tv_business_locatin,tv_business_name,tv_business_type,tv_business_servicess;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_business_locatin = itemView.findViewById(R.id.tv_business_locatin);
            tv_business_name = itemView.findViewById(R.id.tv_business_name);
            tv_business_type = itemView.findViewById(R.id.tv_business_type);
            cv_layout = itemView.findViewById(R.id.cv_layout2);
            business_image = itemView.findViewById(R.id.business_image);
            tv_business_servicess = itemView.findViewById(R.id.business_servicess);
        }
    }
}
