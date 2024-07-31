package com.sbd.gbd.Adapters.admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sbd.gbd.Activities.Admin.AdminPropertyApproval;
import com.sbd.gbd.Activities.Dashboard.AdsDetailsActivity;
import com.sbd.gbd.R;
import com.sbd.gbd.Utilitys.AppConstants;

import java.util.ArrayList;

public class AdminAdsAdaptor extends RecyclerView.Adapter<AdminAdsAdaptor.ViewHolder> {


    private ArrayList noticeBoardList;
    private Context context;
    AdminPropertyApproval adminPropertyApproval = new AdminPropertyApproval();

    public AdminAdsAdaptor(ArrayList noticeBoardList, Context context) {
        this.noticeBoardList = noticeBoardList;
        this.context = context;

    }

    @NonNull
    @Override
    public AdminAdsAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View listItem= layoutInflater.inflate(R.layout.admin_ads_recy_layout, parent, false);
        AdminAdsAdaptor.ViewHolder viewHolder = new AdminAdsAdaptor.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final String noticeimage = String.valueOf(noticeBoardList.get(position));

        String[] data = noticeimage.split("---");

        Glide.with(context)
                .load(data[0])
                .apply(new RequestOptions().override(500, 500))
                .into(holder.iv_corosel_image);

        holder.tv_ads_category.setText(data[2]);
        holder.tv_amount.setText(data[3]);
        holder.tv_space.setText(data[4]);
        holder.ads_location_adaptor.setText(data[6]);

        if(data[7].equals("1")){
            holder.tv_ads_status.setText("Pending for Approval");
            holder.tv_ads_status.setTextColor(Color.parseColor("#E04006"));
            holder.cv_card.setCardBackgroundColor(Color.parseColor("#DFFFA5"));
        }else if(data[7].equals("2")){
            holder.tv_ads_status.setText("Approved");
            holder.btn_approve.setVisibility(View.GONE);
        }

        holder.btn_approve.setOnClickListener(view -> {
            adminPropertyApproval.sendpdi(AppConstants.ads,data[1],2);
        });

        holder.btn_remove.setOnClickListener(view -> {
            adminPropertyApproval.sendpdi(AppConstants.ads,data[1],3);
        });
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
        TextView tv_ads_category,tv_amount,tv_space,ads_location_adaptor,tv_ads_status;
        CardView cv_card;
        AppCompatButton btn_remove, btn_approve;
        LinearLayout ll_enquiry;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.iv_corosel_image=itemView.findViewById(R.id.iv_ads_image);
            this.cv_card = itemView.findViewById(R.id.cv_card);
            this.tv_ads_category=itemView.findViewById(R.id.tv_ads_category);
            this.tv_amount = itemView.findViewById(R.id.tv_ads_amount);
            this.tv_space = itemView.findViewById(R.id.tv_ads_spaces);
            this.ads_location_adaptor = itemView.findViewById(R.id.ads_location_adaptor);
            btn_approve = itemView.findViewById(R.id.btn_admin_aprove);
            btn_remove = itemView.findViewById(R.id.btn_admin_pending);
            cv_card = itemView.findViewById(R.id.cv_admin_card_ads);
            tv_ads_status = itemView.findViewById(R.id.tv_ads_status);
           /// this.ll_enquiry = itemView.findViewById(R.id.ll_enquiry);

        }
    }
}
