package com.chiranths.jobportal1.Adapters.admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chiranths.jobportal1.Activities.Admin.AdminPropertyApproval;
import com.chiranths.jobportal1.Activities.Propertys.PropertyDetailsActivity;
import com.chiranths.jobportal1.R;
import com.chiranths.jobportal1.Utilitys.AppConstants;

import java.util.List;

public class AdminPropertyAdaptor extends RecyclerView.Adapter<AdminPropertyAdaptor.ViewHolder> {

    private List productInfos;
    private Context context;
    AdminPropertyApproval adminPropertyApproval = new AdminPropertyApproval();

    public AdminPropertyAdaptor(List productInfos, Context context) {
        this.productInfos = productInfos;
        this.context = context;
    }

    @NonNull
    @Override
    public AdminPropertyAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View listItem= layoutInflater.inflate(R.layout.admin_product_items_layout, parent, false);
        AdminPropertyAdaptor.ViewHolder viewHolder = new AdminPropertyAdaptor.ViewHolder(listItem);
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
                .apply(new RequestOptions().override(500, 500))
                .into(holder.iv_image);

        holder.product_type1.setText(data[2]);
        holder.product_location1.setText(data[6]);
        holder.product_price.setText(data[3]);
        holder.product_size1.setText(data[5]);
        holder.product_name.setText(data[4]);

        holder.cv_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(context, PropertyDetailsActivity.class);
                intent.putExtra(AppConstants.pid,data[0]);
                context.startActivity(intent);
            }
        });

        holder.btn_approve.setOnClickListener(view -> {
            adminPropertyApproval.sendpdi("Products",data[1],2);
        });

        holder.btn_remove.setOnClickListener(view -> {
            adminPropertyApproval.sendpdi("Products",data[1],3);
        });
    }

    @Override
    public int getItemCount() {
        return productInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_image;
        CardView cv_layout;
        AppCompatButton btn_remove, btn_approve;
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
            btn_approve = itemView.findViewById(R.id.btn_admin_product_approve);
            btn_remove = itemView.findViewById(R.id.btn_admin_product_remove);

        }
    }
}
