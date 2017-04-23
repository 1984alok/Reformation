package com.reformation.home;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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


import com.google.android.gms.location.places.PlaceDetectionApi;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.reformation.home.fragment.DividerItemDecoration;

import java.util.ArrayList;

import adapter.AudioAdapter;
import adapter.GalleryAdapter;
import apihandler.ApiClient;
import apihandler.ApiInterface;
import model.Audio;
import model.EventDetailGateData;
import model.EventDetailPlaceData;
import model.EventModel;
import model.EventResponseData;
import model.EventdetailResponse;
import model.Exhibitor;
import model.Gallery;
import model.GateModel;
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
    private ImageView leftImg,
            rightFilterImg,imageCalandar,imageViewShare;
    private TextView topicTitle,topicDesc,topic_date,topicHeader,textViewaddrss,
            textViewTopicSubTitle1,artist,textViewTicket;
    private LinearLayout catgList;
    private FrameLayout mainView;
    private EventModel event;
    private RelativeLayout mapFrame;
    private RecyclerView eventGalleryView,recyclerview_audioguide;
    private GalleryAdapter galleryAdapter;
    private AudioAdapter audioAdapter;
    private LinearLayoutManager layoutManager,audiLayoutManager;
    private ApiInterface mApiInterface;
    private CustomProgresDialog dlg;

    private ArrayList<Gallery> galleries;
    private ArrayList<Audio> audios;
    private Exhibitor exhibitor;
    private GateModel gateModel;
    LatLng location;


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


        topicHeader =(TextView)findViewById(R.id.textViewHeaderTitle);
        leftImg=(ImageView)findViewById(R.id.imageViewLeft);
        rightFilterImg=(ImageView)findViewById(R.id.imageViewRight);

        topicTitle =(TextView)findViewById(R.id.txtEventName);
        topicDesc =(TextView)findViewById(R.id.textViewTopicDesc);
        topic_date =(TextView)findViewById(R.id.txtEventTime);

        textViewaddrss =(TextView)findViewById(R.id.textViewMap);
        textViewTopicSubTitle1 =(TextView)findViewById(R.id.textViewTopicSubTitle1);
        artist =(TextView)findViewById(R.id.artist);
        textViewTicket =(TextView)findViewById(R.id.textViewTicket);

        imageCalandar=(ImageView)findViewById(R.id.imageCalandar);
        imageViewShare=(ImageView)findViewById(R.id.imageViewShare);
        catgList=(LinearLayout) findViewById(R.id.catgList);
        mapFrame=(RelativeLayout) findViewById(R.id.mapFrame);

        eventGalleryView = (RecyclerView)findViewById(R.id.horizontal_recycler_eventView);
        recyclerview_audioguide = (RecyclerView)findViewById(R.id.recyclerview_audioguide);
        recyclerview_audioguide.setFocusable(false);
        recyclerview_audioguide.setNestedScrollingEnabled(false);
        layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        audiLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

       // Drawable drawable = getResources().getDrawable(R.drawable.line_devider);
       // eventGalleryView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL_LIST,drawable));

        recyclerview_audioguide.setHasFixedSize(true);
        eventGalleryView.setHasFixedSize(true);

        eventGalleryView.setLayoutManager(layoutManager);
        recyclerview_audioguide.setLayoutManager(audiLayoutManager);


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

        LogUtil.createLog("Eventdetail By Id :",id);

        dlg.showDialog();
        Call<EventdetailResponse> call = mApiInterface.getEventDetailById(Constant.SELECTED_LANG,id);
        call.enqueue(new Callback<EventdetailResponse>() {
            @Override
            public void onResponse(Call<EventdetailResponse> call, Response<EventdetailResponse> response) {
                dlg.hideDialog();
                if (response.isSuccessful()) {
                    EventdetailResponse eventdetailResponse = response.body();
                    if(eventdetailResponse!=null&&eventdetailResponse.getStatus()){
                        EventResponseData eventResponseData = eventdetailResponse.getResponseData();
                        if(eventResponseData!=null) {
                            event = eventResponseData.getEventDetails();
                            EventDetailGateData eventDetailGateData = eventResponseData.getGateData();

                            if(eventDetailGateData!=null) {
                                gateModel = eventDetailGateData.getData();
                                audios = eventDetailGateData.getAudio();
                                galleries = eventDetailGateData.getGallery();
                            }

                            EventDetailPlaceData eventDetailPlaceData = eventResponseData.getPlaceData();
                            if(eventDetailPlaceData!=null) {
                                exhibitor = eventDetailPlaceData.getData();

                                ArrayList<Audio> audioArrayList = eventDetailPlaceData.getGateData().getAudio();
                                if (audioArrayList != null && audioArrayList.size() > 0)
                                    audios.addAll(audioArrayList);

                                ArrayList<Gallery> galleryArrayList = eventDetailPlaceData.getGateData().getGallery();
                                if (galleryArrayList != null && galleryArrayList.size() > 0)
                                    galleries.addAll(galleryArrayList);
                            }

                            loadDataInView();

                        }

                    }

                }

            }

            @Override
            public void onFailure(Call<EventdetailResponse> call, Throwable t) {
                Log.d("onFailure ::", t.getMessage());
                if (dlg != null)
                    dlg.hideDialog();

            }
        });
    }

    private void loadDataInView() {

        try {
        if(event!=null){
            topicTitle.setText(event.getTitle());
            textViewTopicSubTitle1.setText(event.getSubtitle());
            artist.setText(event.getSpeaker());

            String sdate = Utils.formatDate(event.getDate());
            String dtTxt =  Utils.getWeekNameFromDay(event.getDate())+", "+
                    Utils.getDaywithTHFormatFromDate(sdate)+" "+
                    Utils.getMonthFromDate(sdate)+" | "+ event.getStart().split(":")[0]+"h -"+event.getEnd().split(":")[0]+"h";
            topic_date.setText(dtTxt);
            createEventCatgList(this,catgList,event.getCategory());
            textViewTicket.setText(getResources().getString(R.string.ticket_info)+":"+event.getTicket());
            topicDesc.setText(event.getDescp());
        }

        if(exhibitor!=null){
            textViewaddrss.setText(exhibitor.getStreet()+exhibitor.getCity()+exhibitor.getZip()+exhibitor.getCountry());
            location = new LatLng(Double.parseDouble(exhibitor.getLatitude()), Double.parseDouble(exhibitor.getLongitude()));
        }

        if(audios!=null&&audios.size()>0){
            audioAdapter = new AudioAdapter(this,audios);
            recyclerview_audioguide.setAdapter(audioAdapter);
        }
        if(galleries!=null&&galleries.size()>0){
            galleryAdapter = new GalleryAdapter(this,galleries);
            eventGalleryView.setAdapter(galleryAdapter);
        }

        }catch (NumberFormatException e){
            e.printStackTrace();
        }
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
