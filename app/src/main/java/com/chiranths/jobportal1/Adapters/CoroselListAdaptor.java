package com.chiranths.jobportal1.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chiranths.jobportal1.Activities.BasicActivitys.CoroselDetailsActivity;
import com.chiranths.jobportal1.Activities.Propertys.PropertyDetailsActivity;
import com.chiranths.jobportal1.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CoroselListAdaptor extends RecyclerView.Adapter<CoroselListAdaptor.ViewHolder> {

    private ArrayList noticeBoardList;
    private Context context;

    public CoroselListAdaptor(ArrayList noticeBoardList, Context context) {
        this.noticeBoardList = noticeBoardList;
        this.context = context;

    }

    @NonNull
    @Override
    public CoroselListAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View listItem= layoutInflater.inflate(R.layout.home_colorsel_layout, parent, false);
        CoroselListAdaptor.ViewHolder viewHolder = new CoroselListAdaptor.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final String noticeimage = String.valueOf(noticeBoardList.get(position));

        /*Picasso.get().load(noticeimage)
                //.centerCrop()
                //.resize(350,140)
                .into(holder.iv_corosel_image);*/
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
