package com.reformation.home;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.gson.JsonObject;

import apihandler.ApiClient;
import apihandler.ApiInterface;
import model.EventModel;
import model.TopicweekResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Constant;
import utils.CustomProgresDialog;
import utils.LogUtil;
import utils.Utils;

import static utils.Utils.isAndroid5;

public class EventDetailActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String EXTRA_PARAM_ID = "place_id";
    private ImageView topicImgview,leftImg,
            rightFilterImg,imageCalandar,imageViewShare;
    private TextView topicTitle,topicDesc,topic_date,topicHeader,textViewMap,
            textViewTopicSubTitle1,artist,textViewTicket;
    private LinearLayout catgList;
    private FrameLayout mainView;
    private EventModel event;
    private RelativeLayout mapFrame;
    private RecyclerView horizontal_recycler_eventView,recyclerview_audioguide;
    private ProgressBar progressBar;
    private ApiInterface mApiInterface;
    private CustomProgresDialog dlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        dlg = CustomProgresDialog.getInstance(this);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        if(isAndroid5()){
            windowTransition();
        }else{
            makeEnterTransition();
        }

        topicImgview =(ImageView)findViewById(R.id.homeMenuImg);
        progressBar = (ProgressBar)findViewById(R.id.dlg);

        topicHeader =(TextView)findViewById(R.id.textViewHeaderTitle);
        leftImg=(ImageView)findViewById(R.id.imageViewLeft);
        rightFilterImg=(ImageView)findViewById(R.id.imageViewRight);

        topicTitle =(TextView)findViewById(R.id.txtEventName);
        topicDesc =(TextView)findViewById(R.id.textViewTopicDesc);
        topic_date =(TextView)findViewById(R.id.txtEventTime);

        textViewMap =(TextView)findViewById(R.id.textViewMap);
        textViewTopicSubTitle1 =(TextView)findViewById(R.id.textViewTopicSubTitle1);
        artist =(TextView)findViewById(R.id.artist);
        textViewTicket =(TextView)findViewById(R.id.textViewTicket);

        imageCalandar=(ImageView)findViewById(R.id.imageCalandar);
        imageViewShare=(ImageView)findViewById(R.id.imageViewShare);
        catgList=(LinearLayout) findViewById(R.id.catgList);
        mapFrame=(RelativeLayout) findViewById(R.id.mapFrame);

        topicHeader.setText(getResources().getString(R.string.program_text));
        rightFilterImg.setImageResource(R.drawable.heart);
        leftImg.setOnClickListener(this);
        mapFrame.setOnClickListener(this);
        imageViewShare.setOnClickListener(this);

        event = (EventModel) getIntent().getSerializableExtra("DATA");
        if(event!=null){
            String id = event.getId();
            topicTitle.setText(event.getTitle());
            String sdate = Utils.formatDate(event.getDate());
            String dtTxt =  Utils.getWeekNameFromDay(event.getDate())+", "+Utils.getDaywithTHFormatFromDate(sdate)+" "+Utils.getMonthFromDate(sdate)+" | "+ event.getStart().split(":")[0]+"h -";
            topic_date.setText(dtTxt);
            createEventCatgList(this,catgList,event.getCategory());
            textViewTicket.setText(getResources().getString(R.string.ticket_info));
            getEventDetails(id);
        }


    }



    private void getEventDetails(String id) {

        dlg.showDialog();
        Call<JsonObject> call = mApiInterface.getEventDetailById(Constant.SELECTED_LANG,id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dlg.hideDialog();
                if (response.isSuccessful()) {
                    JsonObject model = response.body();
                    LogUtil.createLog("Response ::",model.toString());
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("onFailure ::", t.getMessage());
                if (dlg != null)
                    dlg.hideDialog();

            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void windowTransition() {
        getWindow().setEnterTransition(makeEnterTransition());

    }





    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static Transition makeEnterTransition() {
        Transition fade = new Fade();
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        return fade;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageViewLeft:
                super.onBackPressed();
                break;

        }
    }


    public static void createEventCatgList(Context ctx, LinearLayout linearLayout, String catg){
        if(catg!=null){
            String[] catList = catg.split(",");
            if(linearLayout.getChildCount()>0){
                linearLayout.removeAllViews();
            }
            for (int i = 0; i <catList.length ; i++) {
                // if(holder.linearLayout.getChildCount()<catList.length) {
                TextView viewTxt = (TextView) LayoutInflater.from(ctx).inflate(R.layout.event_catg_list, null);
                viewTxt.setText(catList[i].toString());
                viewTxt.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.catg_bg));
                linearLayout.addView(viewTxt);
            }
        }
    }
}
