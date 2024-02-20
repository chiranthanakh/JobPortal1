package com.sbd.gbd.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sbd.gbd.Activities.BasicActivitys.LayoutDetailsActivity;
import com.sbd.gbd.Model.LayoutModel;
import com.sbd.gbd.R;
import com.sbd.gbd.Utilitys.AppConstants;

import java.util.ArrayList;

public class LayoutsAdaptor extends RecyclerView.Adapter<LayoutsAdaptor.ViewHolder> {

    private ArrayList<LayoutModel> layoutsList;
    private Context context;
    public LayoutsAdaptor(ArrayList layoutsList, Context context) {
        this.layoutsList = layoutsList;
        this.context = context;

    }

    @NonNull
    @Override
    public LayoutsAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View listItem= layoutInflater.inflate(R.layout.ads_recyclarview_layout, parent, false);
        LayoutsAdaptor.ViewHolder viewHolder = new LayoutsAdaptor.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        LayoutModel layoutinfo = layoutsList.get(position);

        RequestOptions options = new RequestOptions();
        options.fitCenter();

        Glide.with(context)
                .load(layoutinfo.getImage())
                .apply(new RequestOptions().override(1000, 500))
                .into(holder.iv_corosel_image);

        holder.tv_ads_category.setText(layoutinfo.getCategory());
        holder.tv_amount.setText(layoutinfo.getPrice());
        holder.tv_space.setText(layoutinfo.getPropertysize());
        holder.ads_location_adaptor.setText(layoutinfo.getLocation());
        holder.tv_layout_name.setText(layoutinfo.getPname());

        holder.cv_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(context, LayoutDetailsActivity.class);
                intent.putExtra(AppConstants.pid,layoutinfo.getPid());
                intent.putExtra("page","2");
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return layoutsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_corosel_image;
        TextView tv_ads_category,tv_amount,tv_space,ads_location_adaptor,tv_layout_name;
        CardView cv_card;
        LinearLayout ll_enquiry;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.iv_corosel_image=itemView.findViewById(R.id.iv_ads_image);
            this.cv_card = itemView.findViewById(R.id.cv_card);
            this.tv_ads_category=itemView.findViewById(R.id.tv_ads_category);
            this.tv_amount = itemView.findViewById(R.id.tv_ads_amount);
            this.tv_space = itemView.findViewById(R.id.tv_ads_spaces);
            this.ads_location_adaptor = itemView.findViewById(R.id.ads_location_adaptor);
            this.tv_layout_name = itemView.findViewById(R.id.tv_layout_name);
           /// this.ll_enquiry = itemView.findViewById(R.id.ll_enquiry);

        }
    }
}
