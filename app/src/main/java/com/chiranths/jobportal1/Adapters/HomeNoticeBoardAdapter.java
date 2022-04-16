package com.chiranths.jobportal1.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.chiranths.jobportal1.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeNoticeBoardAdapter extends RecyclerView.Adapter<HomeNoticeBoardAdapter.MyViewHolder> {

   // private List<NoticeBoard> noticeBoardList;
    private ArrayList noticeBoardList;

    public HomeNoticeBoardAdapter(ArrayList noticeBoardList)
    {
        this.noticeBoardList = noticeBoardList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_event_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String noticeimage = String.valueOf(noticeBoardList.get(position));

        Picasso.get().load(noticeimage).into(holder.iv_corosel_image);
       // holder.tv_notice_Home_title.setText(noticeBoard.getTitle());
      //  holder.tv_notice_Home_description.setText(noticeBoard.getDesc());
       // holder.tv_notice_home_date.setText(noticeBoard.getDate());

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_notice_Home_title,tv_notice_Home_description,tv_notice_home_date;
        ImageView iv_corosel_image;
        public ConstraintLayout c_layout;
        public MyViewHolder(@NonNull View view) {
            super(view);
            //tv_notice_Home_title = (TextView) view.findViewById(R.id.tv_notice_Home_title);
            iv_corosel_image = view.findViewById(R.id.iv_location_image);
            //tv_notice_home_date =(TextView) view.findViewById(R.id.tv_notice_home_date);
           // c_layout = (ConstraintLayout) view.findViewById(R.id.layout_cc);
        }
    }
}
