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
import com.chiranths.jobportal1.Activities.BasicActivitys.AdsDetailsActivity;
import com.chiranths.jobportal1.CalldetailsRecords;
import com.chiranths.jobportal1.R;
import com.chiranths.jobportal1.Utilitys.AppConstants;
import com.chiranths.jobportal1.Utilitys.Utilitys;

import java.util.List;

public class PropertyAdaptor extends RecyclerView.Adapter<PropertyAdaptor.ViewHolder> {


    private List productInfos;
    private Context context;
    CalldetailsRecords calldetails = new CalldetailsRecords() ;
    Utilitys utilitys = new Utilitys();


    public PropertyAdaptor(List productInfos, Context context) {
        this.productInfos = productInfos;
        this.context = context;
    }

    @NonNull
    @Override
    public PropertyAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View listItem= layoutInflater.inflate(R.layout.product_items_layout, parent, false);
        PropertyAdaptor.ViewHolder viewHolder = new PropertyAdaptor.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String propertyinfo = String.valueOf(productInfos.get(position));

        String[] imagesdata = propertyinfo.split("!!");
        String[] data = imagesdata[1].split("---");
        String[] imageurl = imagesdata[0].split("---");

        Glide.with(context)
                .load(imageurl[0])
                .into(holder.iv_image);

        holder.product_type1.setText(data[2]);
        holder.product_location1.setText(data[6]);
        holder.product_price.setText(data[3]);
        holder.product_size1.setText(data[5]);
        holder.product_name.setText(data[4]);

        holder.cv_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(context, AdsDetailsActivity.class);
                intent.putExtra(AppConstants.pid,data[0]);
                intent.putExtra("page","2");
                context.startActivity(intent);
            }
        });

        //calling function
        holder.property_btn_call.setOnClickListener(view -> utilitys.navigateCall(context,data[7],data[0]));

        //whatsapp function
        holder.property_btn_whatsapp.setOnClickListener(view ->
                utilitys.navigateWhatsapp(context,data[7],data[0]));

    }

    @Override
    public int getItemCount() {
        return productInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_image,property_btn_whatsapp,property_btn_call;
        CardView cv_layout;
        TextView product_name,product_price,product_size1,product_location1,product_type1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            property_btn_whatsapp = itemView.findViewById(R.id.property_btn_whatsapp);
            property_btn_call = itemView.findViewById(R.id.property_btn_call);
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
