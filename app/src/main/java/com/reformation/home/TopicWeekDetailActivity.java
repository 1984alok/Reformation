package com.reformation.home;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.reformation.home.fragment.DividerItemDecoration;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import adapter.HomeEventAdapter;
import adapter.TopicMonthWiseAdapter;
import apihandler.ApiClient;
import apihandler.ApiInterface;
import model.TopicweekResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Constant;
import utils.CustomProgresDialog;
import utils.FontUtls;
import utils.LoadInPicasso;
import utils.Utils;

public class TopicWeekDetailActivity extends AppCompatActivity implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener{

    ApiInterface mApiInterface;
    ArrayList<String> evenCatg;
    ArrayList<TopicweekResponse.Event> eventList;
    private ImageView topicImgview,leftImg,rightFilterImg;
    private TextView topicTitle,topicDesc,topic_date,topicHeader;
    private CustomProgresDialog dlg;
    private HomeEventAdapter homeEventAdapter;
    private RecyclerView eventRecyclerView;
    private LinearLayoutManager layoutManager;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private TopicweekResponse topicweekResponse;
    ProgressBar progressBar;
    TopicMonthWiseAdapter topicMonthWiseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_week_detail);
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.colorPrimary, R.color.blue);
        topicImgview =(ImageView)findViewById(R.id.homeMenuImg);
        progressBar = (ProgressBar)findViewById(R.id.dlg);
        leftImg=(ImageView)findViewById(R.id.imageViewLeft);
        rightFilterImg=(ImageView)findViewById(R.id.imageViewRight);
        topicTitle =(TextView)findViewById(R.id.textViewTopicTitle);
        topicDesc =(TextView)findViewById(R.id.textViewTopicDesc);
        topic_date =(TextView)findViewById(R.id.textViewTopicDate);
        topicHeader =(TextView)findViewById(R.id.textViewHeaderTitle);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        eventRecyclerView = (RecyclerView)findViewById(R.id.horizontal_recycler_eventView);
        eventRecyclerView.setFocusable(false);
        eventRecyclerView.setNestedScrollingEnabled(false);
        layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

       // Drawable drawable = getResources().getDrawable(R.drawable.anniversary_devider);
       // eventRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL_LIST,drawable));
        eventRecyclerView.setHasFixedSize(true);
        eventRecyclerView.setLayoutManager(layoutManager);
        rightFilterImg.setImageResource(R.drawable.filter_blue);
        leftImg.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        dlg = CustomProgresDialog.getInstance(this);
        topicHeader.setText(getResources().getString(R.string.topic_week_title));
        topicweekResponse = (TopicweekResponse) getIntent().getSerializableExtra("Data");
        if(topicweekResponse!=null)
        loadTopicWeek(topicweekResponse);
    }


    private void loadTopicWeek(TopicweekResponse model) {
        TopicweekResponse.TopicWeekModel topicWeekModel = model.getResponseData().get(0);
        if(topicWeekModel!=null){
                eventList = topicWeekModel.getEvent();
            if(eventList!=null) {
                homeEventAdapter = new HomeEventAdapter(this, eventList, Constant.EVENT_TOPIC_DETAIL_TYPE);
                eventRecyclerView.setAdapter(homeEventAdapter);
            }

            if(topicWeekModel.getHeaderPic()!=null) {
                progressBar.setVisibility(View.VISIBLE);
                topicImgview.setVisibility(View.GONE);
                LoadInPicasso.getInstance(this).loadPic(topicImgview,progressBar,topicWeekModel.getHeaderPic());

            }

            String sdate = Utils.formatDate(topicWeekModel.getPerStart());
            String edate = Utils.formatDate(topicWeekModel.getPerEnd());

            topic_date.setText(getResources().getString(R.string.topic_date)+" "+
                    Utils.getDaywithTHFormatFromDate(sdate)+"-"+Utils.getDaywithTHFormatFromDate(edate)+" "+
            Utils.getMonthFromDate(edate));
            topicTitle.setText(topicWeekModel.getToWeekTitle());
            topicDesc.setText(topicWeekModel.getToWeekDes()!=null?topicWeekModel.getToWeekDes():getResources().getString(R.string.topic_desc));

            FontUtls.loadFont(this,"fonts/RobotoCondensed-Bold.ttf",topicTitle);
          //  FontUtls.loadFont(this,"fonts/RobotoCondensed-Bold.ttf",topicDesc);


        }


    }


    private void getTopicWeek(){

        dlg.showDialog();
        Call<TopicweekResponse> call = mApiInterface.getTopicWeek(Constant.SELECTED_LANG);
        call.enqueue(new Callback<TopicweekResponse>() {
            @Override
            public void onResponse(Call<TopicweekResponse> call, Response<TopicweekResponse> response) {
                dlg.hideDialog();
                if(response.isSuccessful()){
                    TopicweekResponse model = response.body();
                    loadTopicWeek(model);
                }

            }

            @Override
            public void onFailure(Call<TopicweekResponse> call, Throwable t) {
                Log.d("onFailure ::",t.getMessage());
                if(dlg!=null)
                    dlg.hideDialog();

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageViewLeft:
                super.onBackPressed();
                break;

        }
        
    }

    @Override
    public void onRefresh() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshContent();
            }
        },2000);

    }


    private void refreshContent(){
        getTopicWeek();
        mSwipeRefreshLayout.setRefreshing(false);
    }

}


