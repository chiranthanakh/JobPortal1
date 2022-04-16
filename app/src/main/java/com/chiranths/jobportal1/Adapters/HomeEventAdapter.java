package com.chiranths.jobportal1.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.chiranths.jobportal1.Activities.BasicActivitys.ProductInfo;
import com.chiranths.jobportal1.Model.UpcomingEvent;
import com.chiranths.jobportal1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeEventAdapter extends RecyclerView.Adapter<HomeEventAdapter.MyViewHolder> {

    private List<ProductInfo> productInfos;

    public HomeEventAdapter(List<ProductInfo> productInfos)
    {
        this.productInfos = productInfos;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_event_layout,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ProductInfo productInfo = productInfos.get(position);

        Picasso.get().load(productInfo.getImage()).into(holder.iv_image);
        //holder.tv_event_home_title.setText(upcomingEvent.getTitle());
        //holder.tv_event_Home_description.setText(upcomingEvent.getDesc());
        //holder.tv_event_home_date.setText(upcomingEvent.getDate());



    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

       public TextView tv_event_home_title,tv_event_Home_description,tv_event_home_date;
        public ConstraintLayout cons_layout;
        ImageView iv_image;

        public MyViewHolder(@NonNull View view) {
            super(view);
            iv_image = view.findViewById(R.id.iv_location_image);
           /* tv_event_home_title = (TextView) view.findViewById(R.id.tv_event_home_title);
            tv_event_Home_description = (TextView) view.findViewById(R.id.tv_event_Home_description);
            tv_event_home_date = (TextView) view.findViewById(R.id.tv_event_home_date);
            cons_layout= (ConstraintLayout) view.findViewById(R.id.cons_layout);*/
        }
    }
}
