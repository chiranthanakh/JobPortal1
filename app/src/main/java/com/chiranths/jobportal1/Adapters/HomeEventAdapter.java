package com.chiranths.jobportal1.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.chiranths.jobportal1.Model.UpcomingEvent;
import com.chiranths.jobportal1.R;

import java.util.List;

public class HomeEventAdapter extends RecyclerView.Adapter<HomeEventAdapter.MyViewHolder> {

    private List<UpcomingEvent> upcomingEventList;

    public HomeEventAdapter(List<UpcomingEvent> upcomingEventList)
    {
        this.upcomingEventList = upcomingEventList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_event_layout,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        UpcomingEvent upcomingEvent = upcomingEventList.get(position);
        holder.tv_event_home_title.setText(upcomingEvent.getTitle());
        holder.tv_event_Home_description.setText(upcomingEvent.getDesc());
        holder.tv_event_home_date.setText(upcomingEvent.getDate());



    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

       public TextView tv_event_home_title,tv_event_Home_description,tv_event_home_date;
        public ConstraintLayout cons_layout;
        public MyViewHolder(@NonNull View view) {
            super(view);
            tv_event_home_title = (TextView) view.findViewById(R.id.tv_event_home_title);
            tv_event_Home_description = (TextView) view.findViewById(R.id.tv_event_Home_description);
            tv_event_home_date = (TextView) view.findViewById(R.id.tv_event_home_date);
            cons_layout= (ConstraintLayout) view.findViewById(R.id.cons_layout);
        }
    }
}
