package com.chiranths.jobportal1.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chiranths.jobportal1.Adapters.HomeEventAdapter;
import com.chiranths.jobportal1.Adapters.HomeNoticeBoardAdapter;
import com.chiranths.jobportal1.Model.NoticeBoard;
import com.chiranths.jobportal1.Model.UpcomingEvent;
import com.chiranths.jobportal1.R;

import java.util.ArrayList;
import java.util.List;

public class StartingActivity extends AppCompatActivity {

    CardView getStartedBtn;

    private List<UpcomingEvent> upcomingEventList = new ArrayList<>();
    RecyclerView recyclerViewEvent;
    private HomeEventAdapter eventHomeAdapter;

    private List<NoticeBoard> noticeBoardList = new ArrayList<>();
    RecyclerView recyclerView;
    private HomeNoticeBoardAdapter homeNoticeBoardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        getStartedBtn =  findViewById(R.id.GetStartedBtn);
        initilize();

        //Onclick implementation to navigate form one activity to another
        getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });



        // Upcoming Event

        eventHomeAdapter = new HomeEventAdapter(upcomingEventList);
        RecyclerView.LayoutManager elayoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        recyclerViewEvent.setLayoutManager(elayoutManager);
        recyclerViewEvent.setItemAnimator(new DefaultItemAnimator());
        recyclerViewEvent.setAdapter(eventHomeAdapter);
        prepareEventData();


        //Home Notice Board recycler view

        homeNoticeBoardAdapter =new HomeNoticeBoardAdapter(noticeBoardList);
        RecyclerView.LayoutManager nlayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(nlayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(homeNoticeBoardAdapter);
        prepareNoticeData();

    }



    private void initilize()
    {
        recyclerView =(RecyclerView)findViewById(R.id.rv_home_notice);
        recyclerViewEvent = (RecyclerView)findViewById(R.id.rv_home_event);
    }

    private  void prepareNoticeData()
    {
        NoticeBoard noticeBoard = new NoticeBoard("Notice 1","It’s your personal HR Management System login. It’s a one stop solution interactive portal to enable you with complete HR related activities.","12 Mar 2020");
        noticeBoardList.add(noticeBoard);

        noticeBoard = new NoticeBoard("Notice 2","It’s your personal HR Management System login. It’s a one stop solution interactive portal to enable you with complete HR related activities","12 Mar 2020");
        noticeBoardList.add(noticeBoard);


        noticeBoard = new NoticeBoard("Notice 3","It’s your personal HR Management System login. It’s a one stop solution interactive portal to enable you with complete HR related activities","12 Mar 2020");
        noticeBoardList.add(noticeBoard);

        noticeBoard = new NoticeBoard("Notice 4","It’s your personal HR Management System login. It’s a one stop solution interactive portal to enable you with complete HR related activities","12 Mar 2020");
        noticeBoardList.add(noticeBoard);

        noticeBoard = new NoticeBoard("Notice 5","It’s your personal HR Management System login. It’s a one stop solution interactive portal to enable you with complete HR related activities","12 Mar 2020");
        noticeBoardList.add(noticeBoard);

        noticeBoard = new NoticeBoard("Notice 6","It’s your personal HR Management System login. It’s a one stop solution interactive portal to enable you with complete HR related activities","12 Mar 2020");
        noticeBoardList.add(noticeBoard);

        noticeBoard = new NoticeBoard("Notice 7","It’s your personal HR Management System login. It’s a one stop solution interactive portal to enable you with complete HR related activities","12 Mar 2020");
        noticeBoardList.add(noticeBoard);


    }


    private  void prepareEventData()
    {
        UpcomingEvent upcomingEvent = new UpcomingEvent("Event 1","It’s your personal HR Management System login. It’s a one stop solution interactive portal to enable you with complete HR related activities.","12 Mar 2020");
        upcomingEventList.add(upcomingEvent);

        upcomingEvent = new UpcomingEvent("Event 2","It’s your personal HR Management System login. It’s a one stop solution interactive portal to enable you with complete HR related activities","12 Mar 2020");
        upcomingEventList.add(upcomingEvent);

        upcomingEvent = new UpcomingEvent("Event 3","It’s your personal HR Management System login. It’s a one stop solution interactive portal to enable you with complete HR related activities","12 Mar 2020");
        upcomingEventList.add(upcomingEvent);

        upcomingEvent = new UpcomingEvent("Event 4","It’s your personal HR Management System login. It’s a one stop solution interactive portal to enable you with complete HR related activities","12 Mar 2020");
        upcomingEventList.add(upcomingEvent);

        upcomingEvent = new UpcomingEvent("Event 5","It’s your personal HR Management System login. It’s a one stop solution interactive portal to enable you with complete HR related activities","12 Mar 2020");
        upcomingEventList.add(upcomingEvent);

        upcomingEvent = new UpcomingEvent("Event 6","It’s your personal HR Management System login. It’s a one stop solution interactive portal to enable you with complete HR related activities","12 Mar 2020");
        upcomingEventList.add(upcomingEvent);


        upcomingEvent = new UpcomingEvent("Event 7","It’s your personal HR Management System login. It’s a one stop solution interactive portal to enable you with complete HR related activities","12 Mar 2020");
        upcomingEventList.add(upcomingEvent);

        upcomingEvent = new UpcomingEvent("Event 8","It’s your personal HR Management System login. It’s a one stop solution interactive portal to enable you with complete HR related activities","12 Mar 2020");
        upcomingEventList.add(upcomingEvent);

    }
}