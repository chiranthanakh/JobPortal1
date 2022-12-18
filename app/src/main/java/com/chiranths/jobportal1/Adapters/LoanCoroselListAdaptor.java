package com.chiranths.jobportal1.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chiranths.jobportal1.Activities.BasicActivitys.CoroselDetailsActivity;
import com.chiranths.jobportal1.Activities.Propertys.PropertyDetailsActivity;
import com.chiranths.jobportal1.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LoanCoroselListAdaptor extends RecyclerView.Adapter<LoanCoroselListAdaptor.ViewHolder> {

    private ArrayList noticeBoardList;
    private Context context;

    public LoanCoroselListAdaptor(ArrayList noticeBoardList, Context context) {
        this.noticeBoardList = noticeBoardList;
        this.context = context;

    }

    @NonNull
    @Override
    public LoanCoroselListAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View listItem= layoutInflater.inflate(R.layout.home_colorsel_layout, parent, false);
        LoanCoroselListAdaptor.ViewHolder viewHolder = new LoanCoroselListAdaptor.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final String noticeimage = String.valueOf(noticeBoardList.get(position));

        Glide.with(context)
                .load(noticeimage)
                .into(holder.iv_corosel_image);

        holder.iv_corosel_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CoroselDetailsActivity.class);
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


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.iv_corosel_image=itemView.findViewById(R.id.iv_corosel_image);


        }
    }
}
