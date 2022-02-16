package com.chiranths.jobportal1.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.chiranths.jobportal1.Model.NoticeBoard;
import com.chiranths.jobportal1.R;

import java.util.List;

public class HomeNoticeBoardAdapter extends RecyclerView.Adapter<HomeNoticeBoardAdapter.MyViewHolder> {

    private List<NoticeBoard> noticeBoardList;

    public HomeNoticeBoardAdapter(List<NoticeBoard> noticeBoardList)
    {
        this.noticeBoardList = noticeBoardList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_notice_board,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NoticeBoard noticeBoard = noticeBoardList.get(position);
       // holder.tv_notice_Home_title.setText(noticeBoard.getTitle());
        holder.tv_notice_Home_description.setText(noticeBoard.getDesc());
       // holder.tv_notice_home_date.setText(noticeBoard.getDate());


    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_notice_Home_title,tv_notice_Home_description,tv_notice_home_date;
        public ConstraintLayout c_layout;
        public MyViewHolder(@NonNull View view) {
            super(view);
            //tv_notice_Home_title = (TextView) view.findViewById(R.id.tv_notice_Home_title);
            tv_notice_Home_description =(TextView) view.findViewById(R.id.tv_notice_Home_description);
            //tv_notice_home_date =(TextView) view.findViewById(R.id.tv_notice_home_date);
            c_layout = (ConstraintLayout) view.findViewById(R.id.layout_cc);
        }
    }
}
