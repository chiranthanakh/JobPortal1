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
import com.sbd.gbd.Activities.BasicActivitys.AdsDetailsActivity;
import com.sbd.gbd.R;
import com.sbd.gbd.Utilitys.AppConstants;

import java.util.ArrayList;

public class AdminpropertyapprovalAdaptor extends RecyclerView.Adapter<AdminpropertyapprovalAdaptor.ViewHolder> {


    private ArrayList noticeBoardList;
    private Context context;


    public AdminpropertyapprovalAdaptor(ArrayList noticeBoardList, Context context) {
        this.noticeBoardList = noticeBoardList;
        this.context = context;

    }

    @NonNull
    @Override
    public AdminpropertyapprovalAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View listItem= layoutInflater.inflate(R.layout.ads_recyclarview_layout, parent, false);
        AdminpropertyapprovalAdaptor.ViewHolder viewHolder = new AdminpropertyapprovalAdaptor.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final String noticeimage = String.valueOf(noticeBoardList.get(position));

        String[] data = noticeimage.split("---");

        /*Picasso.get()
                .load(data[0])
                //.centerCrop()
                //.resize(150,120)
                .into(holder.iv_corosel_image);*/

        Glide.with(context)
                .load(data[0])
                .into(holder.iv_corosel_image);

        holder.tv_ads_category.setText(data[2]);
        holder.tv_amount.setText(data[3]);
        holder.tv_space.setText(data[4]);
        holder.ads_location_adaptor.setText(data[6]);

        holder.cv_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(context, AdsDetailsActivity.class);
                intent.putExtra(AppConstants.pid,data[1]);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return noticeBoardList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_corosel_image;
        TextView tv_ads_category,tv_amount,tv_space,ads_location_adaptor;
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
           /// this.ll_enquiry = itemView.findViewById(R.id.ll_enquiry);

        }
    }
}
